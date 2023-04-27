package johnnysc.github.forcepush.ui.groups.chat

import androidx.lifecycle.viewModelScope
import johnnysc.github.forcepush.domain.chat.MessagesDomain
import johnnysc.github.forcepush.domain.chat.MessagesDomainToUiMapper
import johnnysc.github.forcepush.domain.group.GroupChatInteractor
import johnnysc.github.forcepush.ui.chat.MessagesRealtimeUpdateCallback
import johnnysc.github.forcepush.ui.chat.MyMessageUiState
import johnnysc.github.forcepush.ui.chat.TextMapper
import johnnysc.github.forcepush.ui.core.BaseViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Asatryan on 13.09.2021
 */
class GroupChatViewModel(
    communication: GroupChatCommunication,
    private val interactor: GroupChatInteractor,
    private val mapper: MessagesDomainToUiMapper<List<GroupMessageUi>>,
    private val dispatchersIO: CoroutineDispatcher = Dispatchers.IO,
    private val dispatchersMain: CoroutineDispatcher = Dispatchers.Main,
) : BaseViewModel<GroupChatCommunication, List<GroupMessageUi>>(communication), TextMapper.Void {

    private var incomeMessages: List<GroupMessageUi> = ArrayList()
    private val myMessagesWaitList = ArrayList<GroupMessageUi>()

    /**
     * try to send one more time
     */
    override fun map(data: String) {
        myMessagesWaitList.remove(GroupMessageUi.MineRaw(data, MyMessageUiState.FAILED))
        send(data)
    }

    fun send(message: String) {
        val element = GroupMessageUi.MineRaw(message, MyMessageUiState.PROGRESS)
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
            showAllMessages()
        }
    }

    private fun showAllMessages() = viewModelScope.launch(dispatchersIO) {
        val rawList = ArrayList(incomeMessages).apply { addAll(myMessagesWaitList) }
        val result = rawList.map {
            val userInfo = interactor.userInfo(it.userId())
            it.aggregatedWith(userInfo)
        }
        withContext(dispatchersMain) { communication.map(result) }
    }

    fun startGettingUpdates() {
        interactor.startGettingUpdates(messagesRealtimeUpdateCallback)
    }

    fun stopGettingUpdates() {
        interactor.stopGettingUpdates()
    }
}