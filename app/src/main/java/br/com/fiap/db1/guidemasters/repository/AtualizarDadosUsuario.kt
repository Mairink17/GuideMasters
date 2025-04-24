package br.com.fiap.db1.guidemasters.repository

import android.content.Context
import android.widget.Toast
import br.com.fiap.db1.guidemasters.R
import br.com.fiap.db1.guidemasters.constantes.Constantes
import br.com.fiap.db1.guidemasters.model.Perfil
import com.google.firebase.firestore.FirebaseFirestore

fun atualizarDadosUsuario(
    perfil: Perfil,
    idUsuarioLogado: String?,
    context : Context
) {
    val mensagemSucesso = context.getString(R.string.mensagem_atualiza_cadastro_sucesso)
    val mensagemErro = context.getString(R.string.mensagem_atualiza_cadastro_erro)


    idUsuarioLogado?.let { id ->
        val dados = mutableMapOf<String, Any?>()

        perfil.experiencia.let { experiencia ->
            dados[Constantes.CAMPO_EXPERIENCIA] = experiencia
        }

        perfil.linkedin.let { linkedin ->
            dados[Constantes.CAMPO_LINKEDIN] = linkedin
        }

        perfil.opcoesSelecionadas?.let { opcoesSelecionadas ->
            dados[Constantes.CAMPO_OPCOES_MENTORIA] = opcoesSelecionadas.toList()
        }

        /*
        perfil.urlPerfil?.let { urlPerfil ->
            dados[Constantes.CAMPO_CAMINHO_FOTO] = urlPerfil
        }

         */

        perfil.formacaoAcademica.let { formacaoAcademica ->
            dados[Constantes.CAMPO_FORMACAO_ACADEMICA] = formacaoAcademica
        }

        perfil.grauInstrucao.let { grauInstrucao ->
            dados[Constantes.CAMPO_GRAU_INSTRUCAO] = grauInstrucao
        }

        val bancoDados by lazy {
            FirebaseFirestore.getInstance()
        }

        bancoDados
            .collection(Constantes.COLLECTION_PERFIL)
            .document(id)
            .update(dados)
            .addOnSuccessListener {
                Toast.makeText(context, mensagemSucesso, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, mensagemErro, Toast.LENGTH_SHORT).show()
            }
    }
}
