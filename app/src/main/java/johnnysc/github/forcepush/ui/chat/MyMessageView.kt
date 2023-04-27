package johnnysc.github.forcepush.ui.chat

import android.content.Context
import android.util.AttributeSet
import android.view.View
import johnnysc.github.forcepush.R
import johnnysc.github.forcepush.ui.core.AbstractView

/**
 * @author Asatryan on 29.08.2021
 */
class MyMessageView : androidx.appcompat.widget.AppCompatImageView, MessageState {
    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    override fun show(state: MyMessageUiState) {
        val imageResId = when (state) {
            MyMessageUiState.SENT -> R.drawable.ic_done_black_24
            MyMessageUiState.FAILED -> R.drawable.ic_replay_black_24
            MyMessageUiState.READ -> R.drawable.ic_double_check_black_24
            else -> 0
        }
        setImageResource(imageResId)
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}

interface MessageState : AbstractView {
    fun show(state: MyMessageUiState)

    class Empty : MessageState {
        override fun show(state: MyMessageUiState) = Unit
        override fun show() = Unit
        override fun hide() = Unit
    }
}