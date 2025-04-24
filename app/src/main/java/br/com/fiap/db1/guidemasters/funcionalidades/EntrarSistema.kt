package br.com.fiap.db1.guidemasters.funcionalidades

import com.google.firebase.auth.FirebaseAuth

fun fazerLogin(email: String, senha: String,  onLoginResult: (Boolean) -> Unit) {
    try {
        val auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                onLoginResult(true)
            }
            .addOnFailureListener {
                onLoginResult(false)
            }
    } catch (e: Exception) {
        onLoginResult(false)
    }
}