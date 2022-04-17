package com.dew

import com.dew.inventory.application.ProductInventoryResponse
import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client
import reactor.core.publisher.Mono
import javax.validation.Valid

@Client("/inventory")
interface InventoryClient {

    @Post
    fun save(@Valid request: CreateProductInventoryCommand): HttpStatus

    @Get("/{codeOrSku}")
    fun find(codeOrSku: String): HttpResponse<ProductInventoryResponse>
}