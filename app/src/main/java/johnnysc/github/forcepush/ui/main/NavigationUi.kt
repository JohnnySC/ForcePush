package johnnysc.github.forcepush.ui.main

import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.core.Abstract

/**
 * @author Asatryan on 18.08.2021
 **/
interface NavigationUi : Abstract.UiObject {

    fun data(): Int//todo make better
    fun isBaseLevel(): Boolean

    class BaseLevel(private val id: Int = R.id.navigation_profile) : NavigationUi {
        override fun isBaseLevel() = true
        override fun data() = id
    }

    class SecondLevel(private val id: Int) : NavigationUi {
        override fun isBaseLevel() = false
        override fun data() = id
    }
}