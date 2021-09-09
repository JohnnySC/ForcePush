package johnnysc.github.forcepush.data.search

import android.content.SharedPreferences
import johnnysc.github.forcepush.core.Read
import johnnysc.github.forcepush.core.Save

/**
 * @author Asatryan on 07.09.2021
 */
class GroupId(private val sharedPreferences: SharedPreferences) : Save<String>, Read<String> {

    override fun save(data: String) {
        sharedPreferences.edit().putString(KEY, data).apply()
    }

    override fun read() = sharedPreferences.getString(KEY, "") ?: ""

    private companion object {
        const val KEY = "groupIdToStartChat"
    }
}