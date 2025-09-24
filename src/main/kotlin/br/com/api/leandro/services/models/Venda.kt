package br.com.api.br.com.api.leandro.services.models
import java.math.BigDecimal
import java.time.LocalDateTime

data class Venda(
    val id: Int? = null,
    val data: LocalDateTime? = null,
    val clienteId: Int,
    val totalVenda: BigDecimal? = null
)

data class ItemVenda(
    val id: Int? = null,
    val vendaId: Int,
    val produtoId: Int,
    val quantidade: Int,
    val precoUnitario: BigDecimal,
    val totalItem: BigDecimal
)

