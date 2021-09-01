package com.kirillemets.kirillyemetsnotes.model.network.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val _user: MutableStateFlow<User?> = MutableStateFlow(null)
    var user: StateFlow<User?> = _user

    private val auth = Firebase.auth

    private val authStateListener = FirebaseAuth.AuthStateListener {
        it.currentUser?.let { firebaseUser ->
            _user.value = User(
                name = firebaseUser.displayName ?: "",
                uid = firebaseUser.uid,
                isAnon = firebaseUser.isAnonymous
            )
        } ?: run {
            auth.signInAnonymously()
        }
    }


    init {
        auth.addAuthStateListener(authStateListener)
    }

    fun signOut() {
        auth.signOut()
    }

    override fun onCleared() {
        auth.removeAuthStateListener(authStateListener)
    }
}