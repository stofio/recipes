package com.recipegenerator.domain.extensions

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.recipegenerator.R

fun Context.toast(text: String?) = Toast.makeText(this, text ?: "", Toast.LENGTH_SHORT).show()

fun Context.toast(@StringRes text: Int) = Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

fun Fragment.toast(text: String?) = requireContext().toast(text)

fun Fragment.toast(@StringRes text: Int) = requireContext().toast(text)

fun Fragment.hideKeyboard() {
    val v = view?.findFocus() ?: View(requireContext())
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(v.windowToken, 0)
}

fun Fragment.confirmDialog(
    title: String,
    message: String,
    positiveText: String = this.resources.getString(R.string.confirm),
    negativeText: String = this.resources.getString(R.string.cancel),
    positiveClickListener: () -> Unit = {}
) {
    MaterialDialog(requireActivity()).show {
        title(text = title)
        message(text = message)
        positiveButton(text = positiveText) {
            positiveClickListener.invoke()
            dismiss()
        }
        negativeButton(text = negativeText) {
            dismiss()
        }
    }
}