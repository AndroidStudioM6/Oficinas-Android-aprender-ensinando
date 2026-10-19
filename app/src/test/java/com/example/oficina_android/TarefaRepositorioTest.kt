package com.example.oficina_android

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TarefaRepositorioTest {

    private lateinit var repositorio: TarefaRepositorio

    @Before
    fun setup() {
        repositorio = TarefaRepositorio()

        repositorio.adicionarTodas(
            listOf(
                Tarefa(id = 1, titulo = "Estudar Kotlin", descricao = "Revisar Coleções", prioridade = Prioridade.ALTA, concluida = false, categoria = "Estudos"),
                Tarefa(id = 2, titulo = "Comprar pão", descricao = "Padaria da esquina", prioridade = Prioridade.BAIXA, concluida = true, categoria = "Pessoal"),
                Tarefa(id = 3, titulo = "Fazer exercícios de Android", descricao = "RecyclerView e Repositorio", prioridade = Prioridade.ALTA, concluida = false, categoria = "Estudos"),
                Tarefa(id = 4, titulo = "Limpar a casa", descricao = "Quarto e sala", prioridade = Prioridade.MEDIA, concluida = true, categoria = "Casa"),
                Tarefa(id = 5, titulo = "Pagar conta de luz", descricao = "Vencimento hoje", prioridade = Prioridade.ALTA, concluida = false, categoria = "Finanças")
            )
        )
    }

    @Test
    fun testAdicionarERemoverTarefa() {
        val novaTarefa = Tarefa(id = 6, titulo = "Nova Tarefa", prioridade = Prioridade.MEDIA)
        repositorio.adicionar(novaTarefa)

        assertEquals(6, repositorio.contarTotal())
        assertNotNull(repositorio.buscarPorId(6))

        val removido = repositorio.remover(novaTarefa)
        assertTrue(removido)
        assertEquals(5, repositorio.contarTotal())
    }

    @Test
    fun testAtualizarTarefa() {
        val tarefaAlterada = Tarefa(id = 1, titulo = "Estudar Kotlin Avançado", prioridade = Prioridade.ALTA, concluida = true, categoria = "Estudos")
        val atualizou = repositorio.atualizar(tarefaAlterada)

        assertTrue(atualizou)
        val tarefaDoBanco = repositorio.buscarPorId(1)
        assertEquals("Estudar Kotlin Avançado", tarefaDoBanco?.titulo)
        assertTrue(tarefaDoBanco?.concluida == true)
    }

    @Test
    fun testFiltrosPendentesEConcluidas() {
        val pendentes = repositorio.listarPendentes()
        val concluidas = repositorio.listarConcluidas()

        assertEquals(3, pendentes.size)
        assertEquals(2, concluidas.size)
        assertTrue(pendentes.all { !it.concluida })
        assertTrue(concluidas.all { it.concluida })
    }

    @Test
    fun testFiltrarPorPrioridade() {
        val altaPrioridade = repositorio.filtrarPorPrioridade(Prioridade.ALTA)
        assertEquals(3, altaPrioridade.size)
        assertTrue(altaPrioridade.all { it.prioridade == Prioridade.ALTA })
    }

    @Test
    fun testFiltrarPorCategoriaEBuscarTexto() {
        val estudos = repositorio.filtrarPorCategoria("Estudos")
        assertEquals(2, estudos.size)

        val resultadoBusca = repositorio.buscarPorTexto("luz")
        assertEquals(1, resultadoBusca.size)
        assertEquals("Pagar conta de luz", resultadoBusca.first().titulo)
    }

    @Test
    fun testExtrairTitulosECategoriasUnicas() {
        val titulos = repositorio.extrairTitulos()
        assertEquals(5, titulos.size)
        assertTrue(titulos.contains("Estudar Kotlin"))

        val categorias = repositorio.obterCategoriasUnicas()
        assertEquals(4, categorias.size) // Estudos, Pessoal, Casa, Finanças
        assertTrue(categorias.containsAll(setOf("Estudos", "Pessoal", "Casa", "Finanças")))
    }

    @Test
    fun testAgruparPorPrioridadeECategoria() {
        val agruparPrioridade = repositorio.agruparPorPrioridade()
        assertEquals(3, agruparPrioridade[Prioridade.ALTA]?.size)
        assertEquals(1, agruparPrioridade[Prioridade.MEDIA]?.size)
        assertEquals(1, agruparPrioridade[Prioridade.BAIXA]?.size)

        val agruparCategoria = repositorio.agruparPorCategoria()
        assertEquals(2, agruparCategoria["Estudos"]?.size)
    }

    @Test
    fun testOrdenacao() {
        val ordenadasPorTitulo = repositorio.ordenarPorTitulo()
        assertEquals("Comprar pão", ordenadasPorTitulo.first().titulo)

        val ordenadasPorPrioridade = repositorio.ordenarPorPrioridade()
        assertEquals(Prioridade.ALTA, ordenadasPorPrioridade.first().prioridade)
    }

    @Test
    fun testContagemEAgrupamento() {
        assertEquals(5, repositorio.contarTotal())
        assertEquals(3, repositorio.contarPendentes())
        assertEquals(2, repositorio.contarConcluidas())
        assertEquals(3, repositorio.contarPorPrioridade(Prioridade.ALTA))

        val contagemPorCat = repositorio.contarPorCategoria()
        assertEquals(2, contagemPorCat["Estudos"])
        assertEquals(1, contagemPorCat["Pessoal"])
    }
}
