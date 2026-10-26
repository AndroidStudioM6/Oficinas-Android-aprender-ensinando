package com.example.oficina_android

import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import com.google.android.material.color.MaterialColors

object PriorityTheme {

    fun applyBadge(view: TextView, priority: String) {
        val (backgroundAttribute, foregroundAttribute) = colorAttributes(view, priority)
        val backgroundColor = MaterialColors.getColor(view, backgroundAttribute)
        val foregroundColor = MaterialColors.getColor(view, foregroundAttribute)
        val density = view.resources.displayMetrics.density
        view.background = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = 14f * density
            setColor(backgroundColor)
        }
        view.setTextColor(foregroundColor)
    }

    fun applyText(view: TextView, priority: String) {
        val (_, foregroundAttribute) = colorAttributes(view, priority)
        view.setTextColor(MaterialColors.getColor(view, foregroundAttribute))
    }

    private fun colorAttributes(view: TextView, priority: String): Pair<Int, Int> {
        val context = view.context
        return when (priority) {
            context.getString(R.string.priority_high) ->
                com.google.android.material.R.attr.colorErrorContainer to
                    com.google.android.material.R.attr.colorOnErrorContainer
            context.getString(R.string.priority_low) ->
                com.google.android.material.R.attr.colorPrimaryContainer to
                    com.google.android.material.R.attr.colorOnPrimaryContainer
            context.getString(R.string.priority_medium) ->
                com.google.android.material.R.attr.colorTertiaryContainer to
                    com.google.android.material.R.attr.colorOnTertiaryContainer
            else -> com.google.android.material.R.attr.colorSecondaryContainer to
                com.google.android.material.R.attr.colorOnSecondaryContainer
        }
    }
}
