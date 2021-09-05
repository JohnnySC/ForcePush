package johnnysc.github.forcepush.ui.chats

import johnnysc.github.forcepush.ui.core.AbstractView
import johnnysc.github.forcepush.ui.search.Chat

/**
 * @author Asatryan on 02.09.2021
 */
interface ChatUi {
    fun put(chatsMap: MutableMap<String, ChatUi>)
    fun aggregatedWith(userInfo: UserChatUi): ChatUi
    fun map(
        userName: AbstractView.Text,
        userAvatar: AbstractView.Image,
        message: AbstractView.Text,
        fromMe: AbstractView,
        unreadMessagesCount: AbstractView.Text
    ) = Unit
    fun same(chatUi: ChatUi): Boolean
    fun startChat(chat: Chat)
    fun sort() : Int

    data class Raw(
        private val otherUserId: String,
        private val lastMessageBody: String,
        private val notReadMessagesCount: Int,
        private val lastMessageFromUser: Boolean
    ) : ChatUi {

        override fun put(chatsMap: MutableMap<String, ChatUi>) {
            chatsMap[otherUserId] = this
        }

        override fun aggregatedWith(userInfo: UserChatUi) = Base(
            userInfo, lastMessageBody, lastMessageFromUser, notReadMessagesCount
        )

        override fun same(chatUi: ChatUi) = false

        override fun startChat(chat: Chat) = chat.startChatWith(otherUserId)
        override fun sort() = 0
    }

    data class Base(
        private val userData: UserChatUi,
        private val lastMessage: String,
        private val fromUser: Boolean,
        private val notReadMessagesCount: Int,
    ) : ChatUi {
        override fun put(chatsMap: MutableMap<String, ChatUi>) = Unit
        override fun aggregatedWith(userInfo: UserChatUi) = this
        override fun map(
            userName: AbstractView.Text,
            userAvatar: AbstractView.Image,
            message: AbstractView.Text,
            fromMe: AbstractView,
            unreadMessagesCount: AbstractView.Text
        ) {
            userData.map(userName, userAvatar)
            message.map(lastMessage)
            if (fromUser)
                fromMe.hide()
            else
                fromMe.show()
            if (notReadMessagesCount < 1)
                unreadMessagesCount.hide()
            else {
                unreadMessagesCount.map(notReadMessagesCount.toString())
                unreadMessagesCount.show()
            }
        }
        override fun same(chatUi: ChatUi) = chatUi is Base && chatUi.userData.same(userData)
        override fun startChat(chat: Chat) = userData.startChat(chat)
        override fun sort() = notReadMessagesCount
    }
}