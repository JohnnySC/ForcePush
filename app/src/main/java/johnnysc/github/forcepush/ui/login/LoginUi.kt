package johnnysc.github.forcepush.ui.login

import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.ui.core.AbstractView

/**
 * @author Asatryan on 14.08.2021
 **/
interface LoginUi : Abstract.UiObject {

    fun map(error: AbstractView.Text, progress: AbstractView, button: AbstractView) = Unit

    object Success : LoginUi

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
            error.show(message)
            progress.hide()
            button.show()
        }
    }
}