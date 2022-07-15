package com.example.githubuserapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserapp.R
import com.example.githubuserapp.adapter.ListAdapter
import com.example.githubuserapp.data.GitHub
import com.example.githubuserapp.databinding.FragmentFollowBinding
import com.example.githubuserapp.ui.DetailUserActivity
import com.example.githubuserapp.viewmodel.FollowersViewModel

class FollowersFragment : Fragment(R.layout.fragment_follow) {

    private var _binding : FragmentFollowBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel : FollowersViewModel
    private lateinit var adapter: ListAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = arguments
        username = args?.getString(DetailUserActivity.EXTRA_DATA).toString()

        _binding = FragmentFollowBinding.bind(view)

        adapter = ListAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: GitHub) {
                Intent(activity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_DATA, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_URL, data.avatar_url)
                    startActivity(it)
                }
            }

        })

        binding.apply {
            rvGithub.setHasFixedSize(true)
            rvGithub.layoutManager = LinearLayoutManager(activity)
            rvGithub.adapter = adapter
        }
        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(FollowersViewModel::class.java)
        viewModel.setListFollowers(username)
        viewModel.getListFollowers().observe(viewLifecycleOwner, {
            if (it!=null){
                adapter.setList(it)
                showLoading(false)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state: Boolean){
        if (state){
            binding.progressBar.visibility = View.VISIBLE
        }else{
            binding.progressBar.visibility = View.GONE
        }
    }

}