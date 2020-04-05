package com.alexander.economics.service.warehouse.service;

import com.alexander.economics.service.warehouse.model.Warehouse;
import com.alexander.economics.service.warehouse.repository.WarehouseRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Collection;
import java.util.UUID;

import static com.alexander.economics.service.warehouse.helper.TestHelper.randomWarehouse;
import static com.alexander.economics.service.warehouse.helper.TestHelper.randomWarehouses;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static java.util.List.copyOf;
import static java.util.Optional.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentCaptor.forClass;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {WarehouseServiceImpl.class})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class WarehouseServiceImplTest {
    @MockBean
    private WarehouseRepository r;

    @Autowired
    private WarehouseService service;

    @AfterEach
    void checkAfter() {
        verifyNoMoreInteractions(r);
    }

    @Test
    void getWarehouses() {
        final Collection<Warehouse> expectedWarehouses = randomWarehouses();
        when(r.findAll()).thenReturn(copyOf(expectedWarehouses));
        final Collection<Warehouse> actualWarehouses = service.getWarehouses();
        verify(r, times(1)).findAll();
        assertThat(actualWarehouses, containsInAnyOrder(expectedWarehouses.toArray()));
    }

    @Test
    void getById() {
        final Warehouse expectedWarehouse = randomWarehouse();
        final Long id = expectedWarehouse.getId();
        when(r.findById(id)).thenReturn(of(expectedWarehouse));

        final Warehouse actualWarehouse = service.getById(id);

        verify(r, times(1)).findById(eq(id));
        assertThat(actualWarehouse, is(equalTo(expectedWarehouse)));
    }

    @Test
    void getByUuid() {
        final Warehouse expectedWarehouse = randomWarehouse();
        final UUID uuid = expectedWarehouse.getUuid();
        when(r.findByUuid(uuid)).thenReturn(of(expectedWarehouse));

        final Warehouse actualWarehouse = service.getByUuid(uuid);

        verify(r, times(1)).findByUuid(eq(uuid));

        assertThat(actualWarehouse, is(equalTo(expectedWarehouse)));
    }

    @Test
    void createWarehouse() {
        final Warehouse expectedWarehouse = randomWarehouse();
        when(r.save(any(Warehouse.class))).thenReturn(expectedWarehouse);

        final Long actualWarehouseId = service.createWarehouse(
            expectedWarehouse.getName(), expectedWarehouse.getLatitude(), expectedWarehouse.getLongitude()).getId();

        final ArgumentCaptor<Warehouse> w = forClass(Warehouse.class);
        verify(r, times(1)).save(w.capture());
        assertThat(actualWarehouseId, is(equalTo(expectedWarehouse.getId())));
        assertThat(w.getValue().getUuid(), is(notNullValue()));
        assertThat(w.getValue().getName(), is(equalTo(expectedWarehouse.getName())));
        assertThat(w.getValue().getLatitude(), is(equalTo(expectedWarehouse.getLatitude())));
        assertThat(w.getValue().getLongitude(), is(equalTo(expectedWarehouse.getLongitude())));
    }

    @Test
    void saveWarehouse() {
        final Warehouse warehouse = randomWarehouse();

        service.saveWarehouse(warehouse);

        verify(r, times(1)).save(eq(warehouse));
    }

    @Test
    void removeWarehouse() {
        final Long id = randomWarehouse().getId();
        service.removeWarehouse(id);
        verify(r, times(1)).deleteById(eq(id));
    }

    @Test
    void noNulls() {
        final Warehouse w = randomWarehouse();
        final Collection<Runnable> cases = unmodifiableCollection(asList(
            () -> service.getById(null),
            () -> service.getByUuid(null),
            () -> service.createWarehouse(w.getName(), w.getLatitude(), null),
            () -> service.createWarehouse(w.getName(), null, w.getLongitude()),
            () -> service.createWarehouse(null, w.getLatitude(), w.getLongitude()),
            () -> service.saveWarehouse(null),
            () -> service.removeWarehouse(null)
        ));

        cases.forEach((nullCase) -> {
            try {
                nullCase.run();
                fail();
            } catch (NullPointerException e) {
                // ok
            }
        });
    }
}