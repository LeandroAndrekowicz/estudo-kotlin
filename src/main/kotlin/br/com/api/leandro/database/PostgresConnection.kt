package br.com.api.br.com.api.leandro.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource
import io.github.cdimascio.dotenv.dotenv

val dotenv = dotenv()

object Database {
    private val dataSource: DataSource

    init {
        println(System.getenv("DATABASE_URL"))
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = dotenv["DATABASE_URL"]
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
}