package com.alexander.economics.service.warehouse.repository;

import com.alexander.economics.service.warehouse.model.Warehouse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collection;

import static com.alexander.economics.service.warehouse.helper.TestHelper.randomWarehouse;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@DataJpaTest
class WarehouseRepositoryTest {
    @Autowired
    private WarehouseRepository r;

    @Test
    void findByUuid() {
        final Warehouse expectedWarehouse = randomWarehouse();
        r.save(expectedWarehouse);
        final Warehouse actualWarehouse = r.findByUuid(expectedWarehouse.getUuid()).orElse(null);
        assertThat(actualWarehouse, is(samePropertyValuesAs(expectedWarehouse, "id")));
    }

    @Test
    void toStringTest() {
        assertThat(randomWarehouse().toString(), is(notNullValue()));
    }

    @Test
    void uuidIsUnique() {
        final Warehouse warehouse1 = randomWarehouse();
        final Warehouse warehouse2 = randomWarehouse();
        setField(warehouse2, "uuid", warehouse1.getUuid());
        r.save(warehouse1);
        assertThat(warehouse1.getUuid(), is(equalTo(warehouse2.getUuid())));
        assertThrows(DataIntegrityViolationException.class, () -> r.save(warehouse2));
    }

    @Test
    void noNullsAllowed() {
        final Collection<String> nullProps = unmodifiableCollection(asList("uuid", "name", "latitude", "longitude"));
        nullProps.forEach((prop) -> {
            final Warehouse w = randomWarehouse();
            r.save(w);
            setField(w, prop, null);
            assertThrows(DataIntegrityViolationException.class, () -> r.save(w));
        });
    }
}