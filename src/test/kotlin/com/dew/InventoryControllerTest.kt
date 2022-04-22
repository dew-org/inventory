package com.dew

import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.testcontainers.containers.MongoDBContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.utility.DockerImageName

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryControllerTest : TestPropertyProvider {

    companion object {
        @Container
        val mongo: MongoDBContainer = MongoDBContainer(DockerImageName.parse("mongo:latest")).withExposedPorts(27017)
    }

    @Inject
    lateinit var inventoryClient: InventoryClient

    @Test
    fun interact_with_inventory() {
        val productInventory = CreateProductInventoryCommand(
            "123", "123-CEL", 20
        )

        val status = inventoryClient.save(productInventory)

        assertEquals(HttpStatus.CREATED, status)

        var response = inventoryClient.find(productInventory.sku)

        assertEquals(HttpStatus.OK, response.status)
        assertEquals(productInventory.sku, response.body()!!.sku)

        response = inventoryClient.find("321-CEL")

        assertEquals(HttpStatus.NOT_FOUND, response.status)

        try {
            inventoryClient.save(productInventory)
        } catch (e: HttpClientResponseException) {
            assertEquals(HttpStatus.CONFLICT, e.status)
        }
    }

    override fun getProperties(): Map<String, String> {
        if (!mongo.isRunning) {
            mongo.start()
        }

        return mapOf("mongodb.uri" to mongo.replicaSetUrl)
    }
}