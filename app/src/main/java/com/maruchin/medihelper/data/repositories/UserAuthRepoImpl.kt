package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.domain.entities.AuthResult
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserAuthRepoImpl(
    private val auth: FirebaseAuth
) : UserAuthRepo {

    override suspend fun signIn(email: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(userId = auth.getCurrUserId())
        } catch (ex: FirebaseAuthInvalidUserException) {
            AuthResult.Error(message = "Niepoprawny adres e-mail")
        } catch (ex: FirebaseAuthInvalidCredentialsException) {
            AuthResult.Error(message = "Niepoprawne hasło")
        } catch (ex: FirebaseAuthException) {
            AuthResult.Error(message = "Błąd logowania")
        }
    }

    override suspend fun signUp(email: String, password: String): AuthResult = withContext(Dispatchers.IO) {
        return@withContext try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthResult.Success(userId = auth.getCurrUserId())
        } catch (ex: FirebaseAuthEmailException) {
            AuthResult.Error(message = "Niepoprawny adres e-mail")
        } catch (ex: FirebaseAuthUserCollisionException) {
            AuthResult.Error(message = "Użytkownik o podanym adresie e-mail już istnieje")
        } catch (ex: FirebaseAuthWeakPasswordException) {
            AuthResult.Error(message = "Hasło jest zbyt słabe")
        } catch (ex: FirebaseAuthException) {
            AuthResult.Error(message = "Błąd rejestracji")
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