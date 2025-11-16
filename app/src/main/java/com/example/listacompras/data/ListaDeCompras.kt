package com.example.listacompras.data

import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Representa uma única lista de compras, que por sua vez contém uma coleção de itens.
 * Ex: Uma lista chamada "Churrasco de Sábado".
 */
@IgnoreExtraProperties
data class ListaDeCompras(
    val id: String = "",
    val nome: String = ""
)