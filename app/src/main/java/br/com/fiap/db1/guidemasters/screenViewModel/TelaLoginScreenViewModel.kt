package br.com.fiap.db1.guidemasters.screenViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TelaLoginScreenViewModel : ViewModel(){

    private val _senha = MutableLiveData<String>()
    val senha : LiveData<String> = _senha

    private val _login = MutableLiveData<String>()
    val login : LiveData<String> = _login


    fun onSenhaChanged(senha: String){
        _senha.value = senha
    }

    fun onLoginChanged(login: String){
        _login.value = login
    }


}

