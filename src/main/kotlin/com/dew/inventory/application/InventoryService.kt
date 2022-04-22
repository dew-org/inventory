package com.dew.inventory.application

import com.dew.inventory.application.create.CreateProductInventoryCommand
import com.dew.inventory.domain.InventoryRepository
import com.dew.inventory.domain.ProductInventory
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

@Singleton
class InventoryService(private val inventoryRepository: InventoryRepository) {

    fun save(request: CreateProductInventoryCommand): Mono<Boolean> {
        val productInventory = ProductInventory(
            request.code, request.sku, request.stock
        )

        return inventoryRepository.save(productInventory)
    }

    fun find(codeOrSku: String): Mono<ProductInventoryResponse> {
        return inventoryRepository.find(codeOrSku).mapNotNull { product ->
            ProductInventoryResponse(
                product.code, product.sku, product.stock, product.updatedAt
            )
        }
    }

    /**
     * Decrease the stock of a product by subtracting the quantity of the product
     * @param codeOrSku the code or sku of the product
     * @param quantity the quantity to be subtracted
     */
    fun decreaseStock(codeOrSku: String, quantity: Int): Mono<Boolean> =
        inventoryRepository.decreaseStock(codeOrSku, quantity)
}