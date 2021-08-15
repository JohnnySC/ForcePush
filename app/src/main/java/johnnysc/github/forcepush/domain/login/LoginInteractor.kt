package johnnysc.github.forcepush.domain.login

import johnnysc.github.forcepush.core.SaveText
import johnnysc.github.forcepush.data.LoginRepository
import johnnysc.github.forcepush.ui.login.Auth
import johnnysc.github.forcepush.ui.login.LoginWrapper

/**
 * @author Asatryan on 14.08.2021
 **/
interface LoginInteractor {

    fun authorized(): Boolean

    suspend fun login(loginWrapper: LoginWrapper): Auth

    class Base(private val repository: LoginRepository) : LoginInteractor {

        override suspend fun login(loginWrapper: LoginWrapper): Auth {
            val result = loginWrapper.login()
            if (result is Auth.Base)
                result.map(SaveText(repository))
            return result
        }

        override fun authorized() = repository.user() != null
    }
}