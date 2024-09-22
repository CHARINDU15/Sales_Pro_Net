package com.example.logggg

import android.content.Context
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

object Utils {
    fun showSuccessToast(context: Context, message: String) {
        showToast(
            context,
            message,
            R.layout.toast_success,
            R.drawable.ic_success,
            ContextCompat.getColor(context, R.color.white)
        )
    }

    fun showErrorToast(context: Context, message: String) {
        showToast(
            context,
            message,
            R.layout.toast_error,
            R.drawable.ic_error,
            ContextCompat.getColor(context, R.color.white)
        )
    }

    private fun showToast(
        context: Context,
        message: String,
        layoutId: Int,
        iconId: Int,
        iconColor: Int
    ) {
        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(layoutId, null)

        val icon = layout.findViewById<ImageView>(R.id.toast_icon)
        val text = layout.findViewById<TextView>(R.id.toast_text)

        val iconDrawable = ContextCompat.getDrawable(context, iconId)
        if (iconDrawable != null) {
            iconDrawable.setTint(iconColor) // Apply tint color here
            icon.setImageDrawable(iconDrawable)
        }
        text.text = message

        val toast = Toast(context)
        toast.duration = Toast.LENGTH_LONG
        toast.view = layout
        toast.show()
    }
}
