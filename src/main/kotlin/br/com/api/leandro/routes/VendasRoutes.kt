package br.com.api.br.com.api.leandro.routes

import br.com.api.br.com.api.leandro.services.dtos.CreateSaleDto
import br.com.api.br.com.api.leandro.services.models.ItemVenda
import br.com.api.br.com.api.leandro.services.models.Venda
import br.com.api.br.com.api.leandro.services.ClienteService
import br.com.api.br.com.api.leandro.services.ProdutoService
import br.com.api.br.com.api.leandro.services.VendaService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.math.BigDecimal
import java.time.LocalDateTime

fun Route.vendaRoutes() {
    val service = VendaService()
    val produtoService = ProdutoService()
    val clienteService = ClienteService()

    route("/vendas") {
        get { call.respond(service.getAllVendas()) }

        post {
            try {
                val body = call.receive<CreateSaleDto>()

                clienteService.getById(body.clienteId)
                    ?: return@post call.respond(HttpStatusCode.BadRequest, "Cliente ${body.clienteId} não encontrado")

                val itens = body.produtos.map { item ->
                    val produto = produtoService.getById(item.produtoId)
                        ?: throw IllegalArgumentException("Produto ${item.produtoId} não encontrado")

                    ItemVenda(
                        vendaId = 0,
                        produtoId = produto.id,
                        quantidade = item.quantidade,
                        precoUnitario = produto.preco,
                        totalItem = produto.preco * item.quantidade.toBigDecimal(),
                        nomeProduto = produto.nome
                    )
                }

                val venda = Venda(
                    cliente = clienteService.getById(body.clienteId)!!,
                    data = LocalDateTime.now(),
                    totalVenda = itens.fold(BigDecimal.ZERO) { acc, item -> acc + item.totalItem },
                    itens = itens
                )
                val novaVenda = service.createVenda(venda, itens)
                call.respond(HttpStatusCode.Created, novaVenda)

            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, e.message ?: "Erro")
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, e.message ?: "Erro interno")
            }
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()
            if (id == null) {
                call.respond(HttpStatusCode.BadRequest, "ID inválido")
                return@get
            }

            val venda = service.getVendaById(id)
            if (venda == null) {
                call.respond(HttpStatusCode.NotFound, "Venda não encontrada")
            } else {
                call.respond(venda)
            }
        }
    }
}