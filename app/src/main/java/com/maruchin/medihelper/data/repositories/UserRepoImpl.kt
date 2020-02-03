package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.utils.AppFirebase
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserRepoImpl(
    private val appFirebase: AppFirebase,
    private val auth: FirebaseAuth
) : UserRepo {

    private val collectionRef: CollectionReference
        get() = appFirebase.users

    override suspend fun signIn(email: String, password: String): String = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.getCurrUserId()
        } catch (ex: FirebaseAuthInvalidUserException) {
            throw UserRepo.IncorrectEmailException()
        } catch (ex: FirebaseAuthInvalidCredentialsException) {
            throw UserRepo.IncorrectPasswordException()
        } catch (ex: FirebaseAuthException) {
            throw  UserRepo.UndefinedAuthException()
        }
    }

    override suspend fun signUp(email: String, password: String): String = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val userId = auth.getCurrUserId()
            collectionRef.document(userId)
            userId
        } catch (ex: FirebaseAuthEmailException) {
            throw UserRepo.IncorrectEmailException()
        } catch (ex: FirebaseAuthUserCollisionException) {
            throw UserRepo.UserAlreadyExistException()
        } catch (ex: FirebaseAuthWeakPasswordException) {
            throw UserRepo.WeakPasswordException()
        } catch (ex: FirebaseAuthException) {
            throw UserRepo.UndefinedAuthException()
        }
    }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        auth.signOut()
        return@withContext
    }

    override suspend fun changePassword(newPassword: String) = withContext(Dispatchers.IO) {
        try {
            auth.currentUser?.updatePassword(newPassword)?.await()
        } catch (ex: FirebaseAuthWeakPasswordException) {
            throw UserRepo.WeakPasswordException()
        } catch (ex: FirebaseAuthException) {
            throw UserRepo.UndefinedAuthException()
        }
        return@withContext
    }

    override suspend fun getCurrUser(): User? = withContext(Dispatchers.IO) {
        return@withContext auth.currentUser?.let { firebaseUser ->
            User(
                entityId = firebaseUser.uid,
                email = firebaseUser.email!!
            )
        }
    }
}