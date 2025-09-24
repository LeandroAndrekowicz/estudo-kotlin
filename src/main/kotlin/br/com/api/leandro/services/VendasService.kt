package br.com.api.services

import br.com.api.br.com.api.leandro.database.Database
import br.com.api.br.com.api.leandro.services.models.ItemVenda
import br.com.api.br.com.api.leandro.services.models.Venda
import java.math.BigDecimal
import java.time.LocalDateTime

class VendaService {

    fun getAllVendas(): List<Venda> {
        val vendas = mutableListOf<Venda>()
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement("SELECT * FROM vendas")
            val rs = stmt.executeQuery()
            while (rs.next()) {
                vendas.add(
                    Venda(
                        id = rs.getInt("id"),
                        data = rs.getTimestamp("data")?.toLocalDateTime(),
                        clienteId = rs.getInt("cliente_id"),
                        totalVenda = rs.getBigDecimal("total_venda")
                    )
                )
            }
        }
        return vendas
    }

    fun createVenda(venda: Venda, itens: List<ItemVenda>): Venda {
        Database.getConnection().use { conn ->
            conn.autoCommit = false
            try {
                val stmtVenda = conn.prepareStatement(
                    "INSERT INTO vendas (cliente_id, total_venda) VALUES (?, ?) RETURNING id"
                )
                val total = itens.fold(BigDecimal.ZERO) { acc, item -> acc + item.totalItem }
                stmtVenda.setInt(1, venda.clienteId)
                stmtVenda.setBigDecimal(2, total)
                val rsVenda = stmtVenda.executeQuery()
                if (!rsVenda.next()) throw Exception("Erro ao criar venda")
                val vendaId = rsVenda.getInt("id")

                val stmtItem = conn.prepareStatement(
                    "INSERT INTO itens_venda (venda_id, produto_id, quantidade, preco_unitario, total_item) VALUES (?,?,?,?,?)"
                )
                for (item in itens) {
                    stmtItem.setInt(1, vendaId)
                    stmtItem.setInt(2, item.produtoId)
                    stmtItem.setInt(3, item.quantidade)
                    stmtItem.setBigDecimal(4, item.precoUnitario)
                    stmtItem.setBigDecimal(5, item.totalItem)
                    stmtItem.addBatch()
                }
                stmtItem.executeBatch()
                conn.commit()
                return venda.copy(id = vendaId, totalVenda = total, data = LocalDateTime.now())
            } catch (e: Exception) {
                conn.rollback()
                throw e
            } finally {
                conn.autoCommit = true
            }
        }
    }
}
