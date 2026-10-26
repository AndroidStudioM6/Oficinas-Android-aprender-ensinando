package com.example.oficina_android

import android.os.Bundle
import android.content.Context
import android.view.View
import androidx.core.content.edit
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.radiobutton.MaterialRadioButton
import android.widget.RadioGroup

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val options = mapOf(
            R.id.themeSystem to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            R.id.themeLight to AppCompatDelegate.MODE_NIGHT_NO,
            R.id.themeDark to AppCompatDelegate.MODE_NIGHT_YES
        )
        options.forEach { (buttonId, mode) ->
            view.findViewById<MaterialRadioButton>(buttonId).setOnClickListener {
                setThemeMode(requireContext(), mode)
            }
        }
        syncSelectedTheme(view)
    }

    override fun onResume() {
        super.onResume()
        view?.let(::syncSelectedTheme)
    }

    private fun syncSelectedTheme(view: View) {
        val selectedMode = getThemeMode(requireContext())
        val selectedId = when (selectedMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> R.id.themeLight
            AppCompatDelegate.MODE_NIGHT_YES -> R.id.themeDark
            else -> R.id.themeSystem
        }
        view.findViewById<RadioGroup>(R.id.themeOptions).check(selectedId)
    }

    companion object {
        const val PREFERENCES = "app_settings"
        const val KEY_THEME_MODE = "theme_mode"

        fun getThemeMode(context: Context): Int = context
            .getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE)
            .getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        fun setThemeMode(context: Context, mode: Int) {
            context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE).edit {
                putInt(KEY_THEME_MODE, mode)
            }
            AppCompatDelegate.setDefaultNightMode(mode)
        }
    }
}
