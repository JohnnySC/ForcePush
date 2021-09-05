package johnnysc.github.forcepush.data.chats

import johnnysc.github.forcepush.domain.chats.UserChatDomain

/**
 * @author Asatryan on 02.09.2021
 */
interface UserInfoData {

    fun <T> map(mapper: UserChatDataMapper<T>): T

    class Base(
        private val id: String,
        private val name: String,
        private val avatarUrl: String
    ) : UserInfoData {
        override fun <T> map(mapper: UserChatDataMapper<T>) = mapper.map(id, name, avatarUrl)
    }
}

interface UserChatDataMapper<T> {

    fun map(id: String, name: String, avatarUrl: String): T

    class Base : UserChatDataMapper<UserChatDomain> {
        override fun map(id: String, name: String, avatarUrl: String) =
            UserChatDomain.Base(id, name, avatarUrl)
    }
}