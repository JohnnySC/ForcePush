package johnnysc.github.forcepush.data.chat

import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.ktx.Firebase

/**
 * @author Asatryan on 25.08.2021
 */
interface MessageData {
    fun messageIsMine(): Boolean
    fun messageBody(): String
    fun wasReadByUser(): Boolean

    @IgnoreExtraProperties
    data class Base(
        val userId: String = "",
        val message: String = "",
        val wasRead: Boolean = false
    ) : MessageData {
        override fun messageIsMine() = userId == Firebase.auth.currentUser?.uid
        override fun messageBody() = message
        override fun wasReadByUser() = wasRead
    }
}