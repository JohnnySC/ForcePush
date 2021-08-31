package johnnysc.github.forcepush.data.chat

import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.domain.chat.MessageDomain
import johnnysc.github.forcepush.domain.chat.MessagesDomain

/**
 * @author Asatryan on 25.08.2021
 */
interface MessagesDataMapper<T> : Abstract.Mapper.DataToDomain<List<MessageData>, T>

class BaseMessagesDataMapper : MessagesDataMapper<MessagesDomain> {
    override fun map(data: List<MessageData>): MessagesDomain {
        return MessagesDomain.Success(
            data.map {
                if (it.isMyMessage())
                    MessageDomain.MyMessageDomain(it.messageBody())
                else
                    MessageDomain.UserMessageDomain(it.messageBody())
            }
        )
    }

    override fun map(e: Exception): MessagesDomain {
        return MessagesDomain.Fail(e.message ?: "error")
    }
}