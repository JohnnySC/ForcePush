package johnnysc.github.forcepush.ui.main

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 18.08.2021
 **/
interface NavigationCommunication : Communication<NavigationUi> {
    class Base : Communication.Base<NavigationUi>(), NavigationCommunication
}