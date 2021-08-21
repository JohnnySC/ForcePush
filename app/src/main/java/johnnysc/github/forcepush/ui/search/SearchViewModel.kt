package johnnysc.github.forcepush.ui.search

import androidx.lifecycle.viewModelScope
import johnnysc.github.forcepush.data.search.SearchUserRepository
import johnnysc.github.forcepush.ui.core.BaseViewModel
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
) : BaseViewModel<SearchCommunication, SearchUserListUi>(
    searchCommunication
) {

    private val delay = Delay { query ->
        viewModelScope.launch(Dispatchers.IO) { find(query) }
    }

    private val initial = SearchUserListUi.Base(listOf(SearchUserUi.Search()))

    private var cleared = false

    fun search(query: String) {
        cleared = query.length < 3
        if (cleared) {
            delay.clear()
            communication.map(initial)
        } else {
            communication.map(SearchUserListUi.Base(listOf(SearchUserUi.Empty())))
            delay.add(query.lowercase())
        }
    }

    private suspend fun find(query: String) {
        val raw = repository.search(query)
        val list = raw.map { it.map(mapper) }
        withContext(Dispatchers.Main) {
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