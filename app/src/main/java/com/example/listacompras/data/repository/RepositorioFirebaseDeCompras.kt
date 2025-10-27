package com.example.listacompras.data.repository

import android.util.Log
import com.example.listacompras.data.ItemDeCompra
// Importações Corretas para o Firebase moderno
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Implementação do repositório que usa o Cloud Firestore como fonte de dados.
 * Esta classe cumpre o contrato definido por RepositorioDeCompras.
 */
class RepositorioFirebaseDeCompras : RepositorioDeCompras {

    // Obtém a instância do Firestore e a referência para a nossa coleção.
    private val db = Firebase.firestore
    private val colecaoListaDeCompras = db.collection("lista_de_compras")

    /**
     * Ouve as atualizações da coleção no Firestore em tempo real.
     * O callbackFlow nos permite converter o listener do Firebase em um Flow do Kotlin.
     */
    override fun obterListaDeCompras(): Flow<List<ItemDeCompra>> = callbackFlow {
        val listener = colecaoListaDeCompras.orderBy("criadoEm").addSnapshotListener { snapshots, erro ->
            if (erro != null) {
                Log.w("RepositorioFirebase", "Falha ao ouvir o banco de dados.", erro)
                close(erro) // Fecha o Flow com erro
                return@addSnapshotListener
            }

            snapshots?.let {
                // Converte os documentos do Firestore para nossa classe de dados e envia para o Flow
                trySend(it.toObjects<ItemDeCompra>()).isSuccess
            }
        }
        // Garante que o listener seja removido quando o Flow não estiver mais sendo coletado
        awaitClose { listener.remove() }
    }

    /**
     * Adiciona um novo item ao Firestore.
     */
    override suspend fun adicionarItem(nome: String, quantidadeStr: String) {
        val quantidade = quantidadeStr.toIntOrNull() ?: 1
        val documento = colecaoListaDeCompras.document()
        val novoItem = ItemDeCompra(
            id = documento.id,
            nome = nome,
            quantidade = quantidade
        )
        documento.set(novoItem)
    }

    /**
     * Atualiza o status 'comprado' de um item no Firestore.
     */
    override suspend fun alternarStatusDeComprado(item: ItemDeCompra) {
        colecaoListaDeCompras.document(item.id).update("comprado", !item.comprado)
    }

    /**
     * Deleta um item do Firestore usando seu ID.
     */
    override suspend fun deletarItem(item: ItemDeCompra) {
        colecaoListaDeCompras.document(item.id).delete()
    }
}