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

@Composable
fun TelaAdicionarItem(
    navController: NavController,
    listaId: String, // Parâmetro obrigatório da rota
    modifier: Modifier = Modifier,
    viewModel: ListaDeComprasViewModel = viewModel()
) {
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
                if (nomeDoItem.isNotBlank()) {
                    // Chama a função do ViewModel, passando o ID da lista
                    viewModel.adicionarItem(listaId, nomeDoItem, quantidade)
                    // Volta para a tela anterior (a lista de itens)
                    navController.popBackStack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar Item")
        }
    }
}