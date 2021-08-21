package johnnysc.github.forcepush.ui.core

/**
 * @author Asatryan on 14.08.2021
 **/
interface AbstractView {

    fun show()
    fun hide()

    interface Text : AbstractView {

        fun show(text: String)
    }

    interface Image : AbstractView {

        fun load(url: String)
    }
}