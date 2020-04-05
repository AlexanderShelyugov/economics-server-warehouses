package com.alexander.economics.service.warehouse.repository;

import com.alexander.economics.service.warehouse.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository for warehouses
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findByUuid(UUID uuid);
}
