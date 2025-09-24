package br.com.api.database.tables

import org.jetbrains.exposed.sql.Table

object ClientesTable : Table("clientes") {
    val id = integer("id").autoIncrement()
    val cpf = varchar("cpf", 14).uniqueIndex()
    val nome = varchar("nome", 255)
    val rua = varchar("rua", 255)
    val bairro = varchar("bairro", 100)
    val cidade = varchar("cidade", 100)
    val estado = varchar("estado", 100)
    val uf = varchar("uf", 2)
    val telefone = varchar("telefone", 20)
    val email = varchar("email", 255)

    override val primaryKey = PrimaryKey(id)
}
