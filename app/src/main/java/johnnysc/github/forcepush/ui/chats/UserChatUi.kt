package johnnysc.github.forcepush.ui.chats

import johnnysc.github.forcepush.ui.core.AbstractView
import johnnysc.github.forcepush.ui.search.Chat

/**
 * @author Asatryan on 02.09.2021
 */
interface UserChatUi {
    fun map(userName: AbstractView.Text, userAvatar: AbstractView.Image)
    fun same(userData: UserChatUi): Boolean
    fun startChat(chat: Chat)

    data class Base(
        private val id: String,
        private val name: String,
        private val avatarUrl: String
    ) : UserChatUi {

        override fun map(
            userName: AbstractView.Text,
            userAvatar: AbstractView.Image
        ) {
            userName.map(name)
            userAvatar.load(avatarUrl)
        }

        override fun same(userData: UserChatUi) = userData is Base && userData.id == id
        override fun startChat(chat: Chat) = chat.startChatWith(id)
    }
}