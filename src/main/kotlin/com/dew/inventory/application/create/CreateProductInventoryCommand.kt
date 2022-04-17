package com.dew.inventory.application.create

import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank

@Introspected
data class CreateProductInventoryCommand(
    @field:NotBlank val code: String,
    @field:NotBlank val sku: String,
    @field:NotBlank val stock: Int
)
