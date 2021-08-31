package johnnysc.github.forcepush.data.chat

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.ktx.Firebase

/**
 * @author Asatryan on 25.08.2021
 */
interface MessageData {
    fun isMyMessage(): Boolean
    fun messageBody(): String

    @IgnoreExtraProperties
    class Base(val userId: String = "", val message: String = "") : MessageData {
        override fun isMyMessage() = userId == Firebase.auth.currentUser?.uid
        override fun messageBody() = message
    }
}