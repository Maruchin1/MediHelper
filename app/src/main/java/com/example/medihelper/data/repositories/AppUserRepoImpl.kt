package com.example.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.example.medihelper.domain.repositories.AppUserRepo

class AppUserRepoImpl : AppUserRepo {

    override fun getAuthToken(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserEmail(): String? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAuthTokenLive(): LiveData<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserEmailLive(): LiveData<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}