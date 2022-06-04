package com.dew

import com.dew.inventory.application.ProductInventoryResponse
import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.client.annotation.Client

import javax.validation.Valid

@Client("/inventory")
interface InventoryClient {

    @Post
    HttpStatus save(@Valid CreateProductInventoryCommand request)

    @Get("/{codeOrSku}")
    HttpResponse<ProductInventoryResponse> find(String codeOrSku)

    @Put("/{codeOrSku}/stock/{newStock}")
    HttpStatus updateStock(String codeOrSku, int newStock)
}