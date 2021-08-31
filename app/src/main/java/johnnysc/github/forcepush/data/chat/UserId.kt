package johnnysc.github.forcepush.data.chat

import android.content.SharedPreferences
import johnnysc.github.forcepush.core.Read
import johnnysc.github.forcepush.core.Save

/**
 * @author Asatryan on 31.08.2021
 */
class UserId(private val sharedPreferences: SharedPreferences) : Save<String>, Read<String> {

    override fun save(data: String) {
        sharedPreferences.edit().putString(KEY, data).apply()
    }

    override fun read() = sharedPreferences.getString(KEY, "") ?: ""

    private companion object {
        const val KEY = "userIdToChatWith"
    }
}