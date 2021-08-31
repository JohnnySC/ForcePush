package johnnysc.github.forcepush.ui.core

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import johnnysc.github.forcepush.core.Abstract

/**
 * @author Asatryan on 12.07.2021
 **/
interface Communication<T> : Observe<T>, Abstract.Mapper.Data<T, Unit> {

    abstract class Base<T : Any> : Communication<T> {
        protected val liveData = MutableLiveData<T>()
        override fun map(data: T) {
            liveData.value = data
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<T>) =
            liveData.observe(owner, observer)
    }

    class Empty : Communication<Abstract.UiObject.Empty> {
        override fun observe(owner: LifecycleOwner, observer: Observer<Abstract.UiObject.Empty>) = Unit
        override fun map(data: Abstract.UiObject.Empty) = Unit
    }
}