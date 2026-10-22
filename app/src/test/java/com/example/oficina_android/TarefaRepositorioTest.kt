package com.example.oficina_android

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TarefaRepositorioTest {

    @Before
    fun limpar() {
        TarefaRepositorio.limparTudo() // começa cada teste do zero
    }

    @Test
    fun pendentesNaoIncluemConcluidas() {
        // preparar
        TarefaRepositorio.adicionar(Tarefa(titulo = "Pendente"))
        TarefaRepositorio.adicionar(Tarefa(titulo = "Feita", concluida = true))
        // agir
        val pendentes = TarefaRepositorio.listarPendentes()
        // verificar
        assertEquals(1, pendentes.size)
        assertEquals("Pendente", pendentes[0].titulo)
    }

    @Test
    fun ordenarColocaAltaPrimeiro() {
        TarefaRepositorio.adicionar(
            Tarefa(titulo = "B", prioridade = Prioridade.BAIXA),
        )
        TarefaRepositorio.adicionar(
            Tarefa(titulo = "A", prioridade = Prioridade.ALTA),
        )
        val primeira = TarefaRepositorio.ordenarPorPrioridade().first()
        assertEquals(Prioridade.ALTA, primeira.prioridade)
    }

    @Test
    fun comportamentoComListaVazia() {
        assertEquals(0, TarefaRepositorio.contarTotal())
        assertTrue(TarefaRepositorio.listarTodas().isEmpty())
        assertTrue(TarefaRepositorio.listarPendentes().isEmpty())
        assertTrue(TarefaRepositorio.listarConcluidas().isEmpty())
        assertTrue(TarefaRepositorio.obterCategoriasUnicas().isEmpty())
        assertNull(TarefaRepositorio.buscarPorId(1L))
    }

    @Test
    fun testAdicionarERemoverTarefa() {
        val t1 = TarefaRepositorio.adicionar(Tarefa(titulo = "Estudar Kotlin"))
        assertEquals(1, TarefaRepositorio.contarTotal())
        assertNotNull(TarefaRepositorio.buscarPorId(t1.id))

        val removido = TarefaRepositorio.remover(t1)
        assertTrue(removido)
        assertEquals(0, TarefaRepositorio.contarTotal())
    }

    @Test
    fun testRemoverPorId() {
        val t1 = TarefaRepositorio.adicionar(Tarefa(titulo = "Tarefa 1"))
        val t2 = TarefaRepositorio.adicionar(Tarefa(titulo = "Tarefa 2"))

        assertEquals(2, TarefaRepositorio.contarTotal())
        val removido = TarefaRepositorio.removerPorId(t1.id)

        assertTrue(removido)
        assertEquals(1, TarefaRepositorio.contarTotal())
        assertNull(TarefaRepositorio.buscarPorId(t1.id))
        assertNotNull(TarefaRepositorio.buscarPorId(t2.id))
    }

    @Test
    fun testAtualizarTarefa() {
        val t1 = TarefaRepositorio.adicionar(Tarefa(titulo = "Título Antigo", concluida = false))
        val tarefaAlterada = t1.copy(titulo = "Título Novo", concluida = true)

        val atualizou = TarefaRepositorio.atualizar(tarefaAlterada)
        assertTrue(atualizou)

        val tarefaDoBanco = TarefaRepositorio.buscarPorId(t1.id)
        assertEquals("Título Novo", tarefaDoBanco?.titulo)
        assertTrue(tarefaDoBanco?.concluida == true)
    }

    @Test
    fun testFiltrarPorPrioridadeECategoria() {
        TarefaRepositorio.adicionar(Tarefa(titulo = "A", prioridade = Prioridade.ALTA, categoria = "Estudos"))
        TarefaRepositorio.adicionar(Tarefa(titulo = "B", prioridade = Prioridade.BAIXA, categoria = "Pessoal"))
        TarefaRepositorio.adicionar(Tarefa(titulo = "C", prioridade = Prioridade.ALTA, categoria = "Estudos"))

        val altas = TarefaRepositorio.filtrarPorPrioridade(Prioridade.ALTA)
        assertEquals(2, altas.size)

        val estudos = TarefaRepositorio.filtrarPorCategoria("Estudos")
        assertEquals(2, estudos.size)
    }

    @Test
    fun testBuscarPorTexto() {
        TarefaRepositorio.adicionar(Tarefa(titulo = "Comprar pão", descricao = "Ir na padaria"))
        TarefaRepositorio.adicionar(Tarefa(titulo = "Pagar conta de luz", descricao = "No banco"))

        val buscaTitulo = TarefaRepositorio.buscarPorTexto("pão")
        assertEquals(1, buscaTitulo.size)

        val buscaDescricao = TarefaRepositorio.buscarPorTexto("padaria")
        assertEquals(1, buscaDescricao.size)

        val buscaInexistente = TarefaRepositorio.buscarPorTexto("futebol")
        assertTrue(buscaInexistente.isEmpty())
    }

    @Test
    fun testMapeamentosEConjuntos() {
        TarefaRepositorio.adicionar(Tarefa(titulo = "A", categoria = "Estudos"))
        TarefaRepositorio.adicionar(Tarefa(titulo = "B", categoria = "Estudos"))
        TarefaRepositorio.adicionar(Tarefa(titulo = "C", categoria = "Pessoal"))

        val titulos = TarefaRepositorio.extrairTitulos()
        assertEquals(listOf("A", "B", "C"), titulos)

        val categoriasUnicas = TarefaRepositorio.obterCategoriasUnicas()
        assertEquals(setOf("Estudos", "Pessoal"), categoriasUnicas)

        val mapaIdTitulo = TarefaRepositorio.obterMapaIdParaTitulo()
        assertEquals(3, mapaIdTitulo.size)
    }

    @Test
    fun testAgrupamentoEContagem() {
        TarefaRepositorio.adicionar(Tarefa(titulo = "A", prioridade = Prioridade.ALTA, categoria = "Estudos", concluida = false))
        TarefaRepositorio.adicionar(Tarefa(titulo = "B", prioridade = Prioridade.MEDIA, categoria = "Estudos", concluida = true))
        TarefaRepositorio.adicionar(Tarefa(titulo = "C", prioridade = Prioridade.ALTA, categoria = "Trabalho", concluida = false))

        assertEquals(3, TarefaRepositorio.contarTotal())
        assertEquals(2, TarefaRepositorio.contarPendentes())
        assertEquals(1, TarefaRepositorio.contarConcluidas())
        assertEquals(2, TarefaRepositorio.contarPorPrioridade(Prioridade.ALTA))

        val contagemCategoria = TarefaRepositorio.contarPorCategoria()
        assertEquals(2, contagemCategoria["Estudos"])
        assertEquals(1, contagemCategoria["Trabalho"])

        val agrupaPrioridade = TarefaRepositorio.agruparPorPrioridade()
        assertEquals(2, agrupaPrioridade[Prioridade.ALTA]?.size)
        assertEquals(1, agrupaPrioridade[Prioridade.MEDIA]?.size)
    }

    @Test
    fun testOrdenarPorTitulo() {
        TarefaRepositorio.adicionar(Tarefa(titulo = "Zebra"))
        TarefaRepositorio.adicionar(Tarefa(titulo = "Abacaxi"))
        TarefaRepositorio.adicionar(Tarefa(titulo = "Carro"))

        val ordenadas = TarefaRepositorio.ordenarPorTitulo()
        assertEquals("Abacaxi", ordenadas[0].titulo)
        assertEquals("Carro", ordenadas[1].titulo)
        assertEquals("Zebra", ordenadas[2].titulo)
    }
}
