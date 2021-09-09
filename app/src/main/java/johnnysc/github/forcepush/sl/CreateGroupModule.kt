package johnnysc.github.forcepush.sl

import johnnysc.github.forcepush.data.groups.create.CreateGroupRepository
import johnnysc.github.forcepush.sl.core.BaseModule
import johnnysc.github.forcepush.sl.core.CoreModule
import johnnysc.github.forcepush.ui.groups.create.CreateGroupCommunication
import johnnysc.github.forcepush.ui.groups.create.CreateGroupViewModel

/**
 * @author Asatryan on 08.09.2021
 */
class CreateGroupModule(private val coreModule: CoreModule) : BaseModule<CreateGroupViewModel> {
    override fun viewModel() = CreateGroupViewModel(
        CreateGroupCommunication.Base(),
        CreateGroupRepository.Base(coreModule.firebaseDatabaseProvider())
    )
}