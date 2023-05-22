package johnnysc.github.forcepush.ui.login

import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.ui.core.AbstractView
import johnnysc.github.forcepush.ui.core.Navigate
import johnnysc.github.forcepush.ui.main.MainActivity

/**
 * @author Asatryan on 14.08.2021
 **/
interface LoginUi : Abstract.UiObject {

    fun map(error: AbstractView.Text, progress: AbstractView, button: AbstractView) = Unit
    fun navigate(navigate: Navigate) = Unit

    object Success : LoginUi {
        override fun navigate(navigate: Navigate) = navigate.switchTo(MainActivity::class.java)
    }

    object Initial : LoginUi {
        override fun map(error: AbstractView.Text, progress: AbstractView, button: AbstractView) {
            error.hide()
            progress.hide()
            button.show()
        }
    }

    class Progress : LoginUi {
        override fun map(error: AbstractView.Text, progress: AbstractView, button: AbstractView) {
            error.hide()
            progress.show()
            button.hide()
        }
    }

    data class Failed(private val message: String) : LoginUi {
        override fun map(error: AbstractView.Text, progress: AbstractView, button: AbstractView) {
            error.map(message)
            progress.hide()
            button.show()
        }
    }
}