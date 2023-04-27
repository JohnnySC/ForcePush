package johnnysc.github.forcepush.domain.group

import johnnysc.github.forcepush.data.chat.MessagesData
import johnnysc.github.forcepush.data.chat.MessagesDataMapper
import johnnysc.github.forcepush.data.groups.chat.GroupChatRepository
import johnnysc.github.forcepush.domain.chat.MessagesDataRealtimeUpdateCallback
import johnnysc.github.forcepush.domain.chat.MessagesDomain
import johnnysc.github.forcepush.ui.chat.MessagesRealtimeUpdateCallback

/**
 * @author Asatryan on 13.09.2021
 */
interface GroupChatInteractor {
    suspend fun send(message: String): Boolean
    fun stopGettingUpdates()
    fun startGettingUpdates(callback: MessagesRealtimeUpdateCallback)
    suspend fun userInfo(id: String): GroupChatUserInfo

    class Base(
        private val repository: GroupChatRepository,
        private val mapper: MessagesDataMapper<MessagesDomain>
    ) : GroupChatInteractor {

        private var callback: MessagesRealtimeUpdateCallback = MessagesRealtimeUpdateCallback.Empty

        private val dataCallback = object : MessagesDataRealtimeUpdateCallback {
            override fun updateMessages(messagesData: MessagesData) =
                callback.updateMessages(messagesData.map(mapper))
        }

        override suspend fun send(message: String) = try {
            repository.sendMessage(message)
        } catch (e: Exception) {
            false
        }

        override fun stopGettingUpdates() {
            callback = MessagesRealtimeUpdateCallback.Empty
            repository.stopGettingUpdates()
        }

        override fun startGettingUpdates(callback: MessagesRealtimeUpdateCallback) {
            this.callback = callback
            repository.startGettingUpdates(dataCallback)
        }

        override suspend fun userInfo(id: String) = repository.userInfo(id)
    }
}

data class GroupChatUserInfo(val name: String, val avatarUrl: String)