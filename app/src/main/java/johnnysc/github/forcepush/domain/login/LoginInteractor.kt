package johnnysc.github.forcepush.domain.login

import johnnysc.github.forcepush.data.login.LoginRepository
import johnnysc.github.forcepush.ui.login.Auth

/**
 * @author Asatryan on 14.08.2021
 **/
interface LoginInteractor {

    fun authorized(): Boolean
    suspend fun auth(block: suspend () -> Auth): Auth

    class Base(
        private val repository: LoginRepository
    ) : LoginInteractor {

        override suspend fun auth(block: suspend () -> Auth): Auth {
            return try {
                val result = block.invoke()
                result.save(repository)
                result
            } catch (e: Exception) {
                Auth.Fail(e)
            }
        }

        override fun authorized() = repository.user() != null
    }
}