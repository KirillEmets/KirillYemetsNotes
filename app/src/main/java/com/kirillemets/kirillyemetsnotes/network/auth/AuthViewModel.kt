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
        viewModelScope.launch(Dispatchers.IO) {
            firebaseUser.value = auth.currentUser
        }
    }

    fun onSignIn(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            firebaseUser.value = auth.currentUser
        }
    }
}