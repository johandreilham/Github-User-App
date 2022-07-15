package com.example.githubuserapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuserapp.favorite.FavoriteUser
import com.example.githubuserapp.favorite.FavoriteUserDao
import com.example.githubuserapp.favorite.UserDataBase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {

    private var userDao: FavoriteUserDao?
    private var userDb: UserDataBase?

    init {
        userDb = UserDataBase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getFavoriteUser()
    }
}