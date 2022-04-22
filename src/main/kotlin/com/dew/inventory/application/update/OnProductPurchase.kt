package com.dew.inventory.application.update

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.inventory.application.InventoryService
import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

@Requires(notEnv = [Environment.TEST])
@KafkaListener
class OnProductPurchase(private val inventoryService: InventoryService) {

    private val logger: Logger = LoggerFactory.getLogger(OnProductPurchase::class.java)

    @Topic("product-purchase")
    fun productPurchase(products: List<PurchasedProduct>) {
        products.map {
            inventoryService.decreaseStock(it.sku, it.quantity).flatMap { updated ->
                if (!updated) {
                    logger.error("Failed to update stock for sku: ${it.sku}")
                }

                Mono.just(updated)
            }
        }
    }
}