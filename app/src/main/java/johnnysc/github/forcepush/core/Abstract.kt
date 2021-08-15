package johnnysc.github.forcepush.core

/**
 * @author Asatryan on 26.06.2021
 **/
abstract class Abstract {

    interface Object<T, M : Mapper> {

        fun map(mapper: M): T
    }

    interface UiObject {
        class Empty : UiObject
    }

    interface Mapper {

        interface Data<S, R> : Mapper {
            fun map(data: S): R
        }

        class Empty : Mapper
    }
}