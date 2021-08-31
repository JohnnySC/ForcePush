package johnnysc.github.forcepush.ui.chats

import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.sl.core.Feature
import johnnysc.github.forcepush.ui.main.BaseFragment

/**
 * @author Asatryan on 25.08.2021
 */
class ChatsFragment : BaseFragment<ChatsViewModel>() {
    override fun viewModelClass() = ChatsViewModel::class.java
    override val titleResId = R.string.title_chats
    override fun name() = Feature.CHATS.name


    //todo do later -  list of chats

}