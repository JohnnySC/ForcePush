package johnnysc.github.forcepush.data.chats

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.core.FirebaseDatabaseProvider
import johnnysc.github.forcepush.core.Save
import johnnysc.github.forcepush.data.chat.ChatId
import johnnysc.github.forcepush.data.chat.MessageData
import johnnysc.github.forcepush.data.login.UserInitial
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 02.09.2021
 */
interface ChatsRepository : Save<String> {

    fun stopGettingUpdates()
    fun startGettingUpdates(callback: ChatsDataRealtimeUpdateCallback)
    suspend fun userInfo(userId: String): UserInfoData

    class Base(//todo use cloudDataSource
        private val firebaseDatabaseProvider: FirebaseDatabaseProvider,
        private val userIdContainer: Save<String>
    ) : ChatsRepository {
        private val myUid = Firebase.auth.currentUser!!.uid

        private var callback: ChatsDataRealtimeUpdateCallback =
            ChatsDataRealtimeUpdateCallback.Empty

        override fun stopGettingUpdates() {
            callback = ChatsDataRealtimeUpdateCallback.Empty
            firebaseDatabaseProvider.provideDatabase()
                .child("users")
                .child(myUid)
                .child("chats")
                .removeEventListener(chatsEventListener)
            listenersMap.forEach { (chatId, listener) ->
                firebaseDatabaseProvider.provideDatabase()
                    .child("chats")
                    .child(chatId)
                    .removeEventListener(listener)
            }
        }

        private val chatsEventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) =
                startGettingChats(snapshot.children.mapNotNull { it.key })

            override fun onCancelled(error: DatabaseError) = Unit
        }

        override fun startGettingUpdates(callback: ChatsDataRealtimeUpdateCallback) {
            this.callback = callback
            firebaseDatabaseProvider.provideDatabase()
                .child("users")
                .child(myUid)
                .child("chats")
                .addValueEventListener(chatsEventListener)
        }

        private fun startGettingChats(users: List<String>) {
            val chats = users.map { userId -> ChatId(Pair(myUid, userId)) }
            chats.forEach { chatId -> startListeningChat(chatId) }
        }

        private fun startListeningChat(chatId: ChatId) {
            val listener = listener(chatId)
            firebaseDatabaseProvider.provideDatabase()
                .child("chats")
                .child(chatId.value())
                .addValueEventListener(listener)
        }

        private fun listener(chatId: ChatId) = if (listenersMap.containsKey(chatId.value()))
            listenersMap[chatId.value()]!!
        else {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val allMessages = snapshot.children.mapNotNull { message ->
                        message.getValue(MessageData.Base::class.java)
                    }
                    val notReadMessagesCount = allMessages.filter { messageData ->
                        messageData.userId != myUid && !messageData.wasRead
                    }.size
                    val lastMessage = allMessages.last()
                    val chatData =
                        ChatData.Base(chatId.otherUserId(), lastMessage, notReadMessagesCount)
                    processChat(chatId.value(), chatData)
                }

                override fun onCancelled(error: DatabaseError) = Unit
            }
            listenersMap[chatId.value()] = listener
            listener
        }

        private fun processChat(chatId: String, chatData: ChatData) {
            chatsMap[chatId] = chatData
            val chats = chatsMap.map { it.value }
            callback.updateChats(chats)
        }

        private val chatsMap = mutableMapOf<String, ChatData>()
        private val listenersMap = mutableMapOf<String, ValueEventListener>()

        override fun save(data: String) = userIdContainer.save(data)

        private val users = mutableMapOf<String, UserInfoData>()

        override suspend fun userInfo(userId: String) = if (users.containsKey(userId))
            users[userId]!!
        else {
            val user = firebaseDatabaseProvider.provideDatabase()
                .child("users")
                .child(userId)
                .get()
            val userInitial = handleResult(user)
            val userInfo = UserInfoData.Base(
                userId,
                if (userInitial.name.isEmpty() || userInitial.name == "null")
                    userInitial.login
                else
                    userInitial.name,
                userInitial.photoUrl
            )
            users[userId] = userInfo
            userInfo
        }

        private suspend fun handleResult(user: Task<DataSnapshot>) =
            suspendCoroutine<UserInitial> { cont ->
                user.addOnSuccessListener { cont.resume(it.getValue(UserInitial::class.java)!!) }
                    .addOnFailureListener { cont.resumeWithException(it) }
            }
    }
}

interface ChatsDataRealtimeUpdateCallback {
    fun updateChats(chatDataList: List<ChatData>)

    object Empty : ChatsDataRealtimeUpdateCallback {
        override fun updateChats(chatDataList: List<ChatData>) = Unit
    }
}