package johnnysc.github.forcepush.ui.search

import johnnysc.github.forcepush.data.search.SearchData
import johnnysc.github.forcepush.data.search.SearchResultType

/**
 * @author Asatryan on 18.08.2021
 **/
interface SearchResultsMapper : SearchData.SearchMapper<SearchResultUi> {

    class Base : SearchResultsMapper {

        override fun map(
            id: String,
            name: String,
            photoUrl: String,
            type: SearchResultType
        ) = if (type == SearchResultType.USER)
            SearchResultUi.User(id, name, photoUrl)
        else
            SearchResultUi.Group(id, name)
    }
}