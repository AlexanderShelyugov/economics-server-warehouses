package com.alexander.economics.service.warehouse.service;

import com.alexander.economics.service.warehouse.model.Warehouse;

import java.util.Collection;
import java.util.UUID;

/**
 * An interface for work with warehouses
 */
public interface WarehouseService {
    /**
     * Get all the warehouses
     *
     * @return all warehouses
     */
    Collection<Warehouse> getWarehouses();

    /**
     * Gets warehouse with specified id
     *
     * @param id id
     * @return warehouse with this id, null otherwise
     */
    Warehouse getById(Long id);

    /**
     * Gets warehouse by uuid
     *
     * @param uuid uuid
     * @return warehouse with this uuid, null otherwise
     */
    Warehouse getByUuid(UUID uuid);

    /**
     * Creates a new warehouse
     *
     * @param name      name
     * @param latitude  latitude
     * @param longitude longitude
     * @return new warehouse
     */
    Warehouse createWarehouse(String name, Double latitude, Double longitude);

    /**
     * Saves warehouse entity
     *
     * @param w warehouse
     */
    void saveWarehouse(Warehouse w);

    /**
     * Removes warehouse
     *
     * @param id id
     */
    void removeWarehouse(Long id);
}
