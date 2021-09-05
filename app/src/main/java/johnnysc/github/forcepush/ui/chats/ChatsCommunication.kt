package johnnysc.github.forcepush.ui.chats

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 25.08.2021
 */
interface ChatsCommunication : Communication<List<ChatUi>> {
    class Base : Communication.Base<List<ChatUi>>(), ChatsCommunication
}