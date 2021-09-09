package johnnysc.github.forcepush.ui.groups.create

import johnnysc.github.forcepush.core.Abstract

/**
 * @author Asatryan on 08.09.2021
 */
interface CreateGroupUi : Abstract.UiObject,
    Abstract.Object<Unit, Abstract.Mapper.Data<List<String>, Unit>> {

    class Base(private val groups: List<String>) : CreateGroupUi {
        override fun map(mapper: Abstract.Mapper.Data<List<String>, Unit>) {
            mapper.map(groups)
        }
    }
}