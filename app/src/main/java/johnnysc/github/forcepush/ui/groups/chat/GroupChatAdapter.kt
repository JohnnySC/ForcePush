package johnnysc.github.forcepush.ui.groups.chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.databinding.MyGroupMessageLayoutBinding
import johnnysc.github.forcepush.databinding.UserGroupMessageLayoutBinding
import johnnysc.github.forcepush.ui.chat.MessageState
import johnnysc.github.forcepush.ui.core.AbstractView
import johnnysc.github.forcepush.ui.core.ClickListener

/**
 * @author Asatryan on 13.09.2021
 */
class GroupChatAdapter(
    private val clickListener: ClickListener<GroupMessageUi>
) : RecyclerView.Adapter<GroupChatViewHolder>(), Abstract.Mapper.Data<List<GroupMessageUi>, Unit> {

    private val messages = ArrayList<GroupMessageUi>()

    override fun getItemViewType(position: Int) = if (messages[position].isMyMessage()) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = if (viewType == 1)
        GroupChatViewHolder.MyMessageViewHolder(
            MyGroupMessageLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ), clickListener
        )
    else
        GroupChatViewHolder.UserMessageViewHolder(
            UserGroupMessageLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: GroupChatViewHolder, position: Int) =
        holder.bind(messages[position])

    override fun getItemCount() = messages.size

    override fun map(data: List<GroupMessageUi>) {
        val result = DiffUtil.calculateDiff(GroupChatDiffUtilCallback(messages, data))
        messages.clear()
        messages.addAll(data)
        result.dispatchUpdatesTo(this)
    }
}

abstract class GroupChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    open fun bind(item: GroupMessageUi) {}

    class MyMessageViewHolder(
        private val binding: MyGroupMessageLayoutBinding,
        private val clickListener: ClickListener<GroupMessageUi>
    ) : GroupChatViewHolder(binding.root) {

        override fun bind(item: GroupMessageUi) {
            item.map(
                binding.avatarImageView,
                AbstractView.Text.Empty(),
                binding.messageTextView,
                binding.progressBar,
                binding.iconView
            )
            binding.iconView.setOnClickListener { item.click(clickListener) }
        }
    }

    class UserMessageViewHolder(
        private val binding: UserGroupMessageLayoutBinding,
//        private val readMessage: ClickListener<GroupMessageUi>
    ) : GroupChatViewHolder(binding.root) {
        override fun bind(item: GroupMessageUi) {
            item.map(
                binding.avatarImageView,
                binding.userNameTextView,
                binding.messageTextView,
                AbstractView.Text.Empty(),
                MessageState.Empty()
            )
//            readMessage.click(item)
        }
    }
}