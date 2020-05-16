package com.alexander.economics.service.warehouse.controller;

import com.alexander.economics.service.warehouse.converter.WarehouseConverter;
import com.alexander.economics.service.warehouse.dto.WarehouseDTO;
import com.alexander.economics.service.warehouse.model.Warehouse;
import com.alexander.economics.service.warehouse.service.WarehouseService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.UUID;

import static java.lang.String.valueOf;
import static java.util.UUID.fromString;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.notFound;
import static org.springframework.http.ResponseEntity.ok;

/**
 * Service, that provides an information about warehouses
 */
@RestController
@RequestMapping(value = "/warehouses", produces = {APPLICATION_JSON_VALUE})
@Slf4j
@AllArgsConstructor
class WarehouseController {
    @NonNull
    private final WarehouseService service;
    @NonNull
    private final WarehouseConverter converter;

    /**
     * Returns all warehouses
     *
     * @return all warehouses
     */
    @GetMapping
    ResponseEntity<Collection<WarehouseDTO>> getWarehouses() {
        Collection<Warehouse> warehouses = service.getWarehouses();
        return ok(warehouses.stream().map(converter::convert).collect(toUnmodifiableSet()));
    }

    @GetMapping("/{warehouseId}")
    ResponseEntity<WarehouseDTO> getWarehouse(@PathVariable("warehouseId") String id) {
        final Warehouse w = service.getByUuid(fromString(id));
        if (w == null) {
            return notFound().build();
        }
        return ok(converter.convert(w));
    }

    /**
     * Adds new warehouse
     */
    @PostMapping(consumes = {APPLICATION_JSON_VALUE})
    ResponseEntity<String> createWarehouse(@RequestBody WarehouseDTO w)
        throws URISyntaxException {
        if (w == null || w.getName() == null || w.getName().isBlank()) {
            return badRequest().body("Not enough parameters");
        }
        final Warehouse warehouse = service.createWarehouse(w.getName(), w.getLatitude(), w.getLongitude(),
            w.getCapacity());
        return created(new URI(valueOf(warehouse.getUuid()))).body("Ok");
    }

    /**
     * Updates warehouse
     */
    @PutMapping(value = "/{warehouseId}", consumes = {APPLICATION_JSON_VALUE})
    ResponseEntity<String> updateWarehouse(@PathVariable("warehouseId") String id,
                                           @RequestBody WarehouseDTO w)
        throws URISyntaxException {
        final Warehouse warehouse = service.getByUuid(fromString(id));
        final UUID savedId;
        if (warehouse == null) {
            final Warehouse savedWarehouse = service.createWarehouse(
                w.getName(), w.getLatitude(), w.getLongitude(), w.getCapacity());
            savedId = savedWarehouse.getUuid();
        } else {
            service.saveWarehouse(new Warehouse(
                warehouse.getId(), warehouse.getUuid(), w.getName(), w.getLatitude(), w.getLongitude(),
                w.getCapacity()
            ));
            savedId = warehouse.getUuid();
        }
        if (savedId == null || !savedId.toString().equals(id)) {
            throw new IllegalStateException();
        }
        return warehouse == null ?
            created(new URI(valueOf(savedId))).body("") :
            ok("");
    }

    @DeleteMapping("/{warehouseId}")
    ResponseEntity<String> deleteWarehouse(@PathVariable("warehouseId") String id) {
        if (id == null || id.isBlank()) {
            return badRequest().body("Please provide entity id");
        }
        final Warehouse w = service.getByUuid(fromString(id));
        if (w == null) {
            return notFound().build();
        }
        service.removeWarehouse(w.getId());
        return noContent().build();
    }
}
