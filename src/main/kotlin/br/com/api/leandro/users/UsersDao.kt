package br.com.api.br.com.api.leandro.users

import br.com.api.br.com.api.leandro.database.Database


class UsersDao {
    fun findAll(): List<User> {
        val query = "SELECT * FROM users"

        Database.getConnection().use { connection ->
            val statement = connection.prepareStatement(query)
            val resultSet = statement.executeQuery()

            val users = mutableListOf<User>()
            while (resultSet.next()) {
                users.add(resultSet.toUser())
            }
            return users
        }
    }
}