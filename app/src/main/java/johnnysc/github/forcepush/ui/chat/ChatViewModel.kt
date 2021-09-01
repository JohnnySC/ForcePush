package johnnysc.github.forcepush.ui.chat

import androidx.lifecycle.viewModelScope
import johnnysc.github.forcepush.domain.chat.ChatInteractor
import johnnysc.github.forcepush.domain.chat.MessagesDomain
import johnnysc.github.forcepush.domain.chat.MessagesDomainToUiMapper
import johnnysc.github.forcepush.ui.core.BaseViewModel
import kotlinx.coroutines.*

/**
 * @author Asatryan on 25.08.2021
 */
class ChatViewModel(
    communication: ChatCommunication,
    private val interactor: ChatInteractor,
    private val mapper: MessagesDomainToUiMapper<List<MessageUi>>,
    private val dispatchersIO: CoroutineDispatcher = Dispatchers.IO,
    private val dispatchersMain: CoroutineDispatcher = Dispatchers.Main,
) : BaseViewModel<ChatCommunication, List<MessageUi>>(communication), TextMapper.Void, ReadMessage {

    private var incomeMessages: List<MessageUi> = ArrayList()
    private val myMessagesWaitList = ArrayList<MessageUi>()

    /**
     * try to send one more time
     */
    override fun map(data: String) {
        myMessagesWaitList.remove(MessageUi.Mine(data, MyMessageUiState.FAILED))
        send(data)
    }

    fun send(message: String) {
        val element = MessageUi.Mine(message, MyMessageUiState.PROGRESS)
        myMessagesWaitList.add(element)
        showAllMessages()

        viewModelScope.launch(dispatchersIO) {
            val success = interactor.send(message)
            if (!success) withContext(dispatchersMain) {
                val index = myMessagesWaitList.indexOf(element)
                if (index != -1)
                    myMessagesWaitList[index] = element.newState(MyMessageUiState.FAILED)
                showAllMessages()
            }
        }
    }

    private val messagesRealtimeUpdateCallback = object : MessagesRealtimeUpdateCallback {
        override fun updateMessages(messagesDomain: MessagesDomain) {
            val messages = messagesDomain.map(mapper)
            if (myMessagesWaitList.isNotEmpty()) {
                val newList = myMessagesWaitList.filter { message ->
                    messages.find { it.same(message) } == null
                }
                myMessagesWaitList.clear()
                myMessagesWaitList.addAll(newList)
            }
            incomeMessages = messages
            viewModelScope.launch(dispatchersMain) { showAllMessages() }
        }
    }

    private fun showAllMessages() = communication.map(
        if (myMessagesWaitList.isEmpty()) incomeMessages
        else ArrayList(incomeMessages).apply { addAll(myMessagesWaitList) }
    )

    fun startGettingUpdates() {
        interactor.startGettingUpdates(messagesRealtimeUpdateCallback)
    }

    fun stopGettingUpdates() {
        interactor.stopGettingUpdates()
    }

    override fun readMessage(id: String) {
        viewModelScope.launch(dispatchersIO) { interactor.readMessage(id) }
    }
}

interface MessagesRealtimeUpdateCallback {
    fun updateMessages(messagesDomain: MessagesDomain)

    object Empty : MessagesRealtimeUpdateCallback {
        override fun updateMessages(messagesDomain: MessagesDomain) = Unit
    }
}