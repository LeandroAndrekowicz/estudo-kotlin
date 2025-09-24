package br.com.api.br.com.api.leandro.routes

import br.com.api.br.com.api.leandro.services.dtos.CreateClientDto
import br.com.api.br.com.api.leandro.services.models.Cliente
import br.com.api.br.com.api.leandro.services.ClienteService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.clienteRoutes() {
    val service = ClienteService()

    route("/clientes") {
        get {
            call.respond(service.getAll())
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) return@get call.respond(HttpStatusCode.BadRequest, "ID inválido")
            val cliente = service.getById(id)
            if (cliente == null) call.respond(HttpStatusCode.NotFound, "Cliente não encontrado")
            else call.respond(cliente)
        }

        post {
            try {
                val body = call.receive<CreateClientDto>()
                val cliente = Cliente(
                    cpf = body.cpf,
                    nome = body.nome,
                    rua = body.rua,
                    bairro = body.bairro,
                    cidade = body.cidade,
                    estado = body.estado,
                    uf = body.uf,
                    telefone = body.telefone,
                    email = body.email
                )
                val novoCliente = service.create(cliente)
                call.respond(HttpStatusCode.Created, novoCliente)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.Conflict, e.message ?: "Erro ao criar cliente")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Erro interno")
            }
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