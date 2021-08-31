package johnnysc.github.forcepush.sl.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import johnnysc.github.forcepush.ui.chat.ChatViewModel
import johnnysc.github.forcepush.ui.chats.ChatsViewModel
import johnnysc.github.forcepush.ui.login.LoginViewModel
import johnnysc.github.forcepush.ui.main.MainViewModel
import johnnysc.github.forcepush.ui.profile.MyProfileViewModel
import johnnysc.github.forcepush.ui.search.SearchViewModel

/**
 * @author Asatryan on 08.08.2021
 **/
class ViewModelsFactory(
    private val dependencyContainer: DependencyContainer,
) : ViewModelProvider.Factory {

    private val map = HashMap<Class<*>, Feature>().apply {
        put(LoginViewModel::class.java, Feature.LOGIN)
        put(MainViewModel::class.java, Feature.MAIN)
        put(SearchViewModel::class.java, Feature.SEARCH)
        put(MyProfileViewModel::class.java, Feature.MY_PROFILE)
        put(ChatsViewModel::class.java, Feature.CHATS)
        put(ChatViewModel::class.java, Feature.CHAT)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val feature =
            map[modelClass] ?: throw IllegalStateException("unknown viewModel $modelClass")
        return dependencyContainer.module(feature).viewModel() as T
    }
}