package johnnysc.github.forcepush.sl

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
    override fun viewModel(): ChatsViewModel {
        return ChatsViewModel(ChatsCommunication.Base())
    }
}