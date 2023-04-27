package johnnysc.github.forcepush.sl

import johnnysc.github.forcepush.data.chat.BaseMessagesDataMapper
import johnnysc.github.forcepush.data.groups.chat.GroupChatRepository
import johnnysc.github.forcepush.data.search.GroupId
import johnnysc.github.forcepush.domain.group.GroupChatInteractor
import johnnysc.github.forcepush.sl.core.BaseModule
import johnnysc.github.forcepush.sl.core.CoreModule
import johnnysc.github.forcepush.ui.groups.chat.GroupChatCommunication
import johnnysc.github.forcepush.ui.groups.chat.GroupChatViewModel
import johnnysc.github.forcepush.ui.groups.chat.RawMessageDomainToUiMapper
import johnnysc.github.forcepush.ui.groups.chat.RawMessagesDomainToUiMapper

/**
 * @author Asatryan on 13.09.2021
 */
class GroupChatModule(private val coreModule: CoreModule) : BaseModule<GroupChatViewModel> {
    override fun viewModel() = GroupChatViewModel(
        GroupChatCommunication.Base(),
        GroupChatInteractor.Base(
            GroupChatRepository.Base(
                coreModule.firebaseDatabaseProvider(),
                GroupId(coreModule.provideSharedPreferences())
            ),
            BaseMessagesDataMapper()
        ),
        RawMessagesDomainToUiMapper(RawMessageDomainToUiMapper())
    )
}