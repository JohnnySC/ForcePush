package johnnysc.github.forcepush.ui.profile

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 18.08.2021
 **/
interface MyProfileCommunication : Communication<MyProfileUi> {
    class Base : Communication.Base<MyProfileUi>(), MyProfileCommunication
}