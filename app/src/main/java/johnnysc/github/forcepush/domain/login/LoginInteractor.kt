package johnnysc.github.forcepush.domain.login

import johnnysc.github.forcepush.data.login.LoginRepository
import johnnysc.github.forcepush.data.login.UserInitial
import johnnysc.github.forcepush.ui.login.Auth
import johnnysc.github.forcepush.ui.login.LoginEngine

/**
 * @author Asatryan on 14.08.2021
 **/
interface LoginInteractor {

    fun authorized(): Boolean

    suspend fun login(loginEngine: LoginEngine): Auth
    suspend fun signIn(signIn: LoginEngine): Auth

    class Base(
        private val repository: LoginRepository,
        private val mapper: Auth.AuthResultMapper<UserInitial>,
    ) : LoginInteractor {

        override suspend fun login(loginEngine: LoginEngine): Auth = try {
            val result = loginEngine.login()
            repository.saveUser(result.map(mapper))
            result
        } catch (e: Exception) {
            Auth.Fail(e)
        }

        override suspend fun signIn(signIn: LoginEngine): Auth = try {
            signIn.login()
        } catch (e: Exception) {
            Auth.Fail(e)
        }

        override fun authorized() = repository.user() != null
    }
}