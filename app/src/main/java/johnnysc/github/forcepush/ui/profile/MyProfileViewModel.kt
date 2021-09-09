package johnnysc.github.forcepush.ui.profile

import androidx.lifecycle.viewModelScope
import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.data.profile.MyProfileData
import johnnysc.github.forcepush.data.profile.MyProfileRepository
import johnnysc.github.forcepush.ui.core.BaseViewModel
import johnnysc.github.forcepush.ui.main.NavigationCommunication
import johnnysc.github.forcepush.ui.main.NavigationUi
import kotlinx.coroutines.launch

/**
 * @author Asatryan on 18.08.2021
 **/
class MyProfileViewModel(
    communication: MyProfileCommunication,
    private val navigation: NavigationCommunication,
    private val repository: MyProfileRepository,
    private val mapper: MyProfileData.MyProfileMapper<MyProfileUi>,
) : BaseViewModel<MyProfileCommunication, MyProfileUi>(
    communication
) {

    fun init() = viewModelScope.launch {
        val data = repository.profile().map(mapper)
        communication.map(data)
    }

    fun signOut() = repository.signOut()
    fun createGroup() = navigation.map(NavigationUi.SecondLevel(R.id.create_group_screen))
}