package br.com.api.br.com.api.leandro.services.dtos

import br.com.api.br.com.api.leandro.serialization.BigDecimalSerializer
import kotlinx.serialization.Serializable
import java.math.BigDecimal

@Serializable
data class CreateSaleDto(
    val clienteId: Int,
    val produtos: List<SaleItemDto>,
    @Serializable(with = BigDecimalSerializer::class)
    val valorTotal: BigDecimal,
    val data: String
)

@Serializable
data class SaleItemDto(
    val produtoId: Int,
    val quantidade: Int
)
