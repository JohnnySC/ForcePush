package johnnysc.github.forcepush.ui.chat

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import johnnysc.github.forcepush.domain.chat.ChatInteractor
import johnnysc.github.forcepush.domain.chat.MessageDomain
import johnnysc.github.forcepush.domain.chat.MessagesDomain
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Test

/**
 * Test for [ChatViewModel]
 *
 * @author Asatryan on 31.08.2021
 */
class ChatViewModelTest {

    @ExperimentalCoroutinesApi
    @Test
    fun test_send_success() = runBlocking {
        val communication = TestCommunication()
        val interactor = TestInteractor(true)
        val dispatchers = TestCoroutineDispatcher()
        val viewModel = ChatViewModel(
            communication,
            interactor,
            BaseMessagesDomainToUiMapper(MessageDomainToUiMapper.Base()),
            dispatchers,
            dispatchers
        )

        viewModel.startGettingUpdates()

        viewModel.send("hello")
        assertEquals(MessageUi.Mine("hello", MyMessageUiState.SENT), communication.list[0])
        assertEquals(1, communication.list.size)
        viewModel.stopGettingUpdates()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_send_failed() = runBlocking {
        val communication = TestCommunication()
        val interactor = TestInteractor(false)
        val dispatchers = TestCoroutineDispatcher()
        val viewModel = ChatViewModel(
            communication,
            interactor,
            BaseMessagesDomainToUiMapper(MessageDomainToUiMapper.Base()),
            dispatchers,
            dispatchers
        )

        viewModel.startGettingUpdates()

        viewModel.send("hello")
        assertEquals(MessageUi.Mine("hello", MyMessageUiState.FAILED), communication.list[0])
        assertEquals(1, communication.list.size)
        viewModel.stopGettingUpdates()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_retry_success() = runBlocking {
        val communication = TestCommunication()
        val interactor = TestInteractor(false)
        val dispatchers = TestCoroutineDispatcher()
        val viewModel = ChatViewModel(
            communication,
            interactor,
            BaseMessagesDomainToUiMapper(MessageDomainToUiMapper.Base()),
            dispatchers,
            dispatchers
        )

        viewModel.startGettingUpdates()

        viewModel.send("hello")
        assertEquals(MessageUi.Mine("hello", MyMessageUiState.FAILED), communication.list[0])
        assertEquals(1, communication.list.size)

        interactor.success = true
        viewModel.map("hello")
        assertEquals(MessageUi.Mine("hello", MyMessageUiState.SENT), communication.list[0])
        assertEquals(1, communication.list.size)
        viewModel.stopGettingUpdates()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_retry_failed() = runBlocking {
        val communication = TestCommunication()
        val interactor = TestInteractor(false)
        val dispatchers = TestCoroutineDispatcher()
        val viewModel = ChatViewModel(
            communication,
            interactor,
            BaseMessagesDomainToUiMapper(MessageDomainToUiMapper.Base()),
            dispatchers,
            dispatchers
        )

        viewModel.startGettingUpdates()

        viewModel.send("hello")
        assertEquals(MessageUi.Mine("hello", MyMessageUiState.FAILED), communication.list[0])
        assertEquals(1, communication.list.size)

        viewModel.map("hello")
        assertEquals(MessageUi.Mine("hello", MyMessageUiState.FAILED), communication.list[0])
        assertEquals(1, communication.list.size)

        viewModel.stopGettingUpdates()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test_retry_2_failed_1_success() = runBlocking {
        val communication = TestCommunication()
        val interactor = TestInteractor(false)
        val dispatchers = TestCoroutineDispatcher()
        val viewModel = ChatViewModel(
            communication,
            interactor,
            BaseMessagesDomainToUiMapper(MessageDomainToUiMapper.Base()),
            dispatchers,
            dispatchers
        )

        viewModel.startGettingUpdates()

        viewModel.send("hello")
        viewModel.send("how are you")
        assertEquals(MessageUi.Mine("hello", MyMessageUiState.FAILED), communication.list[0])
        assertEquals(MessageUi.Mine("how are you", MyMessageUiState.FAILED), communication.list[1])
        assertEquals(2, communication.list.size)

        interactor.success = true

        viewModel.map("hello")
        assertEquals(MessageUi.Mine("hello", MyMessageUiState.SENT), communication.list[0])
        assertEquals(MessageUi.Mine("how are you", MyMessageUiState.FAILED), communication.list[1])
        assertEquals(2, communication.list.size)

        viewModel.stopGettingUpdates()
    }

    private inner class TestCommunication : ChatCommunication {
        val list = ArrayList<MessageUi>()
        override fun observe(owner: LifecycleOwner, observer: Observer<List<MessageUi>>) = Unit

        override fun map(data: List<MessageUi>) {
            list.clear()
            list.addAll(data)
        }
    }

    private inner class TestInteractor(var success: Boolean) : ChatInteractor {

        private var callback: MessagesRealtimeUpdateCallback = MessagesRealtimeUpdateCallback.Empty

        private val list = ArrayList<MessageDomain>()
        override suspend fun send(message: String): Boolean {
            if (success) {
                list.add(MessageDomain.MyMessageDomain(message, false))
                callback.updateMessages(MessagesDomain.Success(list))
            }
            return success
        }

        override fun stopGettingUpdates() {
            callback = MessagesRealtimeUpdateCallback.Empty
        }

        override fun startGettingUpdates(callback: MessagesRealtimeUpdateCallback) {
            this.callback = callback
        }

        override fun readMessage(id: String) = Unit
    }
}