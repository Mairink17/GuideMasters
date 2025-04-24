package br.com.fiap.db1.guidemasters.repository

import br.com.fiap.db1.guidemasters.constantes.Constantes
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


fun criarRetornarOpcoesMentoria( callback: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val listaMentoriaRef = db.collection(Constantes.COLLECTION_MENTORIA)
        .document(Constantes.CAMPO_MENTORIA_OPCOES_MENTORIA)
    val listaPadrao = listOf(
        "Desenvolvimento de carreira",
        "Mudança de área",
        "Construção de habilidades específicas",
        "Networking e relacionamentos profissionais",
        "Planejamento e estratégia de carreira",
        "Equilíbrio entre vida pessoal e profissional",
        "Empreendedorismo e desenvolvimento de negócios",
        "Liderança e gestão de equipes",
        "Desenvolvimento web",
        "Desenvolvimento mobile",
        "Desenvolvimento de aplicativos nativos",
        "Desenvolvimento de aplicativos híbridos",
        "Desenvolvimento de jogos",
        "Desenvolvimento de software embarcado",
        "Inteligência artificial e machine learning",
        "Análise de dados e big data",
        "Segurança da informação",
        "DevOps e infraestrutura como código",
        "Cloud computing e arquitetura de nuvem",
        "Blockchain e criptomoedas",
        "Realidade aumentada e virtual",
        "Automação de processos e robótica",
        "Desenvolvimento de APIs e microserviços",
        "Java",
        "Kotlin",
        "C",
        "C++",
        "C#",
        "Python",
        "JavaScript",
        "TypeScript",
        "Ruby",
        "Swift",
        "Objective-C",
        "PHP",
        "Rust",
        "Go",
        "Scala",
        "R",
        "SQL",
        "Haskell",
        "Lua",
        "Perl",
        "Shell Script",
        "Dart",
        "Assembly"
    )

    listaMentoriaRef.get().addOnCompleteListener(OnCompleteListener<DocumentSnapshot> { task ->
        if (task.isSuccessful) {
            val document = task.result
            if (document != null && document.exists()) {
                val lista = document.get(Constantes.CAMPO_MENTORIA_OPCAO) as? List<String>
                if (lista != null) {
                    callback(lista)
                    return@OnCompleteListener
                }
            }
        }

        listaMentoriaRef.set(mapOf(Constantes.CAMPO_MENTORIA_OPCAO to listaPadrao))
            .addOnCompleteListener { callback(listaPadrao) }
    })
}
