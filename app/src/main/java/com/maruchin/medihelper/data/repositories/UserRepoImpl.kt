package com.maruchin.medihelper.data.repositories

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.model.ProfileDb
import com.maruchin.medihelper.data.model.UserDb
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserRepoImpl(private val db: FirebaseFirestore) : UserRepo {

    override suspend fun addNew(entity: User): String? = withContext(Dispatchers.IO) {
        val userDb = UserDb(userName = entity.userName, email = entity.email)
        db.collection("users").document(entity.userId).set(userDb)
        return@withContext entity.userId
    }

    override suspend fun update(entity: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun deleteById(id: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getById(id: String): User {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getLiveById(id: String): LiveData<User?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllList(): List<User> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override suspend fun getAllListLive(): LiveData<List<User>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}