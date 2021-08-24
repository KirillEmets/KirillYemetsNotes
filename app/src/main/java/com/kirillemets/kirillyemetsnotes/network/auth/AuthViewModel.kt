package com.kirillemets.kirillyemetsnotes.network.auth

import android.app.Activity.RESULT_OK
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kirillemets.kirillyemetsnotes.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val firebaseUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    var user = firebaseUser.transform { fbUser ->
        emit(
            fbUser?.let {
                User(
                    name = fbUser.displayName ?: ""
                )
            }
        )
    }
    private val auth = Firebase.auth

    init {
        auth.addAuthStateListener { firebaseUser.value = it.currentUser }
    }

    fun onSignIn(result: FirebaseAuthUIAuthenticationResult) {
        // Does nothing
        result.resultCode
    }

    fun signOut() {
        viewModelScope.launch(Dispatchers.IO) {
            auth.signOut()
        }
    }
}