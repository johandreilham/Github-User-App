package com.example.githubuserapp.ui

import android.content.Context
import android.os.Bundle
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import com.example.githubuserapp.databinding.ActivitySettingBinding
import com.example.githubuserapp.setting.SettingPreferences
import com.example.githubuserapp.viewmodel.SettingViewModel
import com.example.githubuserapp.viewmodel.ViewModelFactory



class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Setting"

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java)

        binding.apply {
            settingViewModel.getThemeSettings().observe(this@SettingActivity,
                { isDarkModeActive: Boolean ->
                    if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        switchTheme.isChecked = true
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                        switchTheme.isChecked = false
                    }
                })

            switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                settingViewModel.saveThemeSetting(isChecked)
            }
        }
    }
}