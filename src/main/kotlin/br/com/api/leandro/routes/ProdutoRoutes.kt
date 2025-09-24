package br.com.api.br.com.api.leandro.routes

import br.com.api.br.com.api.leandro.services.dtos.CreateProductDto
import br.com.api.br.com.api.leandro.services.models.Produto
import br.com.api.br.com.api.leandro.services.ProdutoService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.produtoRoutes() {
    val produtoService = ProdutoService()

    route("/produtos") {
        get {
            call.respond(produtoService.getAll())
        }

        get("{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) return@get call.respond(HttpStatusCode.BadRequest, "ID inválido")
            val produto = produtoService.getById(id)
            if (produto == null) call.respond(HttpStatusCode.NotFound, "Produto não encontrado")
            else call.respond(produto)
        }

        post {
            println("Create produtos")
            val body = call.receive<CreateProductDto>()
            val produto = Produto(
                nome = body.nome,
                unidade = body.unidade,
                quantidade = body.quantidade,
                preco = body.preco.toBigDecimal()
            )
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