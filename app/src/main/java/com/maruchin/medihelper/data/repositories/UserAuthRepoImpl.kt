package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.data.utils.AppFirebase
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.model.SignInErrors
import com.maruchin.medihelper.domain.model.SignUpErrors
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserAuthRepoImpl(
    private val appFirebase: AppFirebase,
    private val auth: FirebaseAuth
) : UserAuthRepo {

    private val collectionRef: CollectionReference
        get() = appFirebase.users

    override suspend fun signIn(email: String, password: String): String = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.signInWithEmailAndPassword(email, password).await()
            auth.getCurrUserId()
        } catch (ex: FirebaseAuthInvalidUserException) {
            throw UserAuthRepo.IncorrectEmailException()
        } catch (ex: FirebaseAuthInvalidCredentialsException) {
            throw UserAuthRepo.IncorrectPasswordException()
        } catch (ex: FirebaseAuthException) {
            throw  UserAuthRepo.UndefinedAuthException()
        }
    }

    override suspend fun signUp(email: String, password: String): String = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val userId = auth.getCurrUserId()
            collectionRef.document(userId)
            userId
        } catch (ex: FirebaseAuthEmailException) {
            throw UserAuthRepo.IncorrectEmailException()
        } catch (ex: FirebaseAuthUserCollisionException) {
            throw UserAuthRepo.UserAlreadyExistException()
        } catch (ex: FirebaseAuthWeakPasswordException) {
            throw UserAuthRepo.WeakPasswordException()
        } catch (ex: FirebaseAuthException) {
            throw UserAuthRepo.UndefinedAuthException()
        }
    }

    override suspend fun signOut() = withContext(Dispatchers.IO) {
        auth.signOut()
        return@withContext
    }

    override suspend fun changePassword(newPassword: String) = withContext(Dispatchers.IO) {
        auth.currentUser?.updatePassword(newPassword)
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