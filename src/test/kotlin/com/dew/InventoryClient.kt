package com.dew

import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import javax.validation.Valid

@Client("/inventory")
interface InventoryClient {

    @Post
    fun save(@Valid request: CreateProductInventoryCommand): HttpStatus
}