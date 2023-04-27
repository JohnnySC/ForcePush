package johnnysc.github.forcepush.ui.groups.chat

import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import johnnysc.github.forcepush.domain.group.GroupChatUserInfo
import johnnysc.github.forcepush.ui.chat.*
import johnnysc.github.forcepush.ui.core.AbstractView
import johnnysc.github.forcepush.ui.core.ClickListener

/**
 * @author Asatryan on 13.09.2021
 */
interface GroupMessageUi {

    fun userId(): String
    fun isMyMessage(): Boolean

    fun map(textMapper: TextMapper.Void)
    fun map(
        avatar: AbstractView.Image,
        name: AbstractView.Text,
        textView: AbstractView.Text,
        progressView: AbstractView,
        stateView: MessageState
    )

    fun newState(state: MyMessageUiState): GroupMessageUi
    fun same(messageUi: GroupMessageUi): Boolean
    fun click(clickListener: ClickListener<GroupMessageUi>)

    fun read(readMessage: ReadMessage)
    fun aggregatedWith(userInfo: GroupChatUserInfo): GroupMessageUi

    data class MineRaw(
        private val text: String,
        private val state: MyMessageUiState
    ) : GroupMessageUi {
        override fun userId() = Firebase.auth.currentUser!!.uid//todo make better later
        override fun isMyMessage() = true
        override fun map(textMapper: TextMapper.Void) {
            textMapper.map(text)
        }

        override fun map(
            avatar: AbstractView.Image,
            name: AbstractView.Text,
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

        override fun newState(state: MyMessageUiState) = MineRaw(text, state)

        override fun same(messageUi: GroupMessageUi) =
            messageUi is MineRaw && text == messageUi.text

        override fun click(clickListener: ClickListener<GroupMessageUi>) {
            if (state == MyMessageUiState.FAILED)
                clickListener.click(this)
        }

        override fun read(readMessage: ReadMessage) = Unit
        override fun aggregatedWith(userInfo: GroupChatUserInfo): GroupMessageUi {
            return Mine(userInfo.avatarUrl, text, state)
        }
    }

    data class Mine(
        private val avatarUrl: String,
        private val text: String,
        private val state: MyMessageUiState
    ) : GroupMessageUi {
        override fun userId() = Firebase.auth.currentUser!!.uid//todo make better later
        override fun isMyMessage() = true

        override fun map(textMapper: TextMapper.Void) {
            textMapper.map(text)
        }

        override fun map(
            avatar: AbstractView.Image,
            name: AbstractView.Text,
            textView: AbstractView.Text,
            progressView: AbstractView,
            stateView: MessageState
        ) {
            avatar.load(avatarUrl)
            textView.map(text)
            if (state == MyMessageUiState.PROGRESS)
                progressView.show()
            else
                progressView.hide()
            stateView.show(state)
        }

        override fun newState(state: MyMessageUiState): GroupMessageUi {
            return Mine(avatarUrl, text, state)
        }

        override fun same(messageUi: GroupMessageUi) = messageUi is Mine && text == messageUi.text

        override fun click(clickListener: ClickListener<GroupMessageUi>) {
            if (state == MyMessageUiState.FAILED)
                clickListener.click(this)
        }

        override fun read(readMessage: ReadMessage) = Unit

        override fun aggregatedWith(userInfo: GroupChatUserInfo) = this
    }

    data class FromUserRaw(
        private val id: String,
        private val userId: String,
        private val text: String,
        private val isRead: Boolean
    ) : GroupMessageUi {
        override fun userId() = userId

        override fun isMyMessage() = false
        override fun map(textMapper: TextMapper.Void) {
            textMapper.map(text)
        }

        override fun newState(state: MyMessageUiState) = this

        override fun same(messageUi: GroupMessageUi) =
            messageUi is FromUserRaw && id == messageUi.id

        override fun map(
            avatar: AbstractView.Image,
            name: AbstractView.Text,
            textView: AbstractView.Text,
            progressView: AbstractView,
            stateView: MessageState
        ) = Unit

        override fun click(clickListener: ClickListener<GroupMessageUi>) = Unit

        override fun read(readMessage: ReadMessage) {
            if (!isRead) readMessage.readMessage(id)
        }

        override fun aggregatedWith(userInfo: GroupChatUserInfo) =
            FromUser(id, userId, text, isRead, userInfo.name, userInfo.avatarUrl, isRead)
    }

    data class FromUser(
        private val id: String,
        private val userId: String,
        private val text: String,
        private val wasRead: Boolean,
        private val name: String,
        private val avatarUrl: String,
        private val isRead: Boolean
    ) : GroupMessageUi {
        override fun userId() = id

        override fun isMyMessage() = false

        override fun map(textMapper: TextMapper.Void) {
            textMapper.map(text)
        }

        override fun map(
            avatar: AbstractView.Image,
            name: AbstractView.Text,
            textView: AbstractView.Text,
            progressView: AbstractView,
            stateView: MessageState
        ) {
            avatar.load(avatarUrl)
            name.map(this.name)
            textView.map(text)
        }

        override fun newState(state: MyMessageUiState) = this

        override fun same(messageUi: GroupMessageUi) = messageUi is FromUser && id == messageUi.id

        override fun click(clickListener: ClickListener<GroupMessageUi>) = Unit

        override fun read(readMessage: ReadMessage) {
            if (!isRead) readMessage.readMessage(id)
        }

        override fun aggregatedWith(userInfo: GroupChatUserInfo) = this
    }
}