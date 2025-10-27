package com.example.listacompras.data

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

/**
 * Representa um único item na lista de compras.
 * Esta é a classe de dados (Model) principal da nossa aplicação.
 */
@IgnoreExtraProperties
data class ItemDeCompra(
    @get:PropertyName("id") @set:PropertyName("id") var id: String = "",
    @get:PropertyName("nome") @set:PropertyName("nome") var nome: String = "",
    @get:PropertyName("quantidade") @set:PropertyName("quantidade") var quantidade: Int = 1,
    @get:PropertyName("comprado") @set:PropertyName("comprado") var comprado: Boolean = false,
    @ServerTimestamp @get:PropertyName("criadoEm") @set:PropertyName("criadoEm") var criadoEm: Date? = null
)