package com.alexander.economics.service.warehouse.dto;

import lombok.Value;

/**
 * Warehouse's data transfer object
 */
@Value
public class WarehouseDTO {
    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private int capacity;
}
