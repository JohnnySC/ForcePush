package johnnysc.github.forcepush.domain.chat

import johnnysc.github.forcepush.ui.chat.MessageDomainToUiMapper

/**
 * @author Asatryan on 25.08.2021
 */
interface MessageDomain {

    fun <T> map(mapper: MessageDomainToUiMapper<T>): T

    data class MyMessageDomain(private val text: String, private val isRead: Boolean) :
        MessageDomain {
        override fun <T> map(mapper: MessageDomainToUiMapper<T>): T {
            return mapper.map("", isRead, text, true)
        }
    }

    data class UserMessageDomain(
        private val id: String,
        private val text: String,
        private val isRead: Boolean
    ) : MessageDomain {
        override fun <T> map(mapper: MessageDomainToUiMapper<T>): T {
            return mapper.map(id, isRead, text, false)
        }
    }
}