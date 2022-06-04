package com.dew.inventory.domain

import reactor.core.publisher.Mono
import javax.validation.Valid
import javax.validation.constraints.NotBlank

interface InventoryRepository {

    fun save(@Valid productInventory: ProductInventory): Mono<Boolean>

    fun find(@NotBlank codeOrSku: String): Mono<ProductInventory>

    /**
     * Decrease the stock of a product by subtracting the quantity of the product
     * @param codeOrSku the code or sku of the product
     * @param quantity the quantity to be subtracted
     */
    fun decreaseStock(@NotBlank codeOrSku: String, quantity: Int): Mono<Boolean>

    fun updateStock(@NotBlank codeOrSku: String, newStock: Int): Mono<Boolean>
}