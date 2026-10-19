package com.example.oficina_android

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TarefasRepositorioTest {

    @Before
    fun setUp() {
        TarefasRepositorio.limparTudo()
    }

    @Test
    fun testAdicionarEGerarId() {
        val t1 = TarefasRepositorio.adicionar(Tarefa(titulo = "Tarefa 1"))
        val t2 = TarefasRepositorio.adicionar(Tarefa(titulo = "Tarefa 2"))

        assertEquals(1, t1.id)
        assertEquals(2, t2.id)
        assertEquals(2, TarefasRepositorio.listarTodas().size)
    }

    @Test
    fun testRemoverTarefa() {
        val t1 = TarefasRepositorio.adicionar(Tarefa(titulo = "Tarefa 1"))
        val t2 = TarefasRepositorio.adicionar(Tarefa(titulo = "Tarefa 2"))

        TarefasRepositorio.remover(t1)

        val todas = TarefasRepositorio.listarTodas()
        assertEquals(1, todas.size)
        assertEquals(t2.id, todas[0].id)
    }

    @Test
    fun testAtualizarTarefa() {
        val t1 = TarefasRepositorio.adicionar(Tarefa(titulo = "Original", concluida = false))
        val t1Atualizada = t1.copy(titulo = "Atualizada", concluida = true)

        TarefasRepositorio.atualizar(t1Atualizada)

        val todas = TarefasRepositorio.listarTodas()
        assertEquals(1, todas.size)
        assertEquals("Atualizada", todas[0].titulo)
        assertTrue(todas[0].concluida)
    }

    @Test
    fun testListarPendentesEConcluidas() {
        TarefasRepositorio.adicionar(Tarefa(titulo = "Pendente 1", concluida = false))
        TarefasRepositorio.adicionar(Tarefa(titulo = "Concluida 1", concluida = true))
        TarefasRepositorio.adicionar(Tarefa(titulo = "Pendente 2", concluida = false))

        val pendentes = TarefasRepositorio.listarPendentes()
        val concluidas = TarefasRepositorio.listarConcluidas()

        assertEquals(2, pendentes.size)
        assertEquals(1, concluidas.size)
        assertFalse(pendentes[0].concluida)
        assertTrue(concluidas[0].concluida)
    }

    @Test
    fun testOrdenarPorPrioridade() {
        TarefasRepositorio.adicionar(Tarefa(titulo = "Baixa", prioridade = Prioridade.BAIXA))
        TarefasRepositorio.adicionar(Tarefa(titulo = "Alta", prioridade = Prioridade.ALTA))
        TarefasRepositorio.adicionar(Tarefa(titulo = "Media", prioridade = Prioridade.MEDIA))

        val ordenadas = TarefasRepositorio.ordenarPorPrioridade()

        assertEquals(Prioridade.ALTA, ordenadas[0].prioridade)
        assertEquals(Prioridade.MEDIA, ordenadas[1].prioridade)
        assertEquals(Prioridade.BAIXA, ordenadas[2].prioridade)
    }

    @Test
    fun testContarPorPrioridade() {
        TarefasRepositorio.adicionar(Tarefa(titulo = "T1", prioridade = Prioridade.ALTA))
        TarefasRepositorio.adicionar(Tarefa(titulo = "T2", prioridade = Prioridade.ALTA))
        TarefasRepositorio.adicionar(Tarefa(titulo = "T3", prioridade = Prioridade.BAIXA))

        val contagem = TarefasRepositorio.contarPorPrioridade()

        assertEquals(2, contagem[Prioridade.ALTA])
        assertEquals(0, contagem[Prioridade.MEDIA])
        assertEquals(1, contagem[Prioridade.BAIXA])
    }

    @Test
    fun testLimparConcluidas() {
        TarefasRepositorio.adicionar(Tarefa(titulo = "Pendente", concluida = false))
        TarefasRepositorio.adicionar(Tarefa(titulo = "Concluida", concluida = true))

        TarefasRepositorio.limparConcluidas()

        val todas = TarefasRepositorio.listarTodas()
        assertEquals(1, todas.size)
        assertEquals("Pendente", todas[0].titulo)
    }

    @Test
    fun testLimparTudo() {
        TarefasRepositorio.adicionar(Tarefa(titulo = "T1"))
        TarefasRepositorio.adicionar(Tarefa(titulo = "T2"))

        TarefasRepositorio.limparTudo()

        assertEquals(0, TarefasRepositorio.listarTodas().size)

        val nova = TarefasRepositorio.adicionar(Tarefa(titulo = "Nova"))
        assertEquals(1, nova.id)
    }
}
