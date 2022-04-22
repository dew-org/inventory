package com.dew.inventory.domain

import io.micronaut.core.annotation.Creator
import io.micronaut.core.annotation.Introspected
import org.bson.codecs.pojo.annotations.BsonCreator
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty
import java.util.*
import javax.validation.constraints.NotBlank

@Introspected
data class ProductInventory @Creator @BsonCreator constructor(
    @field:BsonId @field:BsonProperty("_id") @param:BsonProperty("_id") val id: ProductId,
    @field:BsonProperty("stock") @param:BsonProperty("stock") @field:NotBlank val stock: Int
) {
    @field:BsonProperty("updatedAt")
    var updatedAt: Date? = null
}
