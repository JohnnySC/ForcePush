package johnnysc.github.forcepush.ui.chat

import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.ui.core.AbstractView
import johnnysc.github.forcepush.ui.core.ClickListener

/**
 * @author Asatryan on 25.08.2021
 */
interface MessageUi {

    fun isMyMessage(): Boolean

    fun map(textMapper: TextMapper.Void)
    fun map(
        textView: AbstractView.Text,
        progressView: AbstractView,
        stateView: MessageState
    )

    fun newState(state: MyMessageUiState): MessageUi
    fun same(messageUi: MessageUi): Boolean
    fun click(clickListener: ClickListener<MessageUi>)

    data class Mine(
        private val text: String,
        private val state: MyMessageUiState
    ) : MessageUi {
        override fun isMyMessage() = true
        override fun map(textMapper: TextMapper.Void) {
            textMapper.map(text)
        }

        override fun map(
            textView: AbstractView.Text,
            progressView: AbstractView,
            stateView: MessageState
        ) {
            textView.map(text)
            if (state == MyMessageUiState.PROGRESS)
                progressView.show()
            else
                progressView.hide()
            stateView.show(state)
        }

        override fun newState(state: MyMessageUiState) = Mine(text, state)

        override fun same(messageUi: MessageUi) =
            messageUi is Mine && text == messageUi.text

        override fun click(clickListener: ClickListener<MessageUi>) {
            if (state == MyMessageUiState.FAILED)
                clickListener.click(this)
        }
    }

    data class FromUser(private val text: String) : MessageUi {
        override fun isMyMessage() = false
        override fun map(textMapper: TextMapper.Void) {
            textMapper.map(text)
        }

        override fun newState(state: MyMessageUiState) = this

        override fun same(messageUi: MessageUi) =
            messageUi is FromUser && text == messageUi.text

        override fun map(
            textView: AbstractView.Text,
            progressView: AbstractView,
            stateView: MessageState
        ) = Unit

        override fun click(clickListener: ClickListener<MessageUi>) = Unit
    }
}

enum class MyMessageUiState {
    PROGRESS,
    FAILED,
    SENT,
    READ
}

interface TextMapper<T> : Abstract.Mapper.Data<String, T> {
    interface Void : TextMapper<Unit>
}