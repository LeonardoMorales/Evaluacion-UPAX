package dev.leonardom.evaluacionupax.core.util

import android.view.View
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence? = null,
    anchorView: View? = null,
    action: ((View) -> Unit)? = null,
) {
    val snackbar = Snackbar.make(view, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action?.let {
                action(this)
            }
        }

        anchorView?.let {
            snackbar.anchorView = it
        }

        snackbar.show()
    } else {
        anchorView?.let {
            snackbar.anchorView = it
        }

        snackbar.show()
    }
}