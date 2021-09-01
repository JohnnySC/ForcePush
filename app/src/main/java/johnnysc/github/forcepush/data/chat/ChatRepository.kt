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

        private val chatId by lazy {
            ChatId(Firebase.auth.currentUser!!.uid, userIdContainer.read()).value()
        }

        private var callback: MessagesDataRealtimeUpdateCallback =
            MessagesDataRealtimeUpdateCallback.Empty

        private val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data =
                    snapshot.children.mapNotNull { item -> Pair(item.key!!, item.getValue(MessageData.Base::class.java)!!) }
                if (data.isNotEmpty())
                    callback.updateMessages(MessagesData.Success(data))
            }

            override fun onCancelled(error: DatabaseError) = Unit
        }

        override suspend fun sendMessage(message: String): Boolean {
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

        private fun chatReference() =
            firebaseDatabaseProvider.provideDatabase().child("chats").child(chatId)
    }
}