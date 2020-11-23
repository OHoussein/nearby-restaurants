package dev.ohoussein.restos.ui.core.util

import android.view.View
import android.widget.TextView

object ViewUtils {

    fun TextView.showOrHideIfNull(content: String?, visibilityIfNull: Int = View.GONE) {
        if (content != null) {
            visibility = View.VISIBLE
            text = content
        } else {
            visibility = visibilityIfNull
            text = null
        }
    }
}