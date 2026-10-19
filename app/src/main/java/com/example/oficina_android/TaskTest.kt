
package com.example.oficina_android

import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskTest {

    @Test
    fun tarefaIniciaNaoConcluida() {
        val tarefa = Task(title = "Estudar Kotlin")

        assertFalse(tarefa.isCompleted)
    }

    @Test
    fun tituloDaTarefaEstaCorreto() {
        val tarefa = Task(title = "Estudar Kotlin")

        assertEquals("Estudar Kotlin", tarefa.title)
    }

    @Test
    fun tarefaPodeSerConcluida() {
        val tarefa = Task(title = "Estudar Kotlin")

        tarefa.isCompleted = true

        assertTrue(tarefa.isCompleted)
    }
}
