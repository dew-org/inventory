package com.dew.inventory.application

import io.micronaut.core.annotation.Introspected
import java.util.*

@Introspected
data class ProductInventoryResponse(
    val code: String,
    val sku: String,
    val stock: Int,
    var updatedAt: Date?
)
