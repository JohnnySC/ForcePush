package johnnysc.github.forcepush.data.chat

/**
 * @author Asatryan on 27.08.2021
 */
class ChatId {
    private val first: String
    private val second: String

    constructor(users: Pair<String, String>) {
        first = users.first
        second = users.second
    }

    constructor(value: String, firstUserId: String) {
        first = firstUserId
        val parts = value.split("_")
        second = if (parts[0] == firstUserId) parts[1] else parts[0]
    }

    fun otherUserId() = second

    fun value() = if (first > second)
        "${first}_$second"
    else
        "${second}_$first"
}