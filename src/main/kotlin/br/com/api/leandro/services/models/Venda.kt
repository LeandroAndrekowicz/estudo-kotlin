package br.com.api.br.com.api.leandro.services.models

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.math.BigDecimal
import java.time.LocalDateTime

@Serializable
data class Venda(
    val id: Int? = null,
    val cliente: Cliente,
    @Contextual
    val data: LocalDateTime? = null,
    @Contextual
    val totalVenda: BigDecimal? = null,
    val itens: List<ItemVenda> = emptyList()
)

@Serializable
data class ItemVenda(
    val id: Int? = null,
    val vendaId: Int,
    val produtoId: Int,
    val quantidade: Int,
    val nomeProduto: String,
    @Contextual
    val precoUnitario: BigDecimal,
    @Contextual
    val totalItem: BigDecimal
)