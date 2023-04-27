package johnnysc.github.forcepush.ui.groups.chat

import johnnysc.github.forcepush.domain.chat.MessageDomain
import johnnysc.github.forcepush.domain.chat.MessagesDomainToUiMapper
import johnnysc.github.forcepush.ui.chat.MessageDomainToUiMapper
import johnnysc.github.forcepush.ui.chat.MyMessageUiState

/**
 * @author Asatryan on 13.09.2021
 */
class RawMessagesDomainToUiMapper(private val mapper: MessageDomainToUiMapper<GroupMessageUi>) :
    MessagesDomainToUiMapper<List<GroupMessageUi>> {
    override fun map(data: List<MessageDomain>) = data.map { it.map(mapper) }
    override fun map(error: String): List<GroupMessageUi> = emptyList()
}

class RawMessageDomainToUiMapper : MessageDomainToUiMapper<GroupMessageUi> {
    override fun map(id: String, userId:String, isRead: Boolean, text: String, isMyMessage: Boolean) =
        if (isMyMessage)
            GroupMessageUi.MineRaw(text, if (isRead) MyMessageUiState.READ else MyMessageUiState.SENT)
        else
            GroupMessageUi.FromUserRaw(id, userId, text, isRead)

    override fun map(error: String) = GroupMessageUi.MineRaw(error, MyMessageUiState.FAILED)
}