package com.myapp.demo.notifier;

import com.myapp.demo.entity.InventoryStock;

/**
 * Interface for sending notifications about low-stock items
 */
public interface Notifier {
    /**
     * Send a notification about a low-stock product
     * @param item the inventory item with low stock
     */
    void notify(InventoryStock item);
}
