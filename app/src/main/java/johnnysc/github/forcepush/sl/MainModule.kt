package johnnysc.github.forcepush.sl

import johnnysc.github.forcepush.sl.core.BaseModule
import johnnysc.github.forcepush.ui.main.MainViewModel
import johnnysc.github.forcepush.ui.main.NavigationCommunication

/**
 * @author Asatryan on 18.08.2021
 **/
class MainModule : BaseModule<MainViewModel> {
    override fun viewModel() = MainViewModel(
        NavigationCommunication.Base()
    )
}