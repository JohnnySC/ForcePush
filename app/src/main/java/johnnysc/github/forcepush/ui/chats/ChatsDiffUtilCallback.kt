package johnnysc.github.forcepush.ui.chats

import androidx.recyclerview.widget.DiffUtil

/**
 * @author Asatryan on 02.09.2021
 */
class ChatsDiffUtilCallback(
    private val oldList: List<ChatUi>,
    private val newList: List<ChatUi>
) : DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition].same(newList[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
        oldList[oldItemPosition] == newList[newItemPosition]
}