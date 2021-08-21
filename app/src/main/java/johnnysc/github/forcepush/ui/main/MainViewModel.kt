package johnnysc.github.forcepush.ui.main

import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.ui.core.BaseViewModel
import johnnysc.github.forcepush.ui.profile.MyProfileFragment
import johnnysc.github.forcepush.ui.search.SearchFragment

/**
 * @author Asatryan on 18.08.2021
 **/
class MainViewModel(
    communication: NavigationCommunication,
) : BaseViewModel<NavigationCommunication, NavigationUi>(communication) {

    fun changeScreen(menuItemId: Int) {
        //todo save screen id 
        communication.map(NavigationUi(menuItemId))
    }

    private val idMap = mapOf(
        R.id.navigation_profile to MyProfileFragment::class.java,
        R.id.navigation_search to SearchFragment::class.java
    )//todo move to some class

    fun getFragment(id: Int): BaseFragment<*> {
        val clazz = idMap[id] ?: throw IllegalStateException("unknown id $id")
        return clazz.newInstance()
    }

    fun init() {
        changeScreen(R.id.navigation_profile) //todo get from navigation cache
    }
}