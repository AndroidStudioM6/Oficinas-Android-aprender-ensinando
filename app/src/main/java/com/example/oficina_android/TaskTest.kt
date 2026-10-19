
package com.example.oficina_android

import org.junit.Assert.assertFalse
import org.junit.Test

class TaskTest {

    @Test
    fun tarefaIniciaNaoConcluida() {
        val tarefa = Task(title = "Estudar Kotlin")

        assertFalse(tarefa.isCompleted)
    }
}
