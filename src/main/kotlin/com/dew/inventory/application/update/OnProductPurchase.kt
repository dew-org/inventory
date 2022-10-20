package com.dew.inventory.application.update

import com.dew.common.domain.invoices.PurchasedProduct
import com.dew.inventory.application.InventoryService
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import io.micronaut.gcp.pubsub.annotation.PubSubListener
import io.micronaut.gcp.pubsub.annotation.Subscription
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono

@Requires(notEnv = [Environment.TEST])
@PubSubListener
class OnProductPurchase(private val inventoryService: InventoryService) {

    private val logger: Logger = LoggerFactory.getLogger(OnProductPurchase::class.java)

    @Subscription("product-purchase")
    fun productPurchase(product: Mono<PurchasedProduct>): Mono<PurchasedProduct> {
        return product.flatMap {
            inventoryService.decreaseStock(it.code, it.quantity).flatMap { updated: Boolean ->
                if (!updated) {
                    logger.error("Failed to update stock for product ${it.code}")
                }

                Mono.just(it)
            }
        }
    }
}