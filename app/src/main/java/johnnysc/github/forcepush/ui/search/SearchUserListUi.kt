package johnnysc.github.forcepush.ui.search

import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.ui.core.AbstractView

/**
 * @author Asatryan on 18.08.2021
 **/
interface SearchUserListUi : Abstract.UiObject,
    Abstract.Object<Unit, Abstract.Mapper.Data<List<SearchUserUi>, Unit>> {

    class Base(private val list: List<SearchUserUi>) : SearchUserListUi {
        override fun map(mapper: Abstract.Mapper.Data<List<SearchUserUi>, Unit>) {
            mapper.map(list)
        }
    }
}

interface SearchUserUi {

    fun chat(chat: Chat) = Unit

    fun map(
        avatar: AbstractView.Image,
        userName: AbstractView.Text,
    ) = Unit

    class Base(
        private val id: String,
        private val name: String,
        private val photoUrl: String,
    ) : SearchUserUi {

        override fun chat(chat: Chat) = chat.startChatWith(id)

        override fun map(
            avatar: AbstractView.Image,
            userName: AbstractView.Text,
        ) {
            avatar.load(photoUrl)
            userName.map(name)
        }
    }

    class Search : SearchUserUi
    class Empty : SearchUserUi
    class NoResults : SearchUserUi
}

interface Chat {

    fun startChatWith(userId: String)
}