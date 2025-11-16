package com.example.listacompras.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacompras.data.ItemDeCompra
import com.example.listacompras.data.ListaDeCompras
import com.example.listacompras.data.repository.RepositorioDeCompras
import com.example.listacompras.data.repository.RepositorioFirebaseDeCompras
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ListaDeComprasViewModel(
    private val repositorio: RepositorioDeCompras = RepositorioFirebaseDeCompras()
) : ViewModel() {

    // Estado para a lista de ITENS (usado na tela de detalhes)
    private val _itensState = MutableStateFlow<List<ItemDeCompra>>(emptyList())
    val itensState: StateFlow<List<ItemDeCompra>> = _itensState.asStateFlow()

    // Estado para a lista de LISTAS (usado na tela principal)
    private val _listasState = MutableStateFlow<List<ListaDeCompras>>(emptyList())
    val listasState: StateFlow<List<ListaDeCompras>> = _listasState.asStateFlow()

    private var jobDeColetaDeItens: Job? = null

    init {
        carregarListasPrincipais()
    }

    private fun carregarListasPrincipais() {
        viewModelScope.launch {
            repositorio.obterListas().collect { listas ->
                _listasState.value = listas
            }
        }
    }

    fun adicionarLista(nomeDaLista: String) {
        viewModelScope.launch {
            repositorio.adicionarLista(nomeDaLista)
        }
    }

    fun deletarLista(listaId: String) {
        viewModelScope.launch {
            repositorio.deletarLista(listaId)
        }
    }

    fun carregarItensDaLista(listaId: String) {
        jobDeColetaDeItens?.cancel()
        jobDeColetaDeItens = viewModelScope.launch {
            repositorio.obterItensDaLista(listaId).collect { itens ->
                _itensState.value = itens
            }
        }
    }

    fun adicionarItem(listaId: String, nomeDoItem: String, quantidadeStr: String) {
        viewModelScope.launch {
            repositorio.adicionarItem(listaId, nomeDoItem, quantidadeStr)
        }
    }

    fun alternarStatusDeComprado(listaId: String, item: ItemDeCompra) {
        viewModelScope.launch {
            repositorio.alternarStatusDeComprado(listaId, item)
        }
    }

    fun deletarItem(listaId: String, item: ItemDeCompra) {
        viewModelScope.launch {
            repositorio.deletarItem(listaId, item)
        }
    }
}