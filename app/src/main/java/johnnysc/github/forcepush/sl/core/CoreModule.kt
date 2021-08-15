package johnnysc.github.forcepush.sl.core

import android.content.Context
import android.content.SharedPreferences

/**
 * @author Asatryan on 15.08.2021
 **/
interface CoreModule {

    fun provideSharedPreferences(): SharedPreferences

    class Base(private val context: Context) : CoreModule {
        override fun provideSharedPreferences(): SharedPreferences =
            context.getSharedPreferences("ForceAppSharedPref", Context.MODE_PRIVATE)
    }
}