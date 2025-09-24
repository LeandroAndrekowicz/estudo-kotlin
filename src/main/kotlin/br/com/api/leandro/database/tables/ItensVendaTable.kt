package br.com.api.database.tables

import org.jetbrains.exposed.sql.Table

object ItensVendaTable : Table("itens_venda") {
    val id = integer("id").autoIncrement()
    val vendaId = integer("venda_id").references(VendasTable.id)
    val produtoId = integer("produto_id").references(ProdutosTable.id)
    val quantidade = integer("quantidade")
    val precoUnitario = decimal("preco_unitario", 10, 2)
    val totalItem = decimal("total_item", 10, 2)

    override val primaryKey = PrimaryKey(id)
}
