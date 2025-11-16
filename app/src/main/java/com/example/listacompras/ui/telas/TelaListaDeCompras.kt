package com.example.listacompras.ui.telas

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.listacompras.data.ItemDeCompra
import com.example.listacompras.ui.lista.ListaDeComprasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaDeCompras(
    navController: NavController,
    listaId: String, // 1. ACEITA O PARÂMETRO 'listaId'
    modifier: Modifier = Modifier,
    viewModel: ListaDeComprasViewModel = viewModel()
) {
    // 2. USA O 'LaunchedEffect' PARA CARREGAR OS DADOS DA LISTA CORRETA
    LaunchedEffect(listaId) {
        viewModel.carregarItensDaLista(listaId)
    }

    // 3. OBSERVA O ESTADO CORRETO ('itensState')
    val itensDaLista by viewModel.itensState.collectAsState()

    Box(modifier = modifier.fillMaxSize()) {
        if (itensDaLista.isEmpty()) {
            Text(
                text = "Sua lista de compras está vazia.\nToque no botão '+' para adicionar um item.",
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp)
            ) {
                items(itensDaLista, key = { it.id }) { item ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if (it == SwipeToDismissBoxValue.EndToStart || it == SwipeToDismissBoxValue.StartToEnd) {
                                // 4. PASSA O 'listaId' PARA A FUNÇÃO DO VIEWMODEL
                                viewModel.deletarItem(listaId, item)
                                true
                            } else {
                                false
                            }
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = { SwipeBackground(dismissState) },
                        enableDismissFromStartToEnd = true,
                        enableDismissFromEndToStart = true
                    ) {
                        ItemDaListaView(
                            item = item,
                            onItemBoughtChanged = {
                                // 4. PASSA O 'listaId' PARA A FUNÇÃO DO VIEWMODEL
                                viewModel.alternarStatusDeComprado(listaId, item)
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeBackground(dismissState: SwipeToDismissBoxState) {
    val color by animateColorAsState(
        targetValue = when (dismissState.targetValue) {
            SwipeToDismissBoxValue.EndToStart, SwipeToDismissBoxValue.StartToEnd -> Color.Red.copy(alpha = 0.8f)
            SwipeToDismissBoxValue.Settled -> Color.Transparent
        },
        label = "color animation"
    )
    val scale by animateFloatAsState(
        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.Settled) 0.75f else 1f,
        label = "scale animation"
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Icon(
            Icons.Default.Delete,
            contentDescription = "Deletar Item",
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
fun ItemDaListaView(
    item: ItemDeCompra,
    onItemBoughtChanged: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.comprado,
                onCheckedChange = { onItemBoughtChanged() }
            )
            Text(
                text = "${item.quantidade}x ${item.nome}",
                modifier = Modifier.padding(start = 16.dp),
                textDecoration = if (item.comprado) TextDecoration.LineThrough else null
            )
        }
    }
}