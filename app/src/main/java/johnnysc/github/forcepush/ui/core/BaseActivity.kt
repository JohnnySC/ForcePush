package johnnysc.github.forcepush.ui.core

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import johnnysc.github.forcepush.sl.core.FPApp

/**
 * @author Asatryan on 14.08.2021
 **/
abstract class BaseActivity : AppCompatActivity() {

    protected fun <T : ViewModel> viewModel(model: Class<T>, owner: ViewModelStoreOwner) =
        (application as FPApp).viewModel(model, owner)
}