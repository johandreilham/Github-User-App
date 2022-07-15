package com.example.githubuserapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.adapter.ListAdapter
import com.example.githubuserapp.data.GitHub
import com.example.githubuserapp.databinding.ActivityFavoriteBinding
import com.example.githubuserapp.favorite.FavoriteUser
import com.example.githubuserapp.viewmodel.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var adapter: ListAdapter
    private lateinit var viewModel: FavoriteViewModel

    private lateinit var binding: ActivityFavoriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.title = "Favorite"


        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GitHub) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_DATA, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvUser.setHasFixedSize(true)
            rvUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvUser.adapter = adapter
        }

        viewModel.getFavoriteUser()?.observe(this,{
            if (it!=null){
                val list = List(it)
                adapter.setList(list)
            }
        })

    }

    private fun List(users: List<FavoriteUser>): ArrayList<GitHub> {
        val listUsers = ArrayList<GitHub>()
        for (user in users){
            val userMapped = GitHub(
                user.login,
                user.id,
                user.avatar_url
            )
            listUsers.add(userMapped)
        }
        return listUsers

    }
}