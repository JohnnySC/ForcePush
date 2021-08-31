package johnnysc.github.forcepush.data.chat

/**
 * @author Asatryan on 25.08.2021
 */
sealed class MessagesData {

    abstract fun <T> map(mapper: MessagesDataMapper<T>): T

    data class Success(private val messages: List<MessageData>) : MessagesData() {
        override fun <T> map(mapper: MessagesDataMapper<T>): T = mapper.map(messages)
    }

    data class Fail(private val e: Exception) : MessagesData() {
        override fun <T> map(mapper: MessagesDataMapper<T>) = mapper.map(e)
    }
}