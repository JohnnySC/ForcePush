package johnnysc.github.forcepush.ui.chats

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 25.08.2021
 */
interface ChatsCommunication : Communication<ChatsUi> {
    class Base : Communication.Base<ChatsUi>(), ChatsCommunication
}