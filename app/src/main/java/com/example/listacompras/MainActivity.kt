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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.listacompras.ui.lista.ListaDeComprasViewModel
import com.example.listacompras.ui.telas.TelaAdicionarItem
import com.example.listacompras.ui.telas.TelaConfiguracoes
import com.example.listacompras.ui.telas.TelaListaDeCompras
import com.example.listacompras.ui.telas.TelaListasPrincipais
import com.example.listacompras.ui.theme.ListaComprasTheme

object Rotas {
    const val TELA_LISTAS_PRINCIPAIS = "telas_listas_principais"
    const val TELA_LISTA_DE_COMPRAS = "telas_lista_de_compras/{listaId}"
    const val TELA_ADICIONAR_ITEM = "telas_adicionar_item/{listaId}"

    fun criarRotaListaDeCompras(listaId: String) = "telas_lista_de_compras/$listaId"
    fun criarRotaAdicionarItem(listaId: String) = "telas_adicionar_item/$listaId"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ListaComprasTheme {
                AppListaDeCompras()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListaDeCompras(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val viewModel: ListaDeComprasViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val rotaAtual = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = { TopAppBar(title = { Text("Minhas Listas de Compras") }) },
        floatingActionButton = {
            if (rotaAtual?.startsWith("telas_lista_de_compras/") == true) {
                FloatingActionButton(onClick = { 
                    val listaId = navBackStackEntry?.arguments?.getString("listaId") ?: ""
                    if (listaId.isNotEmpty()) {
                        navController.navigate(Rotas.criarRotaAdicionarItem(listaId))
                    }
                }) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Item")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Rotas.TELA_LISTAS_PRINCIPAIS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Rotas.TELA_LISTAS_PRINCIPAIS) {
                TelaListasPrincipais(navController = navController, viewModel = viewModel)
            }
            composable(Rotas.TELA_LISTA_DE_COMPRAS) { backStackEntry ->
                val listaId = backStackEntry.arguments?.getString("listaId")
                if (listaId != null) {
                    TelaListaDeCompras(
                        navController = navController,
                        listaId = listaId,
                        viewModel = viewModel
                    )
                }
            }
            composable(Rotas.TELA_ADICIONAR_ITEM) { backStackEntry ->
                val listaId = backStackEntry.arguments?.getString("listaId")
                if (listaId != null) {
                    TelaAdicionarItem(
                        navController = navController, 
                        listaId = listaId, 
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}