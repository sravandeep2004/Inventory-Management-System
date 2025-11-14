package com.myapp.demo.service;

import com.myapp.demo.entity.InventoryStock;
import com.myapp.demo.entity.Status;
import com.myapp.demo.notifier.Notifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.Mockito.*;

/**
 * Unit tests for AlertService
 * Tests that alerts are triggered correctly when inventory is below threshold
 */
@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private Notifier mockNotifier1;

    @Mock
    private Notifier mockNotifier2;

    @InjectMocks
    private AlertService alertService;

    private InventoryStock lowStockItem;
    private InventoryStock normalStockItem;

    @BeforeEach
    void setUp() {
        // Set alert threshold to 2
        ReflectionTestUtils.setField(alertService, "alertThreshold", 2);

        lowStockItem = InventoryStock.builder()
                .productId(1L)
                .productName("Low Stock Item")
                .pricePerUnit(new BigDecimal("10.00"))
                .quantity(1)  // Below threshold of 2
                .status(Status.ACTIVE)
                .build();

        normalStockItem = InventoryStock.builder()
                .productId(2L)
                .productName("Normal Stock Item")
                .pricePerUnit(new BigDecimal("20.00"))
                .quantity(5)  // Above threshold
                .status(Status.ACTIVE)
                .build();

        // Inject mocked notifiers list
        ReflectionTestUtils.setField(alertService, "notifiers", Arrays.asList(mockNotifier1, mockNotifier2));
    }

    @Test
    void testCheckAndNotifyTriggersAlertWhenBelowThreshold() {
        // When
        alertService.checkAndNotify(lowStockItem);

        // Then - both notifiers should be called
        verify(mockNotifier1, times(1)).notify(lowStockItem);
        verify(mockNotifier2, times(1)).notify(lowStockItem);
    }

    @Test
    void testCheckAndNotifyDoesNotTriggerAlertWhenAboveThreshold() {
        // When
        alertService.checkAndNotify(normalStockItem);

        // Then - no notifiers should be called
        verify(mockNotifier1, never()).notify(normalStockItem);
        verify(mockNotifier2, never()).notify(normalStockItem);
    }

    @Test
    void testCheckAndNotifyTriggersAlertAtExactThreshold() {
        // Given
        InventoryStock atThresholdItem = InventoryStock.builder()
                .productId(3L)
                .productName("At Threshold Item")
                .pricePerUnit(new BigDecimal("15.00"))
                .quantity(2)  // Exactly at threshold
                .status(Status.ACTIVE)
                .build();

        // When
        alertService.checkAndNotify(atThresholdItem);

        // Then - notifiers should not be called (only when < threshold, not <=)
        verify(mockNotifier1, never()).notify(atThresholdItem);
        verify(mockNotifier2, never()).notify(atThresholdItem);
    }
}
