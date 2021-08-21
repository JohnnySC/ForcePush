package johnnysc.github.forcepush.ui.search

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 18.08.2021
 **/
interface SearchCommunication : Communication<SearchUserListUi> {
    class Base : Communication.Base<SearchUserListUi>(), SearchCommunication
}