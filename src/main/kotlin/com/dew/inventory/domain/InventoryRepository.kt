package com.dew.inventory.domain

import reactor.core.publisher.Mono
import javax.validation.Valid
import javax.validation.constraints.NotBlank

interface InventoryRepository {

    fun save(@Valid productInventory: ProductInventory): Mono<Boolean>

    fun find(@NotBlank codeOrSku: String): Mono<ProductInventory>
}