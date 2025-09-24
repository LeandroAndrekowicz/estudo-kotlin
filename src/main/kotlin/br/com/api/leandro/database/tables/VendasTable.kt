package br.com.api.br.com.api.leandro.database.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object VendasTable : Table("vendas") {
    val id = integer("id").autoIncrement()
    val data = datetime("data")
    val clienteId = integer("cliente_id").references(ClientesTable.id)
    val totalVenda = decimal("total_venda", 10, 2)

    override val primaryKey = PrimaryKey(id)
}
