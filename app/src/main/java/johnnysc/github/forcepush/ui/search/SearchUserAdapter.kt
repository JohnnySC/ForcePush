package johnnysc.github.forcepush.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.core.Abstract
import johnnysc.github.forcepush.databinding.SearchUserResultBinding
import johnnysc.github.forcepush.ui.core.ClickListener

/**
 * @author Asatryan on 18.08.2021
 **/
class SearchUserAdapter(
    private val clickListener: ClickListener<SearchUserUi>
) : RecyclerView.Adapter<SearchUserViewHolder>(),
    Abstract.Mapper.Data<List<SearchUserUi>, Unit> {

    private val list = ArrayList<SearchUserUi>()

    override fun getItemViewType(position: Int) = when (list[position]) {
        is SearchUserUi.Base -> 0
        is SearchUserUi.Empty -> 1
        is SearchUserUi.NoResults -> 2
        is SearchUserUi.Search -> 3
        else -> -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        0 -> SearchUserViewHolder.Base(
            SearchUserResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), clickListener
        )
        1 -> SearchUserViewHolder.Progress(R.layout.progress.view(parent))
        2 -> SearchUserViewHolder.NoResults(R.layout.no_results.view(parent))
        3 -> SearchUserViewHolder.Initial(R.layout.search.view(parent))
        else -> throw IllegalStateException("unknown viewType $viewType")
    }

    override fun onBindViewHolder(holder: SearchUserViewHolder, position: Int) =
        holder.bind(list[position])

    override fun getItemCount() = list.size

    override fun map(data: List<SearchUserUi>) {
        list.clear()
        list.addAll(data)
        notifyDataSetChanged() //todo diffutilcallback
    }
}

private fun Int.view(parent: ViewGroup) =
    LayoutInflater.from(parent.context).inflate(this, parent, false)

abstract class SearchUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(item: SearchUserUi) = Unit

    class Base(
        private val binding: SearchUserResultBinding,
        private val clickListener: ClickListener<SearchUserUi>
    ) : SearchUserViewHolder(binding.root) {

        override fun bind(item: SearchUserUi) {
            item.map(binding.avatarImageView, binding.userNameTextView)
            binding.chatButton.setOnClickListener { clickListener.click(item) }
        }
    }

    class Initial(view: View) : SearchUserViewHolder(view)
    class Progress(view: View) : SearchUserViewHolder(view)
    class NoResults(view: View) : SearchUserViewHolder(view)
}