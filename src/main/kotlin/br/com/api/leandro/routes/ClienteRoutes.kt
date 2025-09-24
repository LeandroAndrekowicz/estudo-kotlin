package br.com.api.routes

import br.com.api.br.com.api.leandro.services.models.Cliente
import br.com.api.services.ClienteService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clienteRoutes() {
    val service = ClienteService()

    route("/clientes") {
        get { call.respond(service.getAll()) }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) return@get call.respond(HttpStatusCode.BadRequest, "ID inválido")
            val cliente = service.getById(id)
            if (cliente == null) call.respond(HttpStatusCode.NotFound, "Cliente não encontrado")
            else call.respond(cliente)
        }

        post {
            val cliente = call.receive<Cliente>()
            call.respond(HttpStatusCode.Created, service.create(cliente))
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
            val cliente = call.receive<Cliente>()
            if (service.update(id, cliente)) call.respond(HttpStatusCode.OK, "Cliente atualizado")
            else call.respond(HttpStatusCode.NotFound, "Cliente não encontrado")
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
            if (service.delete(id)) call.respond(HttpStatusCode.OK, "Cliente removido")
            else call.respond(HttpStatusCode.NotFound, "Cliente não encontrado")
        }
    }
}
