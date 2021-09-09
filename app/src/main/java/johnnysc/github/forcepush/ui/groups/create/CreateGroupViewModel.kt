package johnnysc.github.forcepush.ui.groups.create

import androidx.lifecycle.viewModelScope
import johnnysc.github.forcepush.data.groups.create.CreateGroupRepository
import johnnysc.github.forcepush.ui.core.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Asatryan on 08.09.2021
 */
class CreateGroupViewModel(
    communication: CreateGroupCommunication,
    private val repository: CreateGroupRepository
) : BaseViewModel<CreateGroupCommunication, CreateGroupUi>(communication) {

    fun createGroup(name: String) {
        if (name.trim().isEmpty()) return
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.createGroup(name.trim()))
                init()
            //todo else handle fail globally
        }
    }

    fun init() {
        viewModelScope.launch(Dispatchers.IO) {
            val groups = repository.groups()
            withContext(Dispatchers.Main) { communication.map(CreateGroupUi.Base(groups)) }
        }
    }
}