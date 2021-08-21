package johnnysc.github.forcepush.sl

import johnnysc.github.forcepush.data.search.SearchUserRepository
import johnnysc.github.forcepush.sl.core.BaseModule
import johnnysc.github.forcepush.ui.search.SearchCommunication
import johnnysc.github.forcepush.ui.search.SearchResultsMapper
import johnnysc.github.forcepush.ui.search.SearchViewModel

/**
 * @author Asatryan on 18.08.2021
 **/
class SearchModule : BaseModule<SearchViewModel> {
    override fun viewModel() = SearchViewModel(
        SearchCommunication.Base(),
        SearchResultsMapper.Base(),
        SearchUserRepository.Base()
    )
}