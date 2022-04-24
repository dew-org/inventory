package com.dew

import com.dew.inventory.application.InventoryService
import com.dew.inventory.application.create.CreateProductInventoryCommand
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.testcontainers.spock.Testcontainers
import spock.lang.Specification

@MicronautTest
@Testcontainers
class InventoryServiceSpec extends Specification implements TestPropertyProvider {

    @Inject
    InventoryService inventoryService

    def "interact with inventory service"() {
        when:
        var request = new CreateProductInventoryCommand("product-1", "321", 50)
        inventoryService.save(request).block()
        var inventory = inventoryService.find("product-1").block()

        then:
        inventory != null
        inventory.stock == 50

        when:
        inventoryService.decreaseStock("product-1", 10).block()
        inventory = inventoryService.find("product-1").block()

        then:
        inventory.stock == 40
        inventory.updatedAt != null
    }

    @Override
    Map<String, String> getProperties() {
        InventoryControllerSpec.mongo.start()

        return ["mongodb.uri": InventoryControllerSpec.mongo.replicaSetUrl]
    }
}
