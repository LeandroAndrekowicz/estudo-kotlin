package br.com.api.br.com.api.leandro.users

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val name: String,
    val email: String? = null,
    val phone: String? = null,
    val username: String,
    val password: String
)