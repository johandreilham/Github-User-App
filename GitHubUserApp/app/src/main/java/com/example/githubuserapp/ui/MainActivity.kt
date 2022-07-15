package com.example.githubuserapp.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.adapter.ListAdapter
import com.example.githubuserapp.data.GitHub
import com.example.githubuserapp.databinding.ActivityMainBinding
import com.example.githubuserapp.setting.SettingPreferences
import com.example.githubuserapp.viewmodel.MainViewModel
import com.example.githubuserapp.viewmodel.SettingViewModel
import com.example.githubuserapp.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ListAdapter
    private lateinit var viewModel : MainViewModel

    private lateinit var binding : ActivityMainBinding

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater);
        setContentView(binding.root)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref)).get(
            SettingViewModel::class.java)

        binding.apply {
            settingViewModel.getThemeSettings().observe(this@MainActivity,
                { isDarkModeActive: Boolean ->
                    if (isDarkModeActive) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    } else {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    }
                })
        }

        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GitHub) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_DATA, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.apply {
            rvGithub.layoutManager = LinearLayoutManager(this@MainActivity)
            rvGithub.setHasFixedSize(true)
            rvGithub.adapter = adapter

            btnSearch.setOnClickListener {
                searchUser()
            }

            etQuery.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER){
                    searchUser()
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }

        viewModel.getSearchUsers().observe(this, {
            if (it!=null){
                adapter.setList(it)
                showLoading(false)
            }
        })

    }

    private fun searchUser(){
        binding.apply {
            val query = etQuery.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUsers(query)
        }
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean{
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            R.id.share_menu -> {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, "https://api.github.com/users")
                intent.type="text/plain"
                startActivity(Intent.createChooser(intent,"Share"))
            }

            R.id.favorite_menu -> {
                Intent(this, FavoriteActivity::class.java).also {
                    startActivity(it)
                }
            }
            R.id.setting_menu -> {
                Intent(this, SettingActivity::class.java).also {
                    startActivity(it)
                }
            }

        }
        return super.onOptionsItemSelected(item)
    }

}