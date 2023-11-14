package com.example.qraviaryapp.fragments
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.qraviaryapp.R

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var sharedPreferencess: SharedPreferences
    private var DARK_MODE: Int = 1
    private lateinit var darkModeString: String
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferencess = PreferenceManager.getDefaultSharedPreferences(context)
        sharedPreferencess.registerOnSharedPreferenceChangeListener(this)
        darkModeString = getString(R.string.dark_mode)
        sharedPreferencess = requireContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val editor: SharedPreferences.Editor = sharedPreferencess.edit()

        if (key == darkModeString) {
            val pref = sharedPreferences?.getString(key, "1")

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
                val isChecked = sharedPreferences?.getBoolean(key, true)

                if (isChecked == true) {
                    // Handle notification switch ON
                } else {
                    // Handle notification switch OFF
                }
            }
            "incubating" -> {
                val incubatingValue = sharedPreferences?.getString(key, "")
                val editor: SharedPreferences.Editor = sharedPreferencess.edit()
                editor.putString("incubatingValue", incubatingValue)
                editor.apply()
            }
            "maturing" -> {
                val maturingValue = sharedPreferences?.getString(key, "")
                val editor: SharedPreferences.Editor = sharedPreferencess.edit()
                editor.putString("maturingValue", maturingValue)
                editor.apply()
            }

//            "auto_increment_switch" -> {
//                val isChecked = sharedPreferences?.getBoolean(key, true)
//
//                if (isChecked == true) {
//                    editor.putBoolean("Check", true)
//                    editor.apply()
//                } else {
//                    editor.putBoolean("Check", false)
//                    editor.apply()
//                }
//            }
            // Handle other preferences if needed
        }
    }

    override fun onDetach() {
        super.onDetach()
        sharedPreferencess.unregisterOnSharedPreferenceChangeListener(this)
    }
}
