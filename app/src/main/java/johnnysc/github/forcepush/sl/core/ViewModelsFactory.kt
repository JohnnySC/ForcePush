package johnnysc.github.forcepush.sl.core

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import johnnysc.github.forcepush.ui.login.LoginViewModel

/**
 * @author Asatryan on 08.08.2021
 **/
class ViewModelsFactory(
    private val dependencyContainer: DependencyContainer,
) : ViewModelProvider.Factory {

    private val map = HashMap<Class<*>, Feature>().apply {
        put(LoginViewModel::class.java, Feature.LOGIN)
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val feature =
            map[modelClass] ?: throw IllegalStateException("unknown viewModel $modelClass")
        return dependencyContainer.module(feature).viewModel() as T
    }
}