package br.com.api.br.com.api.leandro.services.dtos

import kotlinx.serialization.Serializable

@Serializable
data class CreateClientDto(
    val cpf: String,
    val nome: String,
    val rua: String? = null,
    val bairro: String? = null,
    val cidade: String? = null,
    val estado: String? = null,
    val uf: String? = null,
    val telefone: String? = null,
    val email: String? = null
)