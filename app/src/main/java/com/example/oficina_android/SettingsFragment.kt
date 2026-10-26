package com.example.oficina_android

import android.os.Bundle
import android.view.View
import androidx.core.content.edit
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.google.android.material.radiobutton.MaterialRadioButton

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val preferences = requireContext().getSharedPreferences(PREFERENCES, 0)
        val selectedMode = preferences.getInt(KEY_THEME_MODE, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        val selectedId = when (selectedMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> R.id.themeLight
            AppCompatDelegate.MODE_NIGHT_YES -> R.id.themeDark
            else -> R.id.themeSystem
        }
        view.findViewById<MaterialRadioButton>(selectedId).isChecked = true

        val options = mapOf(
            R.id.themeSystem to AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM,
            R.id.themeLight to AppCompatDelegate.MODE_NIGHT_NO,
            R.id.themeDark to AppCompatDelegate.MODE_NIGHT_YES
        )
        options.forEach { (buttonId, mode) ->
            view.findViewById<MaterialRadioButton>(buttonId).setOnClickListener {
                preferences.edit { putInt(KEY_THEME_MODE, mode) }
                AppCompatDelegate.setDefaultNightMode(mode)
            }
        }
    }

    companion object {
        const val PREFERENCES = "app_settings"
        const val KEY_THEME_MODE = "theme_mode"
    }
}
