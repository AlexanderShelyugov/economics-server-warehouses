package com.alexander.economics.service.warehouse.service;

import com.alexander.economics.service.warehouse.model.Warehouse;
import com.alexander.economics.service.warehouse.repository.WarehouseRepository;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Service layer implementation for working with warehouses
 */
@Service
@AllArgsConstructor
@Transactional
class WarehouseServiceImpl implements WarehouseService {
    @NonNull
    private final WarehouseRepository r;

    /**
     * Returns all warehouses
     */
    @Override
    @Transactional(readOnly = true)
    public Collection<Warehouse> getWarehouses() {
        return r.findAll();
    }

    @Override
    public Warehouse getById(@NonNull Long id) {
        return r.findById(id).orElse(null);
    }

    @Override
    public Warehouse getByUuid(@NonNull UUID uuid) {
        return r.findByUuid(uuid).orElse(null);
    }

    @Override
    public Warehouse createWarehouse(@NonNull String name,
                                     @NonNull Double latitude,
                                     @NonNull Double longitude,
                                     @NonNull Integer capacity) {
        final Warehouse w = new Warehouse(null, randomUUID(), name, latitude, longitude, capacity);
        return r.save(w);
    }

    @Override
    public void saveWarehouse(@NonNull Warehouse w) {
        r.save(w);
    }

    @Override
    public void removeWarehouse(@NonNull Long id) {
        r.deleteById(id);
    }
}
