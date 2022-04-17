package com.dew.inventory.infrastructure.persistence.mongo

import com.dew.inventory.domain.InventoryRepository
import com.dew.inventory.domain.ProductInventory
import com.mongodb.client.model.Filters
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import reactor.core.publisher.Mono

@Singleton
class MongoDbInventoryRepository(
    private val mongoDbConfiguration: MongoDbConfiguration, private val mongoClient: MongoClient
) : InventoryRepository {

    override fun save(productInventory: ProductInventory): Mono<Boolean> =
        Mono.from(collection.insertOne(productInventory)).map { true }.onErrorReturn(false)

    override fun find(codeOrSku: String): Mono<ProductInventory> = Mono.from(
        collection.find(
            Filters.or(
                Filters.eq("code", codeOrSku), Filters.eq("sku", codeOrSku)
            )
        ).first()
    )

    private val collection: MongoCollection<ProductInventory>
        get() = mongoClient.getDatabase(mongoDbConfiguration.name)
            .getCollection(mongoDbConfiguration.collection, ProductInventory::class.java)
}