package johnnysc.github.forcepush.core

/**
 * @author Asatryan on 15.08.2021
 **/
interface TextMapper<T> : Abstract.Mapper.Data<String, T> {
    interface Void : TextMapper<Unit> {
        override fun map(data: String) = Unit
    }
}