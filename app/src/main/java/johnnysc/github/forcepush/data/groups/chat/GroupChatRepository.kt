package johnnysc.github.forcepush.data.groups.chat

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.core.Delay
import johnnysc.github.forcepush.core.FirebaseDatabaseProvider
import johnnysc.github.forcepush.core.Read
import johnnysc.github.forcepush.data.chat.MessageData
import johnnysc.github.forcepush.data.chat.MessagesData
import johnnysc.github.forcepush.data.login.UserInitial
import johnnysc.github.forcepush.domain.chat.MessagesDataRealtimeUpdateCallback
import johnnysc.github.forcepush.domain.group.GroupChatUserInfo
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 13.09.2021
 */
interface GroupChatRepository {
    suspend fun sendMessage(message: String): Boolean
    fun stopGettingUpdates()
    fun startGettingUpdates(callback: MessagesDataRealtimeUpdateCallback)
    suspend fun userInfo(id: String): GroupChatUserInfo

    class Base(
        private val firebaseDatabaseProvider: FirebaseDatabaseProvider,
        groupIdContainer: Read<String>
    ) : GroupChatRepository {

        private val groupId = groupIdContainer.read()

        private val chatRef = firebaseDatabaseProvider.provideDatabase()
            .child("group-messages")
            .child(groupId)

        private var callback: MessagesDataRealtimeUpdateCallback =
            MessagesDataRealtimeUpdateCallback.Empty

        private val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.mapNotNull { item ->
                    Pair(
                        item.key!!,
                        item.getValue(MessageData.Base::class.java)!!
                    )//todo maybe not base
                }
                if (data.isNotEmpty())
                    delay.add(MessagesData.Success(data))
            }

            override fun onCancelled(error: DatabaseError) = Unit
        }

        private val delay = Delay<MessagesData> { callback.updateMessages(it) }

        override suspend fun sendMessage(message: String): Boolean {
            val chat = chatRef.push()
            val result = chat.setValue(MessageData.Base(Firebase.auth.currentUser!!.uid, message))
            return handle(result)
        }

        private suspend fun handle(result: Task<Void>) = suspendCoroutine<Boolean> { cont ->
            result.addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

        override fun stopGettingUpdates() {
            delay.clear()
            callback = MessagesDataRealtimeUpdateCallback.Empty
            chatRef.removeEventListener(listener)
        }

        override fun startGettingUpdates(callback: MessagesDataRealtimeUpdateCallback) {
            this.callback = callback
            chatRef.addValueEventListener(listener)
        }

        override suspend fun userInfo(id: String): GroupChatUserInfo {
            if (userInfoMap.containsKey(id))
                return userInfoMap[id]!!

            val user = firebaseDatabaseProvider.provideDatabase()
                .child("users")
                .child(id)
                .get()
            val userInitial = handleResult(user)
            val groupChatUserInfo = GroupChatUserInfo(
                if (userInitial.name.isEmpty()) userInitial.login else userInitial.name,
                userInitial.photoUrl
            )
            userInfoMap[id] = groupChatUserInfo
            return groupChatUserInfo
        }

        private val userInfoMap = mutableMapOf<String, GroupChatUserInfo>()

        private suspend fun handleResult(user: Task<DataSnapshot>) =
            suspendCoroutine<UserInitial> { cont ->
                user.addOnSuccessListener { cont.resume(it.getValue(UserInitial::class.java)!!) }
                    .addOnFailureListener { cont.resumeWithException(it) }
            }
    }
}