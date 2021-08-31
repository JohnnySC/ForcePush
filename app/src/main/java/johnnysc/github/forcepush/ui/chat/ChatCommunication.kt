package johnnysc.github.forcepush.ui.chat

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 25.08.2021
 */
interface ChatCommunication : Communication<List<MessageUi>> {
    class Base : Communication.Base<List<MessageUi>>(), ChatCommunication
}