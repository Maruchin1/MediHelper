package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.*
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.model.SignInErrors
import com.maruchin.medihelper.domain.model.SignUpErrors
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserAuthRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : UserAuthRepo {

    private val collectionRef: CollectionReference
        get() = db.collection("users")

    override suspend fun signIn(email: String, password: String, errors: SignInErrors): String? = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.signInWithEmailAndPassword(email, password).await()
            val userId = auth.getCurrUserId()
            collectionRef.document(userId)
            userId
        } catch (ex: FirebaseAuthInvalidUserException) {
            errors.incorrectEmail = true
            null
        } catch (ex: FirebaseAuthInvalidCredentialsException) {
            errors.incorrectPassword = true
            null
        } catch (ex: FirebaseAuthException) {
            errors.undefinedError = true
            null
        }
    }

    override suspend fun signUp(email: String, password: String, errors: SignUpErrors): String? = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.createUserWithEmailAndPassword(email, password).await()

            auth.getCurrUserId()
        } catch (ex: FirebaseAuthEmailException) {
            errors.incorrectEmail = true
            null
        } catch (ex: FirebaseAuthUserCollisionException) {
            errors.userAlreadyExists = true
            null
        } catch (ex: FirebaseAuthWeakPasswordException) {
            errors.weakPassword = true
            null
        } catch (ex: FirebaseAuthException) {
            errors.undefinedError = true
            null
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