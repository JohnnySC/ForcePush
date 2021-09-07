package johnnysc.github.forcepush.ui.search

import androidx.lifecycle.viewModelScope
import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.core.Delay
import johnnysc.github.forcepush.data.search.SearchUserRepository
import johnnysc.github.forcepush.ui.core.BaseViewModel
import johnnysc.github.forcepush.ui.main.NavigationCommunication
import johnnysc.github.forcepush.ui.main.NavigationUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author Asatryan on 18.08.2021
 **/
class SearchViewModel(
    searchCommunication: SearchCommunication,
    private val mapper: SearchResultsMapper,
    private val repository: SearchUserRepository,
    private val navigation: NavigationCommunication,
    private val dispatchersIO: CoroutineDispatcher = Dispatchers.IO,
    private val dispatchersMain: CoroutineDispatcher = Dispatchers.Main,
) : BaseViewModel<SearchCommunication, SearchUserListUi>(searchCommunication), Search, Chat {

    private val delay = Delay<String> { query ->
        viewModelScope.launch(dispatchersIO) { find(query) }
    }

    private val initial = SearchUserListUi.Base(listOf(SearchUserUi.Search()))

    private var cleared = false

    override fun search(query: String) {
        cleared = query.length < 3
        if (cleared) {
            delay.clear()
            communication.map(initial)
        } else {
            communication.map(SearchUserListUi.Base(listOf(SearchUserUi.Empty())))
            delay.add(query.lowercase())
        }
    }

    override fun startChatWith(userId: String) {
        viewModelScope.launch(dispatchersIO) {
            if (repository.initChatWith(userId))
                withContext(dispatchersMain) { navigation.map(NavigationUi(R.id.chat_screen)) }
            //todo else handle error
        }
    }

    private suspend fun find(query: String) {
        val raw = repository.search(query)
        val list = raw.map { it.map(mapper) }
        withContext(dispatchersMain) {
            val result =
                if (list.isEmpty()) mutableListOf<SearchUserUi>(SearchUserUi.NoResults())
                else ArrayList(list)
            if (!cleared)
                communication.map(SearchUserListUi.Base(result))
        }
    }

    fun init() {
        communication.map(initial)
    }
}