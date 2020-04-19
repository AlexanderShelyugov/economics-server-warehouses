package com.alexander.economics.service.warehouse.converter;

import com.alexander.economics.service.warehouse.dto.WarehouseDTO;
import com.alexander.economics.service.warehouse.model.Warehouse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static com.alexander.economics.service.warehouse.helper.TestHelper.randomWarehouse;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(classes = {ModelMapperWarehouseConverter.class})
@Import(ConverterConfiguration.class)
class ModelMapperWarehouseConverterTest {

    @Autowired
    private WarehouseConverter converter;

    @Test
    void contextLoads() {
        assertThat(converter, is(notNullValue()));
        assertThat(converter, is(instanceOf(ModelMapperWarehouseConverter.class)));
    }

    @Test
    void convert() {
        final Warehouse warehouse = randomWarehouse();
        final WarehouseDTO expectedDTO = new WarehouseDTO(warehouse.getUuid().toString(), warehouse.getName(),
            warehouse.getLatitude(), warehouse.getLongitude(), warehouse.getCapacity());
        assertThat(converter.convert(warehouse), is(equalTo(expectedDTO)));
    }
}