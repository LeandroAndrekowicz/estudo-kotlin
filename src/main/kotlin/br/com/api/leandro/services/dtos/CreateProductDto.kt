package br.com.api.br.com.api.leandro.services.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CreateProductDto(
    val nome: String,
    val unidade: String,
    val quantidade: Int,
    val preco: String
)
