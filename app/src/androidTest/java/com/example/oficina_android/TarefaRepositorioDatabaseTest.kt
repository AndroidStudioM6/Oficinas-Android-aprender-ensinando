package com.example.oficina_android

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import java.util.UUID
import com.google.android.material.textfield.TextInputEditText

@RunWith(AndroidJUnit4::class)
class TarefaRepositorioDatabaseTest {

    @Test
    fun componentsUiFormSavesTaskToRoom() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        TarefaRepositorio.inicializar(context)
        val title = "form-${UUID.randomUUID()}"
        val scenario = ActivityScenario.launch(MainActivity::class.java)

        scenario.onActivity { it.selectPage(1) }
        InstrumentationRegistry.getInstrumentation().waitForIdleSync()
        scenario.onActivity { activity ->
            activity.findViewById<TextInputEditText>(R.id.etTitle).setText(title)
            activity.findViewById<TextInputEditText>(R.id.etDescription).setText("Descrição do teste")
            activity.findViewById<TextInputEditText>(R.id.etCategory).setText("Categoria de teste")
            activity.findViewById<android.widget.Button>(R.id.btnSaveTask).performClick()
        }

        val saved = withTimeout(10_000) {
            while (true) {
                TarefaRepositorio.listarTodas().firstOrNull { it.titulo == title }?.let { return@withTimeout it }
                delay(50)
            }
            error("Tarefa não foi salva")
        }
        assertTrue(saved.descricao == "Descrição do teste")
        assertTrue(saved.categoria == "Categoria de teste")
        assertTrue(saved.prioridade == Prioridade.ALTA)
        assertTrue(saved.dataCriacao > 0)
        TarefaRepositorio.remover(saved)
        scenario.close()
    }

    @Test
    fun roomPreservesTasksAndSupportsSearchAndCrud() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        TarefaRepositorio.inicializar(context)

        val existing = TarefaRepositorio.listarTodas().map { it.id to it.titulo }.toSet()
        val title = "smoke-${UUID.randomUUID()}"
        TarefaRepositorio.adicionar(
            Tarefa(titulo = title, categoria = "Verificação", prioridade = Prioridade.ALTA)
        )

        val inserted = TarefaRepositorio.listarTodas().single { it.titulo == title }
        assertTrue(TarefaRepositorio.buscarPorTexto(title).any { it.id == inserted.id })
        assertTrue(
            TarefaRepositorio.filtrarPorCategoria("verificação").any { it.id == inserted.id }
        )
        assertTrue(
            TarefaRepositorio.filtrarPorPrioridade(Prioridade.ALTA).any { it.id == inserted.id }
        )

        TarefaRepositorio.atualizar(inserted.copy(concluida = true))
        assertTrue(TarefaRepositorio.listarConcluidas().any { it.id == inserted.id })
        TarefaRepositorio.remover(inserted.copy(concluida = true))

        val remaining = TarefaRepositorio.listarTodas()
        assertFalse(remaining.any { it.id == inserted.id })
        assertTrue(
            "The test must preserve every task that existed before it started",
            existing.all { (id, taskTitle) -> remaining.any { it.id == id && it.titulo == taskTitle } }
        )
    }
}
