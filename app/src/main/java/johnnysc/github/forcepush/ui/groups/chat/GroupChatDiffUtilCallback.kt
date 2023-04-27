package johnnysc.github.forcepush.ui.groups.chat

import androidx.recyclerview.widget.DiffUtil

/**
 * @author Asatryan on 13.09.2021
 */
class GroupChatDiffUtilCallback(
    private val oldList: List<GroupMessageUi>,
    private val newList: List<GroupMessageUi>,
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newList[newItemPosition].same(oldList[oldItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        newList[newItemPosition] == oldList[oldItemPosition]
}