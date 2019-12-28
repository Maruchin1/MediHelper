package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.getCurrUserId
import com.maruchin.medihelper.domain.entities.AuthResult
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserAuthRepo
import kotlinx.coroutines.tasks.await

class UserAuthRepoImpl(
    private val auth: FirebaseAuth
) : UserAuthRepo {

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
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

    override suspend fun signUp(email: String, password: String): AuthResult {
        return try {
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

    override suspend fun signOut() {
        auth.signOut()
    }

    override suspend fun changePassword(newPassword: String) {
        auth.currentUser?.updatePassword(newPassword)
    }

    override suspend fun getCurrUser(): User? {
        return auth.currentUser?.let { firebaseUser ->
            User(
                entityId = firebaseUser.uid,
                email = firebaseUser.email!!
            )
        }
    }
}