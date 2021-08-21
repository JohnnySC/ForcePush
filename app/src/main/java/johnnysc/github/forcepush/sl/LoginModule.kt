package johnnysc.github.forcepush.sl

import johnnysc.github.forcepush.data.login.LoginRepository
import johnnysc.github.forcepush.domain.login.LoginInteractor
import johnnysc.github.forcepush.sl.core.BaseModule
import johnnysc.github.forcepush.ui.login.Auth
import johnnysc.github.forcepush.ui.login.LoginCommunication
import johnnysc.github.forcepush.ui.login.LoginViewModel

/**
 * @author Asatryan on 14.08.2021
 **/
class LoginModule : BaseModule<LoginViewModel> {

    override fun viewModel() = LoginViewModel(
        LoginCommunication.Base(),
        LoginInteractor.Base(
            LoginRepository.Base(),
            Auth.AuthResultMapper.Base()
        )
    )
}