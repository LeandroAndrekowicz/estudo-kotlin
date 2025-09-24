package br.com.api.br.com.api.leandro.services

import br.com.api.br.com.api.leandro.database.Database
import br.com.api.br.com.api.leandro.services.models.Cliente
import br.com.api.br.com.api.leandro.services.models.ItemVenda
import br.com.api.br.com.api.leandro.services.models.Venda
import java.math.BigDecimal

class VendaService {

    fun getAllVendas(): List<Venda> {
        val vendas = mutableListOf<Venda>()
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                """
                    SELECT 
                        v.id AS venda_id,
                        v.data,
                        v.total_venda,
                        c.id AS cliente_id,
                        c.nome AS cliente_nome,
                        c.rua AS cliente_rua,
                        c.bairro AS cliente_bairro,
                        c.cidade AS cliente_cidade,
                        c.estado AS cliente_estado,
                        c.uf AS cliente_uf,
                        c.telefone AS cliente_telefone,
                        c.email AS cliente_email,
                        i.id AS item_id,
                        i.produto_id,
                        p.nome AS produto_nome,
                        i.quantidade,
                        i.preco_unitario,
                        i.total_item
                    FROM vendas v
                    JOIN clientes c ON v.cliente_id = c.id
                    LEFT JOIN itens_venda i ON v.id = i.venda_id
                    LEFT JOIN produtos p ON i.produto_id = p.id
                    ORDER BY v.id, i.id
                """.trimIndent()
            )
            val rs = stmt.executeQuery()

            val mapVendas = mutableMapOf<Int, Venda>()

            while (rs.next()) {
                val vendaId = rs.getInt("venda_id")
                val venda = mapVendas.getOrPut(vendaId) {
                    Venda(
                        id = vendaId,
                        cliente = Cliente(
                            id = rs.getInt("cliente_id"),
                            nome = rs.getString("cliente_nome"),
                            rua = rs.getString("cliente_rua"),
                            bairro = rs.getString("cliente_bairro"),
                            cidade = rs.getString("cliente_cidade"),
                            estado = rs.getString("cliente_estado"),
                            uf = rs.getString("cliente_uf"),
                            telefone = rs.getString("cliente_telefone"),
                            email = rs.getString("cliente_email"),
                            cpf = ""
                        ),
                        data = rs.getTimestamp("data")?.toLocalDateTime(),
                        totalVenda = rs.getBigDecimal("total_venda"),
                        itens = mutableListOf()
                    )
                }

                val itemId = rs.getInt("item_id")
                if (!rs.wasNull()) {
                    (venda.itens as MutableList).add(
                        ItemVenda(
                            id = itemId,
                            vendaId = vendaId,
                            produtoId = rs.getInt("produto_id"),
                            quantidade = rs.getInt("quantidade"),
                            precoUnitario = rs.getBigDecimal("preco_unitario"),
                            totalItem = rs.getBigDecimal("total_item"),
                            nomeProduto = rs.getString("produto_nome")
                        )
                    )
                }
            }

            vendas.addAll(mapVendas.values)
        }
        return vendas
    }


    fun createVenda(venda: Venda, itens: List<ItemVenda>): Venda {
        Database.getConnection().use { conn ->
            conn.autoCommit = false
            try {
                val total = itens.fold(BigDecimal.ZERO) { acc, item -> acc + item.totalItem }

                val stmtVenda = conn.prepareStatement(
                    "INSERT INTO vendas (cliente_id, total_venda) VALUES (?, ?) RETURNING id, data"
                )
                stmtVenda.setInt(1, venda.cliente.id ?: throw Exception("Cliente sem ID"))
                stmtVenda.setBigDecimal(2, total)
                val rsVenda = stmtVenda.executeQuery()
                if (!rsVenda.next()) throw Exception("Erro ao criar venda")
                val vendaId = rsVenda.getInt("id")
                val vendaData = rsVenda.getTimestamp("data")?.toLocalDateTime()

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

                return venda.copy(id = vendaId, totalVenda = total, data = vendaData, itens = itens)
            } catch (e: Exception) {
                conn.rollback()
                throw e
            } finally {
                conn.autoCommit = true
            }
        }
    }


    fun getVendaById(id: Int): Venda? {
        Database.getConnection().use { conn ->
            val stmtVenda = conn.prepareStatement("""
            SELECT v.*, c.id AS cliente_id, c.cpf, c.nome, c.rua, c.bairro, c.cidade, c.estado, c.uf, c.telefone, c.email
            FROM vendas v
            JOIN clientes c ON v.cliente_id = c.id
            WHERE v.id = ?
        """.trimIndent())
            stmtVenda.setInt(1, id)
            val rsVenda = stmtVenda.executeQuery()
            if (!rsVenda.next()) return null

            val cliente = Cliente(
                id = rsVenda.getInt("cliente_id"),
                cpf = rsVenda.getString("cpf"),
                nome = rsVenda.getString("nome"),
                rua = rsVenda.getString("rua"),
                bairro = rsVenda.getString("bairro"),
                cidade = rsVenda.getString("cidade"),
                estado = rsVenda.getString("estado"),
                uf = rsVenda.getString("uf"),
                telefone = rsVenda.getString("telefone"),
                email = rsVenda.getString("email")
            )

            val venda = Venda(
                id = rsVenda.getInt("id"),
                cliente = cliente,
                data = rsVenda.getTimestamp("data")?.toLocalDateTime(),
                totalVenda = rsVenda.getBigDecimal("total_venda")
            )

            val stmtItens = conn.prepareStatement("""
            SELECT iv.*, p.nome AS nome_produto
            FROM itens_venda iv
            JOIN produtos p ON iv.produto_id = p.id
            WHERE iv.venda_id = ?
        """.trimIndent())
            stmtItens.setInt(1, id)
            val rsItens = stmtItens.executeQuery()
            val itens = mutableListOf<ItemVenda>()
            while (rsItens.next()) {
                itens.add(
                    ItemVenda(
                        id = rsItens.getInt("id"),
                        vendaId = rsItens.getInt("venda_id"),
                        produtoId = rsItens.getInt("produto_id"),
                        quantidade = rsItens.getInt("quantidade"),
                        precoUnitario = rsItens.getBigDecimal("preco_unitario"),
                        totalItem = rsItens.getBigDecimal("total_item"),
                        nomeProduto = rsItens.getString("nome_produto")
                    )
                )
            }

            return venda.copy(itens = itens)
        }
    }
}
