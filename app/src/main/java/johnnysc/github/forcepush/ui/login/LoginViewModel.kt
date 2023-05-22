package johnnysc.github.forcepush.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import johnnysc.github.forcepush.domain.login.LoginInteractor
import johnnysc.github.forcepush.ui.core.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Asatryan on 27.04.2023
 */
class LoginViewModel(
    communication: LoginCommunication,
    private val interactor: LoginInteractor
) : BaseViewModel<LoginCommunication, LoginUi>(communication), HandleTask {

    val liveDataEngine = MutableLiveData<LoginEngine>()

    fun init() {
        if (interactor.authorized())
            liveDataEngine.value = LoginEngine.SignIn(this)
        else
            communication.map(LoginUi.Initial)
    }

    fun login() {
        liveDataEngine.value = LoginEngine.Login(this)
    }

    override fun handle(authResultBlock: suspend () -> Auth) {
        communication.map(LoginUi.Progress())
        viewModelScope.launch(Dispatchers.IO) {
            val result = interactor.auth(authResultBlock)
            val resultUi = if (result.isSuccessful())
                LoginUi.Success
            else
                LoginUi.Failed((result as Auth.Fail).e.message ?: "")
            withContext(Dispatchers.Main) {
                communication.map(resultUi)
            }
        }
    }
}

interface HandleTask {

    fun handle(authResultBlock: suspend () -> Auth)
}