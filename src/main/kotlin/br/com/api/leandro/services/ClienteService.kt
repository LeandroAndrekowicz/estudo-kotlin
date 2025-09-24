package br.com.api.services

import br.com.api.br.com.api.leandro.database.Database
import br.com.api.br.com.api.leandro.services.models.Cliente

class ClienteService {

    fun getAll(): List<Cliente> {
        val clientes = mutableListOf<Cliente>()
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement("SELECT * FROM clientes")
            val rs = stmt.executeQuery()
            while (rs.next()) {
                clientes.add(
                    Cliente(
                        id = rs.getInt("id"),
                        cpf = rs.getString("cpf"),
                        nome = rs.getString("nome"),
                        rua = rs.getString("rua"),
                        bairro = rs.getString("bairro"),
                        cidade = rs.getString("cidade"),
                        estado = rs.getString("estado"),
                        uf = rs.getString("uf"),
                        telefone = rs.getString("telefone"),
                        email = rs.getString("email")
                    )
                )
            }
        }
        return clientes
    }

    fun getById(id: Int): Cliente? {
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement("SELECT * FROM clientes WHERE id = ?")
            stmt.setInt(1, id)
            val rs = stmt.executeQuery()
            return if (rs.next()) {
                Cliente(
                    id = rs.getInt("id"),
                    cpf = rs.getString("cpf"),
                    nome = rs.getString("nome"),
                    rua = rs.getString("rua"),
                    bairro = rs.getString("bairro"),
                    cidade = rs.getString("cidade"),
                    estado = rs.getString("estado"),
                    uf = rs.getString("uf"),
                    telefone = rs.getString("telefone"),
                    email = rs.getString("email")
                )
            } else null
        }
    }

    fun create(cliente: Cliente): Cliente {
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "INSERT INTO clientes (cpf,nome,rua,bairro,cidade,estado,uf,telefone,email) VALUES (?,?,?,?,?,?,?,?,?) RETURNING id"
            )
            stmt.setString(1, cliente.cpf)
            stmt.setString(2, cliente.nome)
            stmt.setString(3, cliente.rua)
            stmt.setString(4, cliente.bairro)
            stmt.setString(5, cliente.cidade)
            stmt.setString(6, cliente.estado)
            stmt.setString(7, cliente.uf)
            stmt.setString(8, cliente.telefone)
            stmt.setString(9, cliente.email)
            val rs = stmt.executeQuery()
            if (rs.next()) return cliente.copy(id = rs.getInt("id"))
            else throw Exception("Erro ao criar cliente")
        }
    }

    fun update(id: Int, cliente: Cliente): Boolean {
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement(
                "UPDATE clientes SET cpf=?, nome=?, rua=?, bairro=?, cidade=?, estado=?, uf=?, telefone=?, email=? WHERE id=?"
            )
            stmt.setString(1, cliente.cpf)
            stmt.setString(2, cliente.nome)
            stmt.setString(3, cliente.rua)
            stmt.setString(4, cliente.bairro)
            stmt.setString(5, cliente.cidade)
            stmt.setString(6, cliente.estado)
            stmt.setString(7, cliente.uf)
            stmt.setString(8, cliente.telefone)
            stmt.setString(9, cliente.email)
            stmt.setInt(10, id)
            return stmt.executeUpdate() > 0
        }
    }

    fun delete(id: Int): Boolean {
        Database.getConnection().use { conn ->
            val stmt = conn.prepareStatement("DELETE FROM clientes WHERE id=?")
            stmt.setInt(1, id)
            return stmt.executeUpdate() > 0
        }
    }
}
