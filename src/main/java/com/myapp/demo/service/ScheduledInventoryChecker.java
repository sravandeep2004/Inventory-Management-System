package com.myapp.demo.service;

import com.myapp.demo.entity.InventoryStock;
import com.myapp.demo.repository.InventoryStockRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Scheduled component that runs every minute to check for low-stock items
 * and sends notifications if any items are below the threshold
 */
@Component
public class ScheduledInventoryChecker {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledInventoryChecker.class);

    @Value("${inventory.alert.threshold:2}")
    private Integer alertThreshold;

    @Autowired
    private InventoryStockRepository inventoryRepository;

    @Autowired
    private AlertService alertService;

    // Track which products have already been alerted to prevent duplicate emails
    private Set<Long> alertedProducts = new HashSet<>();

    /**
     * Run every minute (60000 milliseconds) to check for low-stock items
     * Initial delay of 5 seconds to allow application to start up
     */
    @Scheduled(fixedRate = 60000, initialDelay = 5000)
    public void checkLowStockItems() {
        logger.debug("Running scheduled inventory check with threshold: {}", alertThreshold);

        try {
            List<InventoryStock> lowStockItems = inventoryRepository.findByQuantityLessThan(alertThreshold);

            if (!lowStockItems.isEmpty()) {
                logger.info("Found {} items below threshold", lowStockItems.size());
                
                // Check each low stock item
                for (InventoryStock item : lowStockItems) {
                    // Only send alert if we haven't already alerted for this product
                    if (!alertedProducts.contains(item.getProductId())) {
                        logger.info("Sending alert for product: {} (Qty: {})", item.getProductName(), item.getQuantity());
                        alertService.checkAndNotify(item);
                        alertedProducts.add(item.getProductId()); // Mark as alerted
                    } else {
                        logger.debug("Alert already sent for product: {}", item.getProductName());
                    }
                }
            } else {
                logger.debug("No items below threshold found");
                // Clear the alerted set when no low stock items exist
                alertedProducts.clear();
            }
        } catch (Exception e) {
            logger.error("Error occurred during scheduled inventory check", e);
        }
    }

    /**
     * Clear alert for a specific product when stock is replenished
     */
    public void clearProductAlert(Long productId) {
        if (alertedProducts.contains(productId)) {
            alertedProducts.remove(productId);
            logger.info("Alert cleared for product ID: {}", productId);
        }
    }
}
