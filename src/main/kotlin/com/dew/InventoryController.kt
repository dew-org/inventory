package com.dew

import com.dew.inventory.application.InventoryService
import com.dew.inventory.application.ProductInventoryResponse
import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import reactor.core.publisher.Mono
import javax.validation.Valid

@Controller("/inventory")
@Secured(SecurityRule.IS_AUTHENTICATED)
open class InventoryController(
    private val inventoryService: InventoryService
) {

    @Post
    open fun save(@Valid request: CreateProductInventoryCommand): Mono<HttpStatus> {
        return inventoryService.save(request)
            .map { added: Boolean -> if (added) HttpStatus.CREATED else HttpStatus.CONFLICT }
    }

    @Get("/{codeOrSku}")
    open fun find(codeOrSku: String): Mono<HttpResponse<ProductInventoryResponse>> {
        return inventoryService.find(codeOrSku).map { product: ProductInventoryResponse? ->
            if (product != null) HttpResponse.ok(product) else HttpResponse.notFound()
        }
    }

    @Put("/{codeOrSku}/stock/{newStock}")
    open fun updateStock(codeOrSku: String, newStock: Int): Mono<HttpStatus> {
        return inventoryService.updateStock(codeOrSku, newStock)
            .map { updated: Boolean -> if (updated) HttpStatus.OK else HttpStatus.NOT_FOUND }
    }
}