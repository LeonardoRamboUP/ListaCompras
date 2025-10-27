package com.example.listacompras.ui.telas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.listacompras.ui.lista.ListaDeComprasViewModel

/**
 * Tela para adicionar um novo item à lista de compras.
 */
@Composable
fun TelaAdicionarItem(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ListaDeComprasViewModel = viewModel() // Recebe o ViewModel compartilhado
) {
    // Estados locais para guardar o que o usuário digita nos campos de texto.
    // `remember` garante que o estado sobreviva a recomposições.
    var nomeDoItem by remember { mutableStateOf("") }
    var quantidade by remember { mutableStateOf("1") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = nomeDoItem,
            onValueChange = { nomeDoItem = it },
            label = { Text("Nome do Item") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = quantidade,
            onValueChange = { quantidade = it },
            label = { Text("Quantidade") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Valida se o nome do item não está vazio antes de salvar.
                if (nomeDoItem.isNotBlank()) {
                    // Chama a função do ViewModel para adicionar o item (a lógica de salvar fica no ViewModel)
                    viewModel.adicionarItem(nomeDoItem, quantidade)
                    // Navega de volta para a tela anterior (a lista de compras)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Item")
        }
    }
}