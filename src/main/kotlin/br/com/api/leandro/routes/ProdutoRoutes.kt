package br.com.api.routes

import br.com.api.services.Produto
import br.com.api.services.ProdutoService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.produtoRoutes() {
    val produtoService = ProdutoService()

    route("/produtos") {
        get { call.respond(produtoService.getAll()) }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) return@get call.respond(HttpStatusCode.BadRequest, "ID inválido")
            val produto = produtoService.getById(id)
            if (produto == null) call.respond(HttpStatusCode.NotFound, "Produto não encontrado")
            else call.respond(produto)
        }

        post {
            val produto = call.receive<Produto>()
            val novoProduto = produtoService.create(produto)
            call.respond(HttpStatusCode.Created, novoProduto)
        }

        put("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) return@put call.respond(HttpStatusCode.BadRequest, "ID inválido")
            val produto = call.receive<Produto>()
            val atualizado = produtoService.update(id, produto)
            if (atualizado) call.respond(HttpStatusCode.OK, "Produto atualizado")
            else call.respond(HttpStatusCode.NotFound, "Produto não encontrado")
        }

        delete("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) return@delete call.respond(HttpStatusCode.BadRequest, "ID inválido")
            val deletado = produtoService.delete(id)
            if (deletado) call.respond(HttpStatusCode.OK, "Produto removido")
            else call.respond(HttpStatusCode.NotFound, "Produto não encontrado")
        }
    }
}
