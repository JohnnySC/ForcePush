package johnnysc.github.forcepush.ui.profile

import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.ui.core.AbstractView

/**
 * @author Asatryan on 18.08.2021
 **/
interface MyProfileUi : Abstract.UiObject {

    fun map(name: AbstractView.Text, login: AbstractView.Text, avatar: AbstractView.Image)

    class Base(
        private val userName: String,
        private val userLogin: String,
        private val photoUrl: String,
    ) : MyProfileUi {

        override fun map(
            name: AbstractView.Text,
            login: AbstractView.Text,
            avatar: AbstractView.Image,
        ) {
            name.show(userName)
            login.show(userLogin)
            avatar.load(photoUrl)
        }
    }
}