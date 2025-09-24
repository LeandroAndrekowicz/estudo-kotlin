package br.com.api.br.com.api.leandro

import br.com.api.br.com.api.leandro.database.Database
import br.com.api.configureHTTP
import br.com.api.configureSerialization
import br.com.api.routes.produtoRoutes
import br.com.api.database.tables.ClientesTable
import br.com.api.database.tables.ProdutosTable
import br.com.api.database.tables.VendasTable
import br.com.api.database.tables.ItensVendaTable
import br.com.api.routes.clienteRoutes
import br.com.api.routes.vendaRoutes
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    Database.connect()

    transaction {
        SchemaUtils.create(
            ProdutosTable,
            ClientesTable,
            VendasTable,
            ItensVendaTable
        )
    }

    configureSerialization()
    configureHTTP()

    routing {
        produtoRoutes()
        clienteRoutes()
        vendaRoutes()
    }
}
