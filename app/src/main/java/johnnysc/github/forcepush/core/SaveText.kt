package johnnysc.github.forcepush.core

/**
 * @author Asatryan on 15.08.2021
 */
class SaveText(private val dataSource: Save<String>) : TextMapper.Void {
    override fun map(data: String) = dataSource.save(data)
}