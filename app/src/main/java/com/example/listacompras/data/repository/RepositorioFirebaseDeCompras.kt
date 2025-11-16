package com.example.listacompras.data.repository

import android.util.Log
import com.example.listacompras.data.ItemDeCompra
import com.example.listacompras.data.ListaDeCompras
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RepositorioFirebaseDeCompras : RepositorioDeCompras {

    private val colecaoDeListas = Firebase.firestore.collection("listas_de_compras")

    override fun obterListas(): Flow<List<ListaDeCompras>> = callbackFlow {
        val listener = colecaoDeListas.addSnapshotListener { snapshots, erro ->
            if (erro != null) {
                Log.w("RepositorioFirebase", "Falha ao ouvir listas", erro)
                close(erro)
                return@addSnapshotListener
            }
            snapshots?.let { trySend(it.toObjects()).isSuccess }
        }
        awaitClose { listener.remove() }
    }

    override suspend fun adicionarLista(nomeDaLista: String) {
        val documento = colecaoDeListas.document()
        val novaLista = ListaDeCompras(id = documento.id, nome = nomeDaLista)
        documento.set(novaLista)
    }

    override suspend fun deletarLista(listaId: String) {
        // ATENÇÃO: Isso deleta o documento da lista, mas não a sub-coleção de itens.
        // Para um app de produção, uma Cloud Function seria necessária para limpar os itens órfãos.
        colecaoDeListas.document(listaId).delete()
    }

    override fun obterItensDaLista(listaId: String): Flow<List<ItemDeCompra>> = callbackFlow {
        val listener = colecaoDeListas.document(listaId).collection("itens")
            .orderBy("criadoEm")
            .addSnapshotListener { snapshots, erro ->
                if (erro != null) {
                    Log.w("RepositorioFirebase", "Falha ao ouvir itens da lista $listaId", erro)
                    close(erro)
                    return@addSnapshotListener
                }
                snapshots?.let { trySend(it.toObjects()).isSuccess }
            }
        awaitClose { listener.remove() }
    }

    override suspend fun adicionarItem(listaId: String, nomeDoItem: String, quantidadeStr: String) {
        val colecaoDeItens = colecaoDeListas.document(listaId).collection("itens")
        val documento = colecaoDeItens.document()
        val novoItem = ItemDeCompra(
            id = documento.id,
            nome = nomeDoItem,
            quantidade = quantidadeStr.toIntOrNull() ?: 1
        )
        documento.set(novoItem)
    }

    override suspend fun alternarStatusDeComprado(listaId: String, item: ItemDeCompra) {
        colecaoDeListas.document(listaId).collection("itens").document(item.id)
            .update("comprado", !item.comprado)
    }

    override suspend fun deletarItem(listaId: String, item: ItemDeCompra) {
        colecaoDeListas.document(listaId).collection("itens").document(item.id).delete()
    }
}