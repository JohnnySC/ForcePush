package johnnysc.github.forcepush.ui.login

import androidx.lifecycle.viewModelScope
import johnnysc.github.forcepush.core.TextMapper
import johnnysc.github.forcepush.domain.login.LoginInteractor
import johnnysc.github.forcepush.ui.core.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Asatryan on 14.08.2021
 **/
class LoginViewModel(
    communication: LoginCommunication,
    private val interactor: LoginInteractor,
    private val dispatchersIO: CoroutineDispatcher = Dispatchers.IO,
    private val dispatchersMain: CoroutineDispatcher = Dispatchers.Main,
) : BaseViewModel<LoginCommunication, LoginUi>(communication) {

    fun login(login: LoginWrapper) {
        communication.map(LoginUi.Progress())
        viewModelScope.launch(dispatchersIO) {
            val result = interactor.login(login)
            val resultUi = if (result is Auth.Fail)
                result.map(LoginUiFailed())
            else
                LoginUi.Success
            withContext(dispatchersMain) { communication.map(resultUi) }
        }
    }

    fun init() {
        val initialState = if (interactor.authorized()) LoginUi.Success else LoginUi.Initial()
        communication.map(initialState)
    }

    private inner class LoginUiFailed : TextMapper<LoginUi.Failed> {
        override fun map(data: String) = LoginUi.Failed(data)
    }
}
