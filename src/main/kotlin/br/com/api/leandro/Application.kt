package br.com.api.br.com.api.leandro

import br.com.api.br.com.api.leandro.database.Database
import br.com.api.configureHTTP
import br.com.api.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    Database
    configureSerialization()
    configureHTTP()
    configureRouting()
}
