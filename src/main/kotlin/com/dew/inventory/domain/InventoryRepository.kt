package com.dew.inventory.domain

import reactor.core.publisher.Mono
import javax.validation.Valid

interface InventoryRepository {

    fun save(@Valid productInventory: ProductInventory): Mono<Boolean>
}