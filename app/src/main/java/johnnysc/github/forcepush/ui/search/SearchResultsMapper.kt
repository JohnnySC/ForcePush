package johnnysc.github.forcepush.ui.search

import johnnysc.github.forcepush.data.search.SearchData

/**
 * @author Asatryan on 18.08.2021
 **/
interface SearchResultsMapper : SearchData.SearchMapper<SearchUserUi> {

    class Base : SearchResultsMapper {

        override fun map(id: String, name: String, photoUrl: String): SearchUserUi {
            return SearchUserUi.Base(id, name, photoUrl)
        }
    }
}