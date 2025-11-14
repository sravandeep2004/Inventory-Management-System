package com.myapp.demo.notifier;

import com.myapp.demo.entity.InventoryStock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Console-based notifier that logs low-stock alerts to the console
 */
@Component
public class ConsoleNotifier implements Notifier {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleNotifier.class);

    @Override
    public void notify(InventoryStock item) {
        logger.warn("=== LOW STOCK ALERT ===");
        logger.warn("Product: {} (ID: {})", item.getProductName(), item.getProductId());
        logger.warn("Current Quantity: {}", item.getQuantity());
        logger.warn("Price Per Unit: {}", item.getPricePerUnit());
        logger.warn("Total Price: {}", item.getTotalPrice());
        logger.warn("========================");
    }
}
