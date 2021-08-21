package johnnysc.github.forcepush.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import johnnysc.github.forcepush.core.Match
import johnnysc.github.forcepush.ui.core.BaseViewModel

/**
 * @author Asatryan on 18.08.2021
 **/
abstract class BaseFragment<T : BaseViewModel<*, *>> : Fragment(), Match<String> {

    protected lateinit var viewModel: T
    protected abstract fun viewModelClass(): Class<T>
    protected abstract val titleResId: Int

    abstract fun name(): String

    override fun matches(data: String) = name() == data

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = (requireActivity() as MainActivity).viewModel(viewModelClass(), this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setTitle(titleResId)
    }
}