package com.alexander.economics.service.warehouse.converter;

import com.alexander.economics.service.warehouse.dto.WarehouseDTO;
import com.alexander.economics.service.warehouse.model.Warehouse;
import lombok.NonNull;

/**
 * An interface for warehouse's converter
 */
public interface WarehouseConverter {
    /**
     * Converts warehouse entity to warehouse data transfer object
     *
     * @param warehouse warehouse entity
     * @return warehouse data transfer object
     * @throws NullPointerException if warehouse is null
     */
    WarehouseDTO convert(@NonNull Warehouse warehouse);
}
