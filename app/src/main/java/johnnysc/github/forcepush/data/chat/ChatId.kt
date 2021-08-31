package johnnysc.github.forcepush.data.chat

/**
 * @author Asatryan on 27.08.2021
 */
class ChatId(private val first: String, private val second: String) {

    fun value() = if (first > second)
        "${first}_$second"
    else
        "${second}_$first"
}