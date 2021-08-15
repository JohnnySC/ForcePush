package johnnysc.github.forcepush.sl.core

import androidx.lifecycle.ViewModel

/**
 * @author Asatryan on 08.08.2021
 **/
interface BaseModule<T : ViewModel> {

    fun viewModel(): T
}