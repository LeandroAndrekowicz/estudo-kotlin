package br.com.api.br.com.api.leandro.services.models

import kotlinx.serialization.Serializable

@Serializable
data class Cliente(
    val id: Int? = null,
    val cpf: String,
    val nome: String,
    val rua: String?,
    val bairro: String?,
    val cidade: String?,
    val estado: String?,
    val uf: String?,
    val telefone: String?,
    val email: String?
)