package com.myapp.demo.service;

import com.myapp.demo.entity.InventoryStock;
import com.myapp.demo.notifier.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for checking inventory levels and sending alerts
 */
@Service
public class AlertService {

    @Value("${inventory.alert.threshold:2}")
    private Integer alertThreshold;

    @Autowired
    private List<Notifier> notifiers;

    /**
     * Check if item is below threshold and send notifications if needed
     * @param item the inventory item to check
     */
    public void checkAndNotify(InventoryStock item) {
        if (item.getQuantity() < alertThreshold) {
            notifyAll(item);
        }
    }

    /**
     * Send notification through all configured notifiers
     * @param item the inventory item to notify about
     */
    private void notifyAll(InventoryStock item) {
        if (notifiers != null && !notifiers.isEmpty()) {
            notifiers.forEach(notifier -> notifier.notify(item));
        }
    }
}
