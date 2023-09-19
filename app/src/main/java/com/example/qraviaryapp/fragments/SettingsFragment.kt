package com.example.qraviaryapp.fragments
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreference
import com.example.qraviaryapp.R

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferences: SharedPreferences
    private var DARK_MODE: Int = 1

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val darkModeString = getString(R.string.dark_mode)
        val editor: SharedPreferences.Editor = sharedPreferences!!.edit()

        if (key == darkModeString) {
            val pref = sharedPreferences.getString(key, "1")

            when (pref?.toInt()) {
                1 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                    editor.putInt("DARK_MODE", 1)
                    editor.apply()
                }
                2 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    editor.putInt("DARK_MODE", 2)
                    editor.apply()
                }
                3 -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    editor.putInt("DARK_MODE", 3)
                    editor.apply()
                }
            }
        }

        when (key) {
            "notification_switch" -> {
                val isChecked = sharedPreferences.getBoolean(key, true)

                if (isChecked) {
                    // Handle notification switch ON
                } else {
                    // Handle notification switch OFF
                }
            }
            "auto_increment_switch" -> {
                val isChecked = sharedPreferences.getBoolean(key, true)

                if (isChecked) {
                    editor.putBoolean("Check", true)
                    editor.apply()
                } else {
                    editor.putBoolean("Check", false)
                    editor.apply()
                }
            }
            // Handle other preferences if needed
        }
    }

    override fun onDetach() {
        super.onDetach()
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}