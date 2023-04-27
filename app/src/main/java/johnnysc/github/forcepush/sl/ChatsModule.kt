package johnnysc.github.forcepush.sl

import johnnysc.github.forcepush.data.chat.UserId
import johnnysc.github.forcepush.data.chats.ChatDataMapper
import johnnysc.github.forcepush.data.chats.ChatsRepository
import johnnysc.github.forcepush.data.chats.UserChatDataMapper
import johnnysc.github.forcepush.data.search.GroupId
import johnnysc.github.forcepush.domain.chats.ChatDomainMapper
import johnnysc.github.forcepush.domain.chats.ChatsInteractor
import johnnysc.github.forcepush.domain.chats.UserChatDomainMapper
import johnnysc.github.forcepush.sl.core.BaseModule
import johnnysc.github.forcepush.sl.core.CoreModule
import johnnysc.github.forcepush.ui.chats.ChatsCommunication
import johnnysc.github.forcepush.ui.chats.ChatsViewModel

/**
 * @author Asatryan on 25.08.2021
 */
class ChatsModule(
    private val coreModule: CoreModule
) : BaseModule<ChatsViewModel> {
    override fun viewModel() = ChatsViewModel(
        ChatsCommunication.Base(),
        coreModule.navigationCommunication(),
        ChatsInteractor.Base(
            ChatsRepository.Base(
                coreModule.firebaseDatabaseProvider(),
                UserId(coreModule.provideSharedPreferences()),
                GroupId(coreModule.provideSharedPreferences())
            ),
            ChatDataMapper.Base(),
            UserChatDataMapper.Base()
        ),
        ChatDomainMapper.Base(),
        UserChatDomainMapper.Base()
    )
}