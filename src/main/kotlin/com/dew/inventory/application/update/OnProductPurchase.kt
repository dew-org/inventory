package com.dew.inventory.application.update

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.inventory.application.InventoryService
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.gcp.pubsub.annotation.PubSubListener
import io.micronaut.gcp.pubsub.annotation.Subscription
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Requires(notEnv = [Environment.TEST])
@PubSubListener
class OnProductPurchase(private val inventoryService: InventoryService) {

    private val logger: Logger = LoggerFactory.getLogger(OnProductPurchase::class.java)

    @Subscription("product-purchase")
    fun productPurchase(products: List<PurchasedProduct>) {
        products.forEach { product ->
            val result = inventoryService.decreaseStock(product.code, product.quantity).block()

            if (result != null && !result) {
                logger.error("Failed to update inventory for product ${product.code}")
            }
        }
    }
}