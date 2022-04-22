package com.dew

import com.dew.inventory.application.InventoryService
import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class InventoryServiceTest : TestPropertyProvider {

    @Inject
    lateinit var inventoryService: InventoryService

    @Test
    fun interact_with_inventory_service() {
        val request = CreateProductInventoryCommand("321", "product", 50)
        inventoryService.save(request).block()

        var productInventory = inventoryService.find("321").block()

        Assertions.assertNotNull(productInventory)
        Assertions.assertEquals(50, productInventory!!.stock)

        val result = inventoryService.decreaseStock("321", 20).block()

        Assertions.assertTrue(result!!)

        productInventory = inventoryService.find("321").block()
        Assertions.assertEquals(30, productInventory!!.stock)
    }

    override fun getProperties(): MutableMap<String, String> {
        if (!InventoryControllerTest.mongo.isRunning) {
            InventoryControllerTest.mongo.start()
        }

        return mutableMapOf(
            "mongodb.uri" to InventoryControllerTest.mongo.replicaSetUrl
        )
    }
}