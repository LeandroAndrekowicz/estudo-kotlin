package br.com.api.br.com.api.leandro.services.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class Produto(
    val id: Int = 0,
    val nome: String,
    val unidade: String,
    val quantidade: Int,
    @Contextual
    val preco: BigDecimal
)