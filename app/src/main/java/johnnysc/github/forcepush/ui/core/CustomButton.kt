package johnnysc.github.forcepush.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.View

/**
 * @author Asatryan on 14.08.2021
 **/
class CustomButton : androidx.appcompat.widget.AppCompatButton, AbstractView {
    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }

}