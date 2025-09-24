package br.com.api.routes

import br.com.api.br.com.api.leandro.services.models.ItemVenda
import br.com.api.br.com.api.leandro.services.models.Venda
import br.com.api.services.VendaService
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.math.BigDecimal

fun Route.vendaRoutes() {
    val service = VendaService()

    route("/vendas") {
        get { call.respond(service.getAllVendas()) }

        post {
            val body = call.receive<CreateVendaRequest>()
            val venda = Venda(clienteId = body.clienteId)
            val itens = body.itens.map { item ->
                ItemVenda(
                    vendaId = 0,
                    produtoId = item.produtoId,
                    quantidade = item.quantidade,
                    precoUnitario = item.precoUnitario,
                    totalItem = item.precoUnitario.multiply(item.quantidade.toBigDecimal())
                )
            }
            call.respond(HttpStatusCode.Created, service.createVenda(venda, itens))
        }
    }
}

data class CreateVendaRequest(
    val clienteId: Int,
    val itens: List<CreateItemVendaRequest>
)

data class CreateItemVendaRequest(
    val produtoId: Int,
    val quantidade: Int,
    val precoUnitario: BigDecimal
)