package br.com.fiap.db1.guidemasters.repository

import com.google.firebase.auth.FirebaseAuth

fun isUserLoggedIn(): Boolean {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    return user != null
}
