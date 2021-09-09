package johnnysc.github.forcepush.ui.search

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 18.08.2021
 **/
interface SearchCommunication : Communication<SearchResultListUi> {
    class Base : Communication.Base<SearchResultListUi>(), SearchCommunication
}