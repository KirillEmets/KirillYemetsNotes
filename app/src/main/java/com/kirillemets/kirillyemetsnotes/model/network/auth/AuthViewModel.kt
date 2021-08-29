package com.kirillemets.kirillyemetsnotes.model.network.auth

import android.app.Activity.RESULT_OK
import androidx.lifecycle.ViewModel
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transform

class AuthViewModel : ViewModel() {
    private val firebaseUser: MutableStateFlow<FirebaseUser?> = MutableStateFlow(null)
    var user = firebaseUser.transform { fbUser ->
        emit(
            fbUser?.let {
                User(
                    name = fbUser.displayName ?: "",
                    uid = fbUser.uid
                )
            }
        )
    }
    val onSignIn: MutableStateFlow<Boolean> = MutableStateFlow(false)

    private val auth = Firebase.auth

    init {
        auth.addAuthStateListener { firebaseUser.value = it.currentUser }
    }

    fun onSignIn(result: FirebaseAuthUIAuthenticationResult) {
        if(result.resultCode == RESULT_OK) {
            onSignIn.value = true
        }

    }

    fun signOut() {
        auth.signOut()
    }
}