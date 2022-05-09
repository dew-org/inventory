package com.dew.inventory.infrastructure.persistence.mongo

import com.dew.inventory.domain.InventoryRepository
import com.dew.inventory.domain.ProductId
import com.dew.inventory.domain.ProductInventory
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoCollection
import jakarta.inject.Singleton
import org.bson.Document
import reactor.core.publisher.Mono

@Singleton
class MongoDbInventoryRepository(
    private val mongoDbConfiguration: MongoDbConfiguration, private val mongoClient: MongoClient
) : InventoryRepository {

    override fun save(productInventory: ProductInventory): Mono<Boolean> =
        Mono.from(collection.insertOne(productInventory.toDocument())).map { true }.onErrorReturn(false)

    override fun find(codeOrSku: String): Mono<ProductInventory> = Mono.from(Mono.from(
        collection.find(
            Filters.or(
                Filters.eq("_id.code", codeOrSku), Filters.eq("_id.sku", codeOrSku)
            )
        ).first()
    ).mapNotNull { it.toProductInventory() })

    /**
     * Decrease the stock of a product by subtracting the quantity of the product
     * @param codeOrSku the code or sku of the product
     * @param quantity the quantity to be subtracted
     */
    override fun decreaseStock(codeOrSku: String, quantity: Int): Mono<Boolean> = Mono.from(
        collection.findOneAndUpdate(
            Filters.or(
                Filters.eq("_id.code", codeOrSku), Filters.eq("_id.sku", codeOrSku)
            ), Updates.combine(Updates.inc("stock", -quantity), Updates.currentDate("updatedAt"))
        )
    ).map { true }.onErrorReturn(false)

    private val collection: MongoCollection<Document>
        get() = mongoClient
            .getDatabase(mongoDbConfiguration.name)
            .getCollection(mongoDbConfiguration.collection)

    private fun ProductInventory.toDocument(): Document =
        Document().append("_id", id.toDocument()).append("stock", stock)

    private fun ProductId.toDocument(): Document = Document("code", code).append("sku", sku)

    private fun Document.toProductInventory(): ProductInventory {
        val product = ProductInventory(
            get("_id", Document::class.java).toProductId(),
            getInteger("stock")
        )

        product.updatedAt = getDate("updatedAt")

        return product
    }

    private fun Document.toProductId(): ProductId {
        return ProductId(getString("code"), getString("sku"))
    }
}