package com.example.listacompras.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.listacompras.data.ItemDeCompra
import com.example.listacompras.data.repository.RepositorioDeCompras
import com.example.listacompras.data.repository.RepositorioFirebaseDeCompras
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para a tela da lista de compras.
 * Ele gerencia o estado da UI e se comunica com o repositório de dados.
 */
class ListaDeComprasViewModel(private val repositorio: RepositorioDeCompras = RepositorioFirebaseDeCompras()) : ViewModel() {

    // StateFlow privado e mutável que guarda o estado atual da lista.
    private val _uiState = MutableStateFlow<List<ItemDeCompra>>(emptyList())
    // StateFlow público e imutável que a UI observa para se atualizar.
    val uiState: StateFlow<List<ItemDeCompra>> = _uiState.asStateFlow()

    init {
        // Inicia a coleta de dados do repositório assim que o ViewModel é criado.
        viewModelScope.launch {
            repositorio.obterListaDeCompras().collect { itens ->
                _uiState.value = itens
            }
        }
    }

    /**
     * Pede ao repositório para alternar o estado de um item (comprado/não comprado).
     */
    fun alternarStatusDeComprado(item: ItemDeCompra) {
        viewModelScope.launch {
            repositorio.alternarStatusDeComprado(item)
        }
    }

    /**
     * Pede ao repositório para adicionar um novo item.
     */
    fun adicionarItem(nome: String, quantidadeStr: String) {
        viewModelScope.launch {
            repositorio.adicionarItem(nome, quantidadeStr)
        }
    }

    /**
     * Pede ao repositório para deletar um item.
     */
    fun deletarItem(item: ItemDeCompra) {
        viewModelScope.launch {
            repositorio.deletarItem(item)
        }
    }
}
