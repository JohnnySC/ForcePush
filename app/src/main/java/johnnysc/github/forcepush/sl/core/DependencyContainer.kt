package johnnysc.github.forcepush.sl.core

import johnnysc.github.forcepush.sl.*


/**
 * @author Asatryan on 08.08.2021
 **/
interface DependencyContainer {

    fun module(feature: Feature): BaseModule<*>

    class Base(private val coreModule: CoreModule) : DependencyContainer {

        override fun module(feature: Feature) = when (feature) {
            Feature.LOGIN -> LoginModule(coreModule)
            Feature.MAIN -> MainModule(coreModule)
            Feature.SEARCH -> SearchModule(coreModule)
            Feature.MY_PROFILE -> MyProfileModule(coreModule)
            Feature.CHATS -> ChatsModule(coreModule)
            Feature.CHAT -> ChatModule(coreModule)
            Feature.CREATE_GROUP -> CreateGroupModule(coreModule)
            Feature.GROUP_CHAT -> GroupChatModule(coreModule)
            else -> throw IllegalStateException("unknown feature $feature")
        }
    }
}