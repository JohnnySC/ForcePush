package johnnysc.github.forcepush.ui.chats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.databinding.FragmentChatsBinding
import johnnysc.github.forcepush.sl.core.Feature
import johnnysc.github.forcepush.ui.core.ClickListener
import johnnysc.github.forcepush.ui.main.BaseFragment

/**
 * @author Asatryan on 25.08.2021
 */
class ChatsFragment : BaseFragment<ChatsViewModel>() {
    override fun viewModelClass() = ChatsViewModel::class.java
    override val titleResId = R.string.title_chats
    override fun name() = Feature.CHATS.name

    private var _binding: FragmentChatsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ChatsAdapter(OpenChat())
        binding.recyclerView.adapter = adapter
        viewModel.observe(this) { adapter.map(it) }
    }

    override fun onResume() {
        super.onResume()
        viewModel.startGettingUpdates()
    }

    override fun onPause() {
        super.onPause()
        viewModel.stopGettingUpdates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private inner class OpenChat : ClickListener<ChatUi> {
        override fun click(item: ChatUi) = item.startChat(viewModel)
    }
}