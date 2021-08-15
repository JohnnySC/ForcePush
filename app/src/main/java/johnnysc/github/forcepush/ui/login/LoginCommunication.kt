package johnnysc.github.forcepush.ui.login

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 14.08.2021
 **/
interface LoginCommunication : Communication<LoginUi> {

    class Base : Communication.Base<LoginUi>(), LoginCommunication
}