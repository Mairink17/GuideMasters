package br.com.fiap.db1.guidemasters.screenViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TelaCadastroScreenViewModel : ViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _senha = MutableLiveData<String>()
    val senha: LiveData<String> = _senha

    private val _cpf = MutableLiveData<String>()
    val cpf: LiveData<String> = _cpf

    private val _dataNascimento = MutableLiveData<Long>()
    val dataNascimento: LiveData<Long> = _dataNascimento

    private val _experiencia = MutableLiveData<String>()
    val experiencia: LiveData<String> = _experiencia

    private val _linkedin = MutableLiveData<String>()
    val linkedin: LiveData<String> = _linkedin

    private val _nome = MutableLiveData<String>()
    val nome: LiveData<String> = _nome

    fun onNomeChanged(nome : String){
        _nome.value = nome
    }

    fun onEmailChanged(email: String){
        _email.value = email
    }

    fun onSenhaChanged(senha: String){
        _senha.value = senha
    }

    fun onCpfChanged(cpf: String){
        _cpf.value = cpf
    }

    fun onDataNascimentoChange(dataNascimento: Long ){
        _dataNascimento.value = dataNascimento
    }

    fun onExperienciaChanged(experiencia: String){
        _experiencia.value = experiencia
    }

    fun onLinkedinChanged(linkedin : String){
        _linkedin.value = linkedin
    }

}
