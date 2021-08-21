package johnnysc.github.forcepush.sl.core

import johnnysc.github.forcepush.sl.LoginModule
import johnnysc.github.forcepush.sl.MainModule
import johnnysc.github.forcepush.sl.MyProfileModule
import johnnysc.github.forcepush.sl.SearchModule


/**
 * @author Asatryan on 08.08.2021
 **/
interface DependencyContainer {

    fun module(feature: Feature): BaseModule<*>

    class Base(private val coreModule: CoreModule) : DependencyContainer {

        override fun module(feature: Feature) = when (feature) {
            Feature.LOGIN -> LoginModule()
            Feature.MAIN -> MainModule()
            Feature.SEARCH -> SearchModule()
            Feature.MY_PROFILE -> MyProfileModule()
            else -> throw IllegalStateException("unknown feature $feature")
        }
    }
}