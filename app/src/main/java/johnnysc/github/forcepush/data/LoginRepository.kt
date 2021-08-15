package johnnysc.github.forcepush.data

import android.content.SharedPreferences
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.core.Save

/**
 * @author Asatryan on 15.08.2021
 **/
interface LoginRepository : Save<String> {

    fun user(): Any?

    class Base(private val sharedPreferences: SharedPreferences) : LoginRepository {
        override fun user() = Firebase.auth.currentUser
        override fun save(data: String) {
            sharedPreferences.edit().putString("profile", data).apply()
        }
    }
}