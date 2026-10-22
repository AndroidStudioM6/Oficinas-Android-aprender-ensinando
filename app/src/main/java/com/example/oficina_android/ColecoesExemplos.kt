package com.example.oficina_android

import android.util.Log

/**
 * Classe utilitária contendo exemplos práticos e didáticos sobre Coleções em Kotlin.
 * Pode ser executada no Android (ex: Logcat) ou em testes automatizados.
 */
object ColecoesExemplos {

    private const val TAG = "COLECOES"

    /**
     * Demonstrar a diferença entre List, Set e Map (imutáveis vs mutáveis).
     */
    fun demonstrarTiposDeColecoes() {
        Log.d(TAG, "=== DEMONSTRAÇÃO DE LIST, SET E MAP ===")

        // List: permite elementos duplicados e mantém a ordem de inserção
        val listaNomes: List<String> = listOf("Ana", "João", "Ana", "Carlos")
        Log.d(TAG, "List (permite duplicados): $listaNomes")

        // Set: garante que todos os elementos sejam únicos (elimina repetições)
        val conjuntoNomes: Set<String> = setOf("Ana", "João", "Ana", "Carlos")
        Log.d(TAG, "Set (elementos únicos): $conjuntoNomes")

        // Map: armazena pares de Chave -> Valor (chaves únicas)
        val mapaNotas: Map<String, Int> = mapOf(
            "Ana" to 9,
            "João" to 7,
            "Carlos" to 8,
        )
        Log.d(TAG, "Map (Chave to Valor): $mapaNotas")

        // Coleção Mutável: permite adicionar, remover e modificar dados
        val frutasMutaveis: MutableList<String> = mutableListOf("Maçã", "Banana")
        frutasMutaveis.add("Uva")
        frutasMutaveis.remove("Banana")
        Log.d(TAG, "MutableList após add e remove: $frutasMutaveis")
    }

    /**
     * Demonstrar funções de alta ordem (filter, map, count, groupBy, sortedBy, distinct).
     */
    fun demonstrarOperacoesFuncionais() {
        Log.d(TAG, "=== OPERAÇÕES FUNCIONAIS EM COLEÇÕES ===")

        val notas = listOf(5, 8, 7, 6, 9, 10, 4)

        // 1. filter: filtra elementos com base em uma condição
        val aprovados = notas.filter { it >= 7 }
        Log.d(TAG, "filter (notas >= 7): $aprovados")

        // 2. map: transforma cada elemento em um novo valor
        val notasComBonus = notas.map { it + 1 }
        Log.d(TAG, "map (com +1 de bônus): $notasComBonus")

        // 3. count: conta quantos elementos satisfazem a condição
        val qtdAprovados = notas.count { it >= 7 }
        Log.d(TAG, "count (aprovados): $qtdAprovados")

        // 4. sortedBy / sorted: ordena a coleção
        val notasOrdenadas = notas.sorted()
        Log.d(TAG, "sorted (ordem crescente): $notasOrdenadas")

        // 5. distinct: remove elementos repetidos de uma coleção
        val numerosComRepeticao = listOf(1, 2, 2, 3, 4, 4, 5)
        val numerosUnicos = numerosComRepeticao.distinct()
        Log.d(TAG, "distinct: $numerosUnicos")

        // 6. groupBy: agrupa elementos segundo uma regra
        val nomes = listOf("Ana", "Amanda", "Bruno", "Carlos", "Beatriz")
        val agrupadosPorInicial = nomes.groupBy { it.first() }
        Log.d(TAG, "groupBy (por letra inicial): $agrupadosPorInicial")
    }
}
