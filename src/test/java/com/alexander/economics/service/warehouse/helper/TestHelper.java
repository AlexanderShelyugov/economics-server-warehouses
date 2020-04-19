package com.alexander.economics.service.warehouse.helper;

import com.alexander.economics.service.warehouse.model.Warehouse;

import java.util.Collection;
import java.util.Random;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static java.util.stream.Stream.generate;

public class TestHelper {
    public static final Random RANDOM = new Random();
    private static final int LATITUDE = 90;
    private static final int LONGITUDE = 180;
    private static final int MAX_CAPACITY = 200;

    private static final int NUMBER_OF_WAREHOUSES = 25;

    public static Warehouse randomWarehouse() {
        return new Warehouse(RANDOM.nextLong(),
            randomUUID(),
            randomUUID().toString(),
            RANDOM.nextDouble() * LATITUDE * 2 - LATITUDE,
            RANDOM.nextDouble() * LONGITUDE * 2 - LONGITUDE,
            RANDOM.nextInt(MAX_CAPACITY));
    }

    public static Collection<Warehouse> randomWarehouses() {
        return generate(TestHelper::randomWarehouse).limit(NUMBER_OF_WAREHOUSES)
            .collect(toUnmodifiableSet());
    }
}
