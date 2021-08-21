package johnnysc.github.forcepush.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.databinding.FragmentMyProfileBinding
import johnnysc.github.forcepush.sl.core.Feature
import johnnysc.github.forcepush.ui.core.BaseActivity
import johnnysc.github.forcepush.ui.main.BaseFragment

/**
 * @author Asatryan on 18.08.2021
 **/
class MyProfileFragment : BaseFragment<MyProfileViewModel>() {
    override fun viewModelClass() = MyProfileViewModel::class.java

    override val titleResId = R.string.title_profile

    override fun name() = Feature.MY_PROFILE.name

    private var _binding: FragmentMyProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observe(this) {
            it.map(binding.nameTextview, binding.loginTextView, binding.avatarImageView)
        }

        binding.signOutButton.setOnClickListener {
            viewModel.signOut()
            (requireActivity() as BaseActivity).switchToLogin()
        }

        viewModel.init()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}