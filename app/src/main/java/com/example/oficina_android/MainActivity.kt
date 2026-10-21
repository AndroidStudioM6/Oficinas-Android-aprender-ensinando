
package com.example.oficina_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    private lateinit var btnOpenTaskList: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnOpenTaskList = findViewById(R.id.btnOpenTaskList)

        btnOpenTaskList.setOnClickListener {
            val intent = Intent(this, TaskListActivity::class.java)
            startActivity(intent)
        }

        testarFuncoes()
    }

    private fun testarFuncoes() {

        Log.d("TESTE_LOGCAT", "===== INICIO DOS TESTES =====")

        val tarefaTeste = Tarefa(titulo = "Teste Logcat")
        val adicionada = TarefasRepositorio.adicionar(tarefaTeste)

        Log.d(
            "TESTE_LOGCAT",
            "Adicionar: ${adicionada.titulo == "Teste Logcat"}"
        )

        val listada = TarefasRepositorio.listarTodas()
            .any { it.id == adicionada.id }

        Log.d("TESTE_LOGCAT", "Listar: $listada")

        val atualizada = adicionada.copy(titulo = "Teste Atualizado")
        TarefasRepositorio.atualizar(atualizada)

        val atualizacaoCorreta = TarefasRepositorio.listarTodas()
            .any {
                it.id == adicionada.id &&
                        it.titulo == "Teste Atualizado"
            }

        Log.d("TESTE_LOGCAT", "Atualizar: $atualizacaoCorreta")

        TarefasRepositorio.remover(atualizada)

        val removida = TarefasRepositorio.listarTodas()
            .none { it.id == adicionada.id }

        Log.d("TESTE_LOGCAT", "Remover: $removida")

        Log.d("TESTE_LOGCAT", "===== FIM DOS TESTES =====")
    }
}
