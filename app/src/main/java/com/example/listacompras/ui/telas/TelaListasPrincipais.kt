package com.example.listacompras.ui.telas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.listacompras.Rotas
import com.example.listacompras.data.ListaDeCompras
import com.example.listacompras.ui.lista.ListaDeComprasViewModel

@Composable
fun TelaListasPrincipais(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ListaDeComprasViewModel = viewModel()
) {
    val listas by viewModel.listasState.collectAsState()
    var nomeNovaLista by remember { mutableStateOf("") }
    var listaParaDeletar by remember { mutableStateOf<ListaDeCompras?>(null) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Seção para adicionar nova lista
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = nomeNovaLista,
                onValueChange = { nomeNovaLista = it },
                label = { Text("Nova Lista") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (nomeNovaLista.isNotBlank()) {
                    viewModel.adicionarLista(nomeNovaLista)
                    nomeNovaLista = ""
                }
            }) {
                Text("Criar")
            }
        }

        Spacer(modifier = Modifier.padding(16.dp))

        // Seção da lista de listas
        if (listas.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = "Nenhuma lista de compras criada.\nUse o campo acima para criar sua primeira lista.",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        } else {
            LazyColumn {
                items(listas, key = { it.id }) { lista ->
                    CardLista(
                        lista = lista, 
                        onClick = {
                            navController.navigate(Rotas.criarRotaListaDeCompras(lista.id))
                        },
                        onDelete = { listaParaDeletar = lista } // Define qual lista deletar
                    )
                }
            }
        }
    }

    // Diálogo de confirmação de exclusão
    listaParaDeletar?.let {
        AlertDialog(
            onDismissRequest = { listaParaDeletar = null },
            title = { Text("Confirmar Exclusão") },
            text = { Text("Você tem certeza que deseja excluir a lista \"${it.nome}\"? Esta ação não pode ser desfeita.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.deletarLista(it.id)
                    listaParaDeletar = null
                }) {
                    Text("Excluir")
                }
            },
            dismissButton = {
                Button(onClick = { listaParaDeletar = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun CardLista(
    lista: ListaDeCompras, 
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = lista.nome,
                modifier = Modifier.weight(1f).padding(vertical = 16.dp),
                style = MaterialTheme.typography.titleMedium
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar Lista")
            }
        }
    }
}