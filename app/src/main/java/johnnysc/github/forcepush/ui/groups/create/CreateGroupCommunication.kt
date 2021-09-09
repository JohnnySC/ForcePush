package johnnysc.github.forcepush.ui.groups.create

import johnnysc.github.forcepush.ui.core.Communication

/**
 * @author Asatryan on 08.09.2021
 */
interface CreateGroupCommunication : Communication<CreateGroupUi> {
    class Base : Communication.Base<CreateGroupUi>(), CreateGroupCommunication
}