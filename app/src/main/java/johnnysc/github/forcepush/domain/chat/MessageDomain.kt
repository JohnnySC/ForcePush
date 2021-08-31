package johnnysc.github.forcepush.domain.chat

import johnnysc.github.forcepush.ui.chat.MessageDomainToUiMapper

/**
 * @author Asatryan on 25.08.2021
 */
interface MessageDomain {

    fun <T> map(mapper: MessageDomainToUiMapper<T>): T

    data class MyMessageDomain(private val text: String) : MessageDomain {
        override fun <T> map(mapper: MessageDomainToUiMapper<T>): T {
            return mapper.map(text, true)
        }
    }

    data class UserMessageDomain(private val text: String) : MessageDomain {
        override fun <T> map(mapper: MessageDomainToUiMapper<T>): T {
            return mapper.map(text, false)
        }
    }
}