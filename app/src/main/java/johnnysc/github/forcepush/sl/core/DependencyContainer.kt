package johnnysc.github.forcepush.sl.core

import johnnysc.github.forcepush.sl.login.LoginModule


/**
 * @author Asatryan on 08.08.2021
 **/
interface DependencyContainer {

    fun module(feature: Feature): BaseModule<*>

    class Base(private val coreModule: CoreModule) : DependencyContainer {

        override fun module(feature: Feature) = when (feature) {
            Feature.LOGIN -> LoginModule(coreModule)
            else -> throw IllegalStateException("unknown feature $feature")
        }
    }
}