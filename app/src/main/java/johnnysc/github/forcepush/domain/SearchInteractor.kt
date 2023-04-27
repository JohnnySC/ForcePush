package johnnysc.github.forcepush.domain

import johnnysc.github.forcepush.data.search.SearchData
import johnnysc.github.forcepush.data.search.SearchGroupRepository
import johnnysc.github.forcepush.data.search.SearchUserRepository

/**
 * @author Asatryan on 07.09.2021
 */
interface SearchInteractor {

    suspend fun search(query: String): List<SearchData>
    suspend fun initChat(groupId: String): Boolean
    suspend fun initChatWith(userId: String): Boolean

    class Base(
        private val searchUserRepository: SearchUserRepository,
        private val searchGroupRepository: SearchGroupRepository
    ) : SearchInteractor {

        override suspend fun search(query: String) = makeResults(
            searchGroupRepository.search(query),
            searchUserRepository.search(query)
        )

        private fun makeResults(
            groups: List<SearchData>,
            users: List<SearchData>
        ) = if (groups.isEmpty()) {
            users
        } else {
            if (users.isEmpty())
                groups
            else {
                ArrayList(groups).apply { addAll(users) }
            }
        }

        override suspend fun initChat(groupId: String) =
            searchGroupRepository.initChat(groupId)

        override suspend fun initChatWith(userId: String) =
            searchUserRepository.initChatWith(userId)
    }
}