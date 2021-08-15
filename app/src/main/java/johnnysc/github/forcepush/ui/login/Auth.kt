package johnnysc.github.forcepush.ui.login

import johnnysc.github.forcepush.core.TextMapper

/**
 * @author Asatryan on 15.08.2021
 */
interface Auth {

    fun <T> map(mapper: TextMapper<T>): T

    data class Base(private val profile: Map<String, Any>) : Auth {
        override fun <T> map(mapper: TextMapper<T>): T = mapper.map(profile["bio"].toString())
    }

    data class Fail(private val e: Exception) : Auth {
        override fun <T> map(mapper: TextMapper<T>) = mapper.map(e.message.toString())
    }
}