package johnnysc.github.forcepush.ui.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import johnnysc.github.forcepush.core.Abstract

/**
 * @author Asatryan on 14.08.2021
 **/
abstract class BaseViewModel<E : Communication<T>, T : Abstract.UiObject>(protected val communication: E) :
    ViewModel(), Observe<T> {

    override fun observe(owner: LifecycleOwner, observer: Observer<T>) {
        communication.observe(owner, observer)
    }
}