package johnnysc.github.forcepush.core

/**
 * @author Asatryan on 18.08.2021
 **/
interface Match<T> {

    fun matches(data:T) : Boolean
}