package johnnysc.github.forcepush.ui.core

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.squareup.picasso.Picasso

/**
 * @author Asatryan on 18.08.2021
 **/
class CustomImageView : androidx.appcompat.widget.AppCompatImageView, AbstractView.Image {

    //region constructors
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    //endregion

    override fun load(url: String) {
        if (url.isNotEmpty())
            Picasso.get().load(url).into(this)
    }

    override fun show() {
        visibility = View.VISIBLE
    }

    override fun hide() {
        visibility = View.GONE
    }
}