package com.myapp.demo.repository;

import com.myapp.demo.entity.InventoryStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for InventoryStock entity
 */
@Repository
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {
    /**
     * Find all inventory items with quantity less than the given threshold
     * Used for alert notifications
     */
    List<InventoryStock> findByQuantityLessThan(Integer threshold);
}
