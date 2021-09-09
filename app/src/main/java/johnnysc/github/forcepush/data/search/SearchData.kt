package johnnysc.github.forcepush.data.search

/**
 * @author Asatryan on 18.08.2021
 **/
interface SearchData {

    fun <T> map(mapper: SearchMapper<T>): T

    class User(
        private val id: String,
        private val name: String,
        private val photoUrl: String,
    ) : SearchData {
        override fun <T> map(mapper: SearchMapper<T>): T {
            return mapper.map(id, name, photoUrl, SearchResultType.USER)
        }
    }

    class Group(
        private val id: String,
        private val name: String,
    ) : SearchData {
        override fun <T> map(mapper: SearchMapper<T>): T {
            return mapper.map(id, name, "", SearchResultType.GROUP)
        }
    }

    interface SearchMapper<T> {
        fun map(id: String, name: String, photoUrl: String, type: SearchResultType): T
    }
}

enum class SearchResultType {
    USER,
    GROUP
}