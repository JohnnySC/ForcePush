package johnnysc.github.forcepush.ui.core

import johnnysc.github.forcepush.ui.chat.TextMapper

/**
 * @author Asatryan on 14.08.2021
 **/
interface AbstractView {

    fun show()
    fun hide()

    interface Text : AbstractView, TextMapper.Void

    interface Image : AbstractView {

        fun load(url: String)
    }
}