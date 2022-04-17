package com.dew.inventory.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.*
import javax.validation.constraints.NotBlank

@Introspected
data class ProductInventory @Creator @BsonCreator constructor(
    @field:BsonProperty("code") @param:BsonProperty("code") @field:NotBlank val code: String,
    @field:BsonProperty("sku") @param:BsonProperty("sku") @field:NotBlank val sku: String,
    @field:BsonProperty("stock") @param:BsonProperty("stock") @field:NotBlank val stock: Int
) {
    @field:BsonProperty("updatedAt")
    var updatedAt: Date? = null
}
