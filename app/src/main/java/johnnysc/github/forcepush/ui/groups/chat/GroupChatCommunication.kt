package johnnysc.github.forcepush.ui.groups.chat

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 13.09.2021
 */
interface GroupChatCommunication : Communication<List<GroupMessageUi>> {
    class Base : Communication.Base<List<GroupMessageUi>>(), GroupChatCommunication
}