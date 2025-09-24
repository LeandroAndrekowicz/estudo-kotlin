package br.com.api.br.com.api.leandro.services

import br.com.api.br.com.api.leandro.database.Database
import br.com.api.br.com.api.leandro.services.models.Produto

class ProdutoService {

    fun getAll(): List<Produto> {
        val produtos = mutableListOf<Produto>()
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement("SELECT id, nome, unidade, quantidade, preco FROM produtos")
            val rs = stmt.executeQuery()
            while (rs.next()) {
                produtos.add(
                    Produto(
                        id = rs.getInt("id"),
                        nome = rs.getString("nome"),
                        unidade = rs.getString("unidade"),
                        quantidade = rs.getInt("quantidade"),
                        preco = rs.getBigDecimal("preco")
                    )
                )
            }
        }
        return produtos
    }

    fun getById(id: Int): Produto? {
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "SELECT id, nome, unidade, quantidade, preco FROM produtos WHERE id = ?"
            )
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()
            return if (rs.next()) {
                Produto(
                    id = rs.getInt("id"),
                    nome = rs.getString("nome"),
                    unidade = rs.getString("unidade"),
                    quantidade = rs.getInt("quantidade"),
                    preco = rs.getBigDecimal("preco")
                )
            } else null
        }
    }

    fun create(produto: Produto): Produto {
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "INSERT INTO produtos (nome, unidade, quantidade, preco) VALUES (?, ?, ?, ?) RETURNING id"
            )

            stmt.setString(1, produto.nome)
            stmt.setString(2, produto.unidade)
            stmt.setInt(3, produto.quantidade)
            stmt.setBigDecimal(4, produto.preco)

            val rs = stmt.executeQuery()

            if (rs.next()) {
                return produto.copy(id = rs.getInt("id"))
            } else {
                throw Exception("Erro ao inserir produto")
            }
        }
    }

    fun update(id: Int, produto: Produto): Boolean {
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "UPDATE produtos SET nome = ?, unidade = ?, quantidade = ?, preco = ? WHERE id = ?"
            )
            stmt.setString(1, produto.nome)
            stmt.setString(2, produto.unidade)
            stmt.setInt(3, produto.quantidade)
            stmt.setBigDecimal(4, produto.preco)
            stmt.setInt(5, id)
            return stmt.executeUpdate() > 0
        }
    }

    fun delete(id: Int): Boolean {
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement("DELETE FROM produtos WHERE id = ?")
            stmt.setInt(1, id)
            return stmt.executeUpdate() > 0
        }
    }
}
