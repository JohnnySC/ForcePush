package johnnysc.github.forcepush.ui.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * @author Asatryan on 05.08.2021
 **/
interface Observe<T> {
    fun observe(owner: LifecycleOwner, observer: Observer<T>)
}