package br.com.api.database.tables

import org.jetbrains.exposed.sql.Table

object ProdutosTable : Table("produtos") {
    val id = integer("id").autoIncrement()
    val nome = varchar("nome", 255)
    val unidade = varchar("unidade", 50)
    val quantidade = integer("quantidade")
    val preco = decimal("preco", 10, 2)

    override val primaryKey = PrimaryKey(id)
}
