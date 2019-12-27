package com.maruchin.medihelper.data.repositories

import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import com.maruchin.medihelper.data.framework.FirestoreRepo
import com.maruchin.medihelper.data.mappers.UserMapper
import com.maruchin.medihelper.domain.entities.AuthResult
import com.maruchin.medihelper.domain.entities.User
import com.maruchin.medihelper.domain.repositories.UserRepo
import kotlinx.coroutines.tasks.await

class UserRepoImpl(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val mapper: UserMapper
) : FirestoreRepo<User>(
    collectionRef = db.collection("users"),
    mapper = mapper
), UserRepo {

    override suspend fun signIn(email: String, password: String): AuthResult {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthResult.Success(userId = getCurrUserId()!!)
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
            AuthResult.Success(userId = getCurrUserId()!!)
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

    override suspend fun getCurrUserId(): String? {
        return auth.currentUser?.uid
    }
}