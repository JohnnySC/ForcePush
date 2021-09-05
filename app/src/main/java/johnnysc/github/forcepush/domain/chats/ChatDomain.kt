package johnnysc.github.forcepush.domain.chats

import johnnysc.github.forcepush.ui.chats.ChatUi

/**
 * @author Asatryan on 02.09.2021
 */
interface ChatDomain {

    fun <T> map(mapper: ChatDomainMapper<T>): T

    data class LastMessageFromMe(
        private val otherUserId: String,
        private val lastMessageBody: String,
        private val notReadMessagesCount: Int
    ) : ChatDomain {
        override fun <T> map(mapper: ChatDomainMapper<T>) =
            mapper.map(otherUserId, lastMessageBody, notReadMessagesCount, false)
    }

    data class LastMessageFromUser(
        private val otherUserId: String,
        private val lastMessageBody: String,
        private val notReadMessagesCount: Int
    ) : ChatDomain {
        override fun <T> map(mapper: ChatDomainMapper<T>) =
            mapper.map(otherUserId, lastMessageBody, notReadMessagesCount, true)
    }
}

interface ChatDomainMapper<T> {
    fun map(
        otherUserId: String,
        lastMessageBody: String,
        notReadMessagesCount: Int,
        lastMessageFromUser: Boolean
    ): T

    class Base : ChatDomainMapper<ChatUi> {
        override fun map(
            otherUserId: String,
            lastMessageBody: String,
            notReadMessagesCount: Int,
            lastMessageFromUser: Boolean
        ) = ChatUi.Raw(otherUserId, lastMessageBody, notReadMessagesCount, lastMessageFromUser)
    }
}