package johnnysc.github.forcepush.data.chat

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.core.FirebaseDatabaseProvider
import johnnysc.github.forcepush.core.Read
import johnnysc.github.forcepush.domain.chat.MessagesDataRealtimeUpdateCallback
import johnnysc.github.forcepush.ui.chat.ReadMessage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * @author Asatryan on 25.08.2021
 */
interface ChatRepository : ReadMessage {

    suspend fun sendMessage(message: String): Boolean
    fun startGettingUpdates(dataCallback: MessagesDataRealtimeUpdateCallback)
    fun stopGettingUpdates()

    class Base(
        private val firebaseDatabaseProvider: FirebaseDatabaseProvider,
        private val userIdContainer: Read<String>
    ) : ChatRepository {

        private val myUid = Firebase.auth.currentUser!!.uid
        private val userId by lazy { userIdContainer.read() }
        private val chatId by lazy {
            ChatId(Pair(myUid, userId)).value()
        }

        private var callback: MessagesDataRealtimeUpdateCallback =
            MessagesDataRealtimeUpdateCallback.Empty

        private val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.children.mapNotNull { item ->
                    Pair(item.key!!, item.getValue(MessageData.Base::class.java)!!)
                }
                if (data.isNotEmpty())
                    callback.updateMessages(MessagesData.Success(data))
            }

            override fun onCancelled(error: DatabaseError) = Unit
        }

        override suspend fun sendMessage(message: String): Boolean {
            checkChatExists()
            val chat = chatReference().push()
            val result = chat.setValue(MessageData.Base(Firebase.auth.currentUser!!.uid, message))
            return handle(result)
        }

        private suspend fun handle(result: Task<Void>) = suspendCoroutine<Boolean> { cont ->
            result.addOnSuccessListener { cont.resume(true) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

        override fun startGettingUpdates(dataCallback: MessagesDataRealtimeUpdateCallback) {
            callback = dataCallback
            chatReference().addValueEventListener(eventListener)
        }

        override fun stopGettingUpdates() {
            chatReference().removeEventListener(eventListener)
            callback = MessagesDataRealtimeUpdateCallback.Empty
        }

        override fun readMessage(id: String) {
            chatReference().child(id).child("wasRead").setValue(true)
        }

        private var chatExists = false

        private suspend fun checkChatExists() {
            if (!chatExists) {
                val data = firebaseDatabaseProvider.provideDatabase().child("users")
                    .child(myUid)
                    .child("chats")
                    .get()
                val exists = handleResult(data)
                if (!exists)
                    init()
                chatExists = exists
            }
        }

        private suspend fun handleResult(data: Task<DataSnapshot>) =
            suspendCoroutine<Boolean> { cont ->
                data.addOnSuccessListener { snapshot ->
                    val exists = snapshot.children.mapNotNull { it.key }.contains(userId)
                    cont.resume(exists)
                }
            }

        private fun init() {
            firebaseDatabaseProvider.provideDatabase().child("users")
                .child(myUid)
                .child("chats")
                .child(userId)
                .setValue(true)
            firebaseDatabaseProvider.provideDatabase().child("users")
                .child(userId)
                .child("chats")
                .child(myUid)
                .setValue(true)
        }

        private fun chatReference() =
            firebaseDatabaseProvider.provideDatabase().child("chats").child(chatId)
    }
}