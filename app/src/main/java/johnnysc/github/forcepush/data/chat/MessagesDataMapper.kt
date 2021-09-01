package johnnysc.github.forcepush.data.chat

import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.domain.chat.MessageDomain
import johnnysc.github.forcepush.domain.chat.MessagesDomain

/**
 * @author Asatryan on 25.08.2021
 */
interface MessagesDataMapper<T> : Abstract.Mapper.DataToDomain<List<Pair<String, MessageData>>, T>

class BaseMessagesDataMapper : MessagesDataMapper<MessagesDomain> {
    override fun map(data: List<Pair<String, MessageData>>) =
        MessagesDomain.Success(data.map { (id, data) ->
            if (data.messageIsMine())
                MessageDomain.MyMessageDomain(data.messageBody(), data.wasReadByUser())
            else
                MessageDomain.UserMessageDomain(id, data.messageBody(), data.wasReadByUser())
        })

    override fun map(e: Exception): MessagesDomain {
        return MessagesDomain.Fail(e.message ?: "error")
    }
}