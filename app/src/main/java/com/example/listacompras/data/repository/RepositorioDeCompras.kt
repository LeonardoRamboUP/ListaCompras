package com.example.listacompras.data.repository

import com.example.listacompras.data.ItemDeCompra
import kotlinx.coroutines.flow.Flow

/**
 * Interface que define o contrato para o repositório de dados da lista de compras.
 * Esta é a abstração que permite o Polimorfismo.
 */
interface RepositorioDeCompras {

    /**
     * Retorna um Flow que emite a lista de itens de compra em tempo real.
     */
    fun obterListaDeCompras(): Flow<List<ItemDeCompra>>

    /**
     * Adiciona um novo item à lista.
     * @param nome O nome do item a ser adicionado.
     * @param quantidadeStr A quantidade em formato de texto.
     */
    suspend fun adicionarItem(nome: String, quantidadeStr: String)

    /**
     * Alterna o estado de 'comprado' de um item.
     * @param item O item a ser modificado.
     */
    suspend fun alternarStatusDeComprado(item: ItemDeCompra)

    /**
     * Deleta um item da lista.
     * @param item O item a ser deletado.
     */
    suspend fun deletarItem(item: ItemDeCompra)
}