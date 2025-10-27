package com.example.listacompras

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.listacompras.ui.lista.ListaDeComprasViewModel
import com.example.listacompras.ui.telas.TelaAdicionarItem
import com.example.listacompras.ui.telas.TelaConfiguracoes
import com.example.listacompras.ui.telas.TelaListaDeCompras
import com.example.listacompras.ui.theme.ListaComprasTheme

/**
 * Define as rotas (os "endereços") para cada tela do aplicativo.
 * Usar um objeto garante que os nomes das rotas sejam consistentes e evita erros de digitação.
 */
object Rotas {
    const val LISTA_DE_COMPRAS = "lista_de_compras"
    const val ADICIONAR_ITEM = "adicionar_item"
    const val CONFIGURACOES = "configuracoes"
}

/**
 * A Activity principal, ponto de entrada da aplicação Android.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita a UI em tela cheia
        setContent {
            ListaComprasTheme {
                AppListaDeCompras()
            }
        }
    }
}

/**
 * O Composable raiz da aplicação. 
 * Ele configura a estrutura principal com Scaffold, TopAppBar, FAB e o sistema de navegação (NavHost).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListaDeCompras(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    // Cria a instância do ViewModel que será compartilhada entre as telas.
    val listaDeComprasViewModel: ListaDeComprasViewModel = viewModel()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Lista de Compras") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Rotas.ADICIONAR_ITEM) }) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Item")
            }
        }
    ) { innerPadding ->
        // NavHost é o contêiner que exibe a tela atual com base na rota.
        NavHost(
            navController = navController,
            startDestination = Rotas.LISTA_DE_COMPRAS, // A primeira tela a ser exibida
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotas.LISTA_DE_COMPRAS) {
                TelaListaDeCompras(
                    navController = navController,
                    viewModel = listaDeComprasViewModel // Passa o ViewModel compartilhado
                )
            }
            composable(Rotas.ADICIONAR_ITEM) {
                TelaAdicionarItem(
                    navController = navController,
                    viewModel = listaDeComprasViewModel // Passa o mesmo ViewModel compartilhado
                )
            }
            composable(Rotas.CONFIGURACOES) {
                TelaConfiguracoes(navController = navController)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppListaDeComprasPreview() {
    ListaComprasTheme {
        AppListaDeCompras()
    }
}