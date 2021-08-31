package johnnysc.github.forcepush.sl.core

import android.content.Context
import android.content.SharedPreferences
import johnnysc.github.forcepush.core.FirebaseDatabaseProvider
import johnnysc.github.forcepush.ui.main.NavigationCommunication

/**
 * @author Asatryan on 15.08.2021
 **/
interface CoreModule {

    fun provideSharedPreferences(): SharedPreferences
    fun firebaseDatabaseProvider(): FirebaseDatabaseProvider
    fun navigationCommunication() : NavigationCommunication

    class Base(private val context: Context) : CoreModule {
        private val firebaseDatabaseProvider = FirebaseDatabaseProvider.Base()
        private val navigationCommunication = NavigationCommunication.Base()

        override fun provideSharedPreferences(): SharedPreferences =
            context.getSharedPreferences("ForceAppSharedPref", Context.MODE_PRIVATE)

        override fun firebaseDatabaseProvider() = firebaseDatabaseProvider

        override fun navigationCommunication() = navigationCommunication
    }
}