package johnnysc.github.forcepush.sl

import johnnysc.github.forcepush.sl.core.BaseModule
import johnnysc.github.forcepush.sl.core.CoreModule
import johnnysc.github.forcepush.ui.main.MainViewModel

/**
 * @author Asatryan on 18.08.2021
 **/
class MainModule(private val coreModule: CoreModule) : BaseModule<MainViewModel> {
    override fun viewModel() = MainViewModel(
        coreModule.navigationCommunication()
    )
}