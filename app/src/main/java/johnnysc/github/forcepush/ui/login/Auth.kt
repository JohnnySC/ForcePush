package johnnysc.github.forcepush.ui.login

import johnnysc.github.forcepush.data.login.LoginRepository
import johnnysc.github.forcepush.data.login.UserInitial

/**
 * @author Asatryan on 15.08.2021
 */
interface Auth {

    fun isSuccessful(): Boolean = false
    suspend fun save(repository: LoginRepository) = Unit

    object Empty : Auth

    data class Base(
        private val profile: Map<String, Any>,
        private val mapper: AuthResultMapper<UserInitial> = AuthResultMapper.Base()
    ) : Auth {

        override suspend fun save(repository: LoginRepository) {
            repository.saveUser(mapper.map(profile))
        }

        override fun isSuccessful() = true
    }

    data class Fail(val e: Exception) : Auth

    interface AuthResultMapper<T> {
        fun map(profile: Map<String, Any>): T

        class Base : AuthResultMapper<UserInitial> {

            override fun map(profile: Map<String, Any>) = UserInitial(
                profile["name"].makeString(),
                profile["login"].makeString().lowercase(),
                profile["email"].makeString(),
                profile["bio"].makeString(),
                profile["avatar_url"].makeString()
            )
        }
    }
}

fun Any?.makeString(): String {
    val toString = toString()
    return if (toString == "null") "" else toString
}