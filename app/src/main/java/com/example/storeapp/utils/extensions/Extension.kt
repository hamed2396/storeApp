package com.example.storeapp.utils.extensions

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.view.View
import android.widget.ImageView
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import coil.load
import coil.request.CachePolicy
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.crazylegend.kotlinextensions.views.color
import com.crazylegend.kotlinextensions.views.gone
import com.crazylegend.kotlinextensions.views.visible
import com.crazylegend.kotlinextensions.whether
import com.example.storeapp.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat

object Extension {
    fun <T : Fragment> T.launchIoLifeCycle(action: suspend CoroutineScope.() -> Unit) =
        lifecycleScope.launch {
            action()

        }

    fun ImageView.loadImage(url: String) {
        load(url) {
            crossfade(true)
            crossfade(500)
            diskCachePolicy(CachePolicy.ENABLED)
            placeholder(R.drawable.placeholder)
        }

    }
}

fun ImageView.loadImageWithGlide(url: String) {
    Glide.with(context).load(url).diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(this)

}

fun View.isVisible(isShowLoading: Boolean, container: View) {
    isShowLoading.whether({
        //true
        this.visible()
        container.gone()
    }, {
        //false
        this.gone()
        container.visible()

    })
}

fun Number.formatWithCommas(): String {
    val formatter = DecimalFormat("#,###")
    return "${formatter.format(this)} تومان"
}

fun Dialog.transparentCorners() {
    window?.setBackgroundDrawableResource(android.R.color.transparent)
}

@SuppressLint("Range")
fun getRealPathUri(context: Context, uri: Uri): String? {
    var result: String? = null
    val resolver = context.contentResolver.query(uri, null, null, null, null)
    if (resolver == null) {
        result = uri.path
    } else {
        if (resolver.moveToFirst()) {
            result = resolver.getString(resolver.getColumnIndex(MediaStore.Images.Media.DATA))
        }
        resolver.close()
    }
    return result
}

fun Uri.openBrowser(context: Context?) {

    Intent(Intent.ACTION_VIEW, this).apply { context?.startActivity(this) }


}

fun ImageView.changeTint(color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color(color)))

}

fun setTypefaceNormal(context: Context): Typeface {
    return Typeface.createFromAsset(context.assets, "fonts/iransans.ttf")
}

infix fun <T : Comparable<T>> T.isBiggerThan(that: T): Boolean {
    return if (this is Number && that is Number) this > that else throw IllegalArgumentException("Only Numbers Are Allowed")
}

infix fun <T : Comparable<T>> T.isLessThan(that: T): Boolean {
    return if (this is Number && that is Number) this < that else throw IllegalArgumentException("Only Numbers Are Allowed")
}

val String.toEditable: Editable get() = Editable.Factory.getInstance().newEditable(this)









