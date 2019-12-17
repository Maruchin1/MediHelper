package com.maruchin.medihelper.data.framework

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow


inline fun <reified T> DocumentReference.getFlow(): Flow<T?> {
    return callbackFlow {
        this@getFlow.addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
            if (documentSnapshot != null) {
                val document = documentSnapshot.toObject(T::class.java)
                offer(document)
            }
        }
    }
}

inline fun <reified T> CollectionReference.getFlow(): Flow<List<T>> {
    return callbackFlow {
        this@getFlow.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (querySnapshot != null) {
                val list = querySnapshot.toObjects(T::class.java)
                if (!isClosedForSend) {
                    offer(list)
                }
            }
        }
    }
}

fun DocumentReference.getDocumentLive(): LiveData<DocumentSnapshot> {
    val result = MutableLiveData<DocumentSnapshot>()
    addSnapshotListener { snapshot, exception ->
        if (snapshot != null) {
            result.postValue(snapshot)
        }
    }
    return result
}

fun CollectionReference.getDocumentsLive(): LiveData<List<DocumentSnapshot>> {
    val result = MutableLiveData<List<DocumentSnapshot>>()
    addSnapshotListener { snapshot, exception ->
        if (snapshot != null) {
            result.postValue(snapshot.documents)
        }
    }
    return result
}

fun Query.getDocumenstLive(): LiveData<List<DocumentSnapshot>> {
    val result = MutableLiveData<List<DocumentSnapshot>>()
    addSnapshotListener { snapshot, exception ->
        if (snapshot != null) {
            result.postValue(snapshot.documents)
        }
    }
    return result
}

fun FirebaseAuth.getCurrUserId() = this.currentUser?.uid ?: throw FirebaseAuthException("", "")