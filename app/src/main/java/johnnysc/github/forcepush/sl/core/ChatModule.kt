package johnnysc.github.forcepush.sl.core

import johnnysc.github.forcepush.data.chat.BaseMessagesDataMapper
import johnnysc.github.forcepush.data.chat.ChatRepository
import johnnysc.github.forcepush.data.chat.UserId
import johnnysc.github.forcepush.domain.chat.ChatInteractor
import johnnysc.github.forcepush.ui.chat.BaseMessagesDomainToUiMapper
import johnnysc.github.forcepush.ui.chat.ChatCommunication
import johnnysc.github.forcepush.ui.chat.ChatViewModel
import johnnysc.github.forcepush.ui.chat.MessageDomainToUiMapper

/**
 * @author Asatryan on 25.08.2021
 */
class ChatModule(private val coreModule: CoreModule) : BaseModule<ChatViewModel> {
    override fun viewModel(): ChatViewModel {
        return ChatViewModel(
            ChatCommunication.Base(),
            ChatInteractor.Base(
                ChatRepository.Base(
                    coreModule.firebaseDatabaseProvider(),
                    UserId(coreModule.provideSharedPreferences())
                ),
                BaseMessagesDataMapper()
            ),
            BaseMessagesDomainToUiMapper(MessageDomainToUiMapper.Base())
        )
    }
}