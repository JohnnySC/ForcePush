package johnnysc.github.forcepush.ui.core

import johnnysc.github.forcepush.ui.chat.TextMapper

/**
 * @author Asatryan on 14.08.2021
 **/
interface AbstractView {

    fun show()
    fun hide()

    interface Text : AbstractView, TextMapper.Void {
        class Empty : Text {
            override fun show() = Unit
            override fun hide() = Unit
            override fun map(data: String) = Unit
        }
    }

    interface Image : AbstractView {

        fun load(url: String)
    }
}