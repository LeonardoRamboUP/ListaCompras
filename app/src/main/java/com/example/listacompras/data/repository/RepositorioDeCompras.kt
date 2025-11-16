package com.example.listacompras.data.repository

import com.example.listacompras.data.ItemDeCompra
import com.example.listacompras.data.ListaDeCompras
import kotlinx.coroutines.flow.Flow

/**
 * Interface que define o contrato para o repositório de dados.
 */
interface RepositorioDeCompras {

    // --- Funções para a Tela Principal (Lista de Listas) ---

    /** Retorna um Flow com todas as listas de compras. */
    fun obterListas(): Flow<List<ListaDeCompras>>

    /** Adiciona uma nova lista de compras (ex: "Supermercado"). */
    suspend fun adicionarLista(nomeDaLista: String)

    /** Deleta uma lista de compras inteira. */
    suspend fun deletarLista(listaId: String)


    // --- Funções para a Tela de Detalhes (Itens de uma lista) ---

    /** Retorna um Flow com os itens de UMA lista de compras específica. */
    fun obterItensDaLista(listaId: String): Flow<List<ItemDeCompra>>

    /** Adiciona um item a uma lista de compras específica. */
    suspend fun adicionarItem(listaId: String, nomeDoItem: String, quantidadeStr: String)

    /** Alterna o status de um item em uma lista específica. */
    suspend fun alternarStatusDeComprado(listaId: String, item: ItemDeCompra)

    /** Deleta um item de uma lista específica. */
    suspend fun deletarItem(listaId: String, item: ItemDeCompra)
}