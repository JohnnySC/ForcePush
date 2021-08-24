package johnnysc.github.forcepush.sl

import johnnysc.github.forcepush.data.profile.MyProfileRepository
import johnnysc.github.forcepush.data.profile.MyProfileData
import johnnysc.github.forcepush.sl.core.BaseModule
import johnnysc.github.forcepush.sl.core.CoreModule
import johnnysc.github.forcepush.ui.profile.MyProfileCommunication
import johnnysc.github.forcepush.ui.profile.MyProfileViewModel

/**
 * @author Asatryan on 18.08.2021
 **/
class MyProfileModule(private val coreModule: CoreModule) : BaseModule<MyProfileViewModel> {

    override fun viewModel() = MyProfileViewModel(
        MyProfileCommunication.Base(),
        MyProfileRepository.Base(coreModule.firebaseDatabaseProvider()),
        MyProfileData.MyProfileMapper.Base()
    )
}