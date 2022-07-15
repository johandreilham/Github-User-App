package com.example.githubuserapp.ui



import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuserapp.R
import com.example.githubuserapp.SectionsPagerAdapter
import com.example.githubuserapp.databinding.ActivityDetailUserBinding
import com.example.githubuserapp.viewmodel.DetailUserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class DetailUserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater);
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_DATA)
        val id = intent.getIntExtra(EXTRA_ID,0)
        val avatarUrl = intent.getStringExtra(EXTRA_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_DATA,username)

        viewModel = ViewModelProvider(this).get(DetailUserViewModel::class.java)

        viewModel.setUserDetail(username.toString())
        viewModel.getUserDetail().observe(this, {
            if (it != null){
                binding.apply {
                    txtName.text = it.name
                    txtUsermane.text = StringBuilder().append("@").append(it.login)
                    txtFollowers.text = StringBuilder().append(it.followers).append(" Followers")
                    txtFollowing.text = StringBuilder().append(it.following).append(" Following")
                    txtRepository.text = StringBuilder().append(it.public_repos).append(" Repository")
                    txtCompany.text = StringBuilder().append("Company: ").append(it.company)
                    txtLocation.text = StringBuilder().append("Location: ").append(it.location)
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatar_url)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(imgPhoto)
                }
            }
        })

        var _isChecked = false
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null){
                    if (count>0){
                        binding.toggleFavorite.isChecked = true
                        _isChecked = true
                    }else{
                        binding.toggleFavorite.isChecked = false
                        _isChecked = false
                    }
                }
            }
        }

        binding.toggleFavorite.setOnClickListener {
            _isChecked = !_isChecked
            if (_isChecked){
                viewModel.addToFavorite(username.toString(), id, avatarUrl.toString())
            }else{
                viewModel.removeFromFavorite(id)
            }
            binding.toggleFavorite.isChecked = _isChecked
        }

        val sectionPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager, bundle)
        binding.apply {
            viewPager.adapter = sectionPagerAdapter
            tabs.setupWithViewPager(viewPager)
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

    companion object {
        const val EXTRA_DATA = "extra_data"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_URL = "extra_url"
    }
}



