package com.example.githubuserapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserapp.data.GitHub
import com.example.githubuserapp.api.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    val listFollowing = MutableLiveData<ArrayList<GitHub>>()

    fun setListFollowing(username: String) {
        ApiConfig.apiInstance
            .getFollowing(username)
            .enqueue(object : Callback<ArrayList<GitHub>> {
                override fun onResponse(
                    call: Call<ArrayList<GitHub>>,
                    response: Response<ArrayList<GitHub>>
                ) {
                    if (response.isSuccessful) {
                        listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<GitHub>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getListFollowing(): LiveData<ArrayList<GitHub>> {
        return listFollowing
    }

}