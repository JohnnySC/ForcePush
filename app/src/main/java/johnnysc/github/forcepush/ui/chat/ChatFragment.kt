package johnnysc.github.forcepush.ui.chat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.databinding.FragmentChatBinding
import johnnysc.github.forcepush.sl.core.Feature
import johnnysc.github.forcepush.ui.main.BaseFragment
import johnnysc.github.forcepush.ui.core.ClickListener

/**
 * @author Asatryan on 25.08.2021
 */
class ChatFragment : BaseFragment<ChatViewModel>() {
    override fun viewModelClass() = ChatViewModel::class.java
    override val titleResId = R.string.title_chat
    override fun name() = Feature.CHAT.name
    override fun showBottomNavigation() = false

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.sendMessageButton.setOnClickListener {
            viewModel.send(binding.messageEditText.text.toString())
            binding.messageEditText.text?.clear()
        }
        val adapter = ChatAdapter(Retry())
        binding.recyclerView.adapter = adapter
        viewModel.observe(this) {
            adapter.map(it)
            binding.recyclerView.scrollToPosition(it.size - 1)
        }
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

    private inner class Retry : ClickListener<MessageUi> {
        override fun click(item: MessageUi) { item.map(viewModel) }
    }
}