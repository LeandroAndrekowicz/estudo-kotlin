package br.com.api.br.com.api.leandro.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import io.github.cdimascio.dotenv.dotenv
import java.sql.Connection
import org.jetbrains.exposed.sql.Database as ExposedDatabase

val dotenv = dotenv()

object Database {
    private val dataSource: DataSource

    init {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = dotenv["DATABASE_URL"] ?: "jdbc:postgresql://localhost:5432/atividade"
            username = dotenv["DATABASE_USERNAME"] ?: "postgres"
            password = dotenv["DATABASE_PASSWORD"] ?: "root"
            maximumPoolSize = 50
            minimumIdle = 2
            connectionTimeout = 30000
            idleTimeout = 600000
            maxLifetime = 1800000
        }

        dataSource = HikariDataSource(config)
    }

    fun getConnection(): Connection = dataSource.connection

    fun connect() {
        ExposedDatabase.connect(dataSource)
        println("Conectado ao banco via Exposed")
    }
}