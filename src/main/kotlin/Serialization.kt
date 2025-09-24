package br.com.api

import br.com.api.br.com.api.leandro.module
import br.com.api.br.com.api.leandro.serialization.BigDecimalSerializer
import br.com.api.br.com.api.leandro.serialization.LocalDateTimeSerializer
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

fun main() {
    embeddedServer(Netty, 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.configureSerialization() {
    val module = SerializersModule {
        contextual(BigDecimalSerializer)
        contextual(LocalDateTimeSerializer)
    }

    install(ContentNegotiation) {
        json(Json {
            serializersModule = module
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
            encodeDefaults = false
            explicitNulls = false
        })
    }
}
