package johnnysc.github.forcepush.ui.search

import androidx.appcompat.widget.SearchView

/**
 * @author Asatryan on 22.08.2021
 */
class SimpleQueryListener(private val search: Search) : SearchView.OnQueryTextListener {
    override fun onQueryTextSubmit(query: String?) = find(query)
    override fun onQueryTextChange(newText: String?) = find(newText)

    private fun find(query: String?): Boolean {
        search.search(query.orEmpty().trim())
        return !query.isNullOrEmpty()
    }
}