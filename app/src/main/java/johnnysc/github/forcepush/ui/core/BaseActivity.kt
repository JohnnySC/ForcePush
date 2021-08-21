package johnnysc.github.forcepush.ui.core

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStoreOwner
import johnnysc.github.forcepush.sl.core.FPApp
import johnnysc.github.forcepush.ui.login.LoginActivity
import johnnysc.github.forcepush.ui.main.MainActivity

/**
 * @author Asatryan on 14.08.2021
 **/
abstract class BaseActivity : AppCompatActivity() {

    fun <T : ViewModel> viewModel(model: Class<T>, owner: ViewModelStoreOwner) =
        (application as FPApp).viewModel(model, owner)

    fun switchToLogin() = switchTo(LoginActivity::class.java)
    fun switchToMain() = switchTo(MainActivity::class.java)

    private fun switchTo(clasz: Class<*>) {
        startActivity(Intent(this, clasz))
        finish()
    }
}