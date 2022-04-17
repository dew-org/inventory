package com.dew

import com.dew.inventory.application.InventoryService
import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import reactor.core.publisher.Mono
import javax.validation.Valid

@Controller("/inventory")
open class InventoryController(
    private val inventoryService: InventoryService
) {

    @Post
    open fun save(@Valid request: CreateProductInventoryCommand): Mono<HttpStatus> {
        return inventoryService.save(request)
            .map { added: Boolean -> if (added) HttpStatus.CREATED else HttpStatus.CONFLICT }
    }
}