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

class FollowersViewModel: ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<GitHub>>()

    fun setListFollowers(username: String){
        ApiConfig.apiInstance
            .getFollowers(username)
            .enqueue(object : Callback<ArrayList<GitHub>>{
                override fun onResponse(
                    call: Call<ArrayList<GitHub>>,
                    response: Response<ArrayList<GitHub>>
                ) {
                    if (response.isSuccessful){
                        listFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<GitHub>>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }

            })
    }

    fun getListFollowers(): LiveData<ArrayList<GitHub>>{
        return listFollowers
    }

}