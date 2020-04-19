package com.alexander.economics.service.warehouse.controller;

import com.alexander.economics.service.warehouse.converter.ConverterConfiguration;
import com.alexander.economics.service.warehouse.converter.WarehouseConverter;
import com.alexander.economics.service.warehouse.dto.WarehouseDTO;
import com.alexander.economics.service.warehouse.model.Warehouse;
import com.alexander.economics.service.warehouse.service.WarehouseService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collection;
import java.util.UUID;

import static com.alexander.economics.service.warehouse.helper.TestHelper.randomWarehouse;
import static com.alexander.economics.service.warehouse.helper.TestHelper.randomWarehouses;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toUnmodifiableSet;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(ConverterConfiguration.class)
@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {
    private static final String URI = "/warehouses";
    private static final String NAME_PARAM = "name";
    private static final String LATITUDE_PARAM = "lat";
    private static final String LONGITUDE_PARAM = "long";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseService service;

    @Autowired
    private ObjectMapper mapper;

    @SpyBean
    private WarehouseConverter converter;

    @Test
    @DisplayName("Context loads successfully!")
    void contextLoads() {
        assertThat(mockMvc, is(notNullValue()));
        assertThat(mapper, is(notNullValue()));
    }

    @AfterEach
    void checkAfter() {
        verifyNoMoreInteractions(service);
    }

    @Test
    @DisplayName("Got warehouses!")
    void getWarehouses() throws Exception {
        final Collection<Warehouse> warehouses = randomWarehouses();
        when(service.getWarehouses()).thenReturn(warehouses);
        final MvcResult result = mockMvc.perform(
            get(URI)
                .accept(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn();

        verify(service, times(1)).getWarehouses();
        verify(converter, times(warehouses.size())).convert(argThat(in(warehouses)));

        final Collection<WarehouseDTO> actualDTOs = mapper.readValue(result.getResponse().getContentAsString(),
            new TypeReference<>() {
            });
        assertThat(actualDTOs, is(notNullValue()));
        assertThat(actualDTOs, hasSize(warehouses.size()));

        final Collection<WarehouseDTO> expectedDTOs = warehouses.stream().map(converter::convert)
            .collect(toUnmodifiableSet());
        assertThat(actualDTOs, containsInAnyOrder(expectedDTOs.toArray()));
    }


    @Test
    @DisplayName("Got warehouse!")
    void getWarehouse() throws Exception {
        final Warehouse w = randomWarehouse();
        when(service.getByUuid(w.getUuid())).thenReturn(w);
        final MvcResult result = mockMvc.perform(
            get(URI + "/" + w.getUuid())
                .accept(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andReturn();
        verify(service, times(1)).getByUuid(eq(w.getUuid()));

        verify(converter, times(1)).convert(eq(w));
        final WarehouseDTO expectedWarehouse = converter.convert(w);
        final WarehouseDTO actualWarehouse = mapper.readValue(
            result.getResponse().getContentAsString(), WarehouseDTO.class);
        assertThat(actualWarehouse, is(equalTo(expectedWarehouse)));
    }

    @Test
    @DisplayName("Warehouse created!")
    void createWarehouse() throws Exception {
        final Warehouse expectedWarehouse = randomWarehouse();
        when(service.createWarehouse(
            eq(expectedWarehouse.getName()),
            eq(expectedWarehouse.getLatitude()),
            eq(expectedWarehouse.getLongitude()),
            eq(expectedWarehouse.getCapacity())
        ))
            .thenReturn(expectedWarehouse);

        WarehouseDTO w = converter.convert(expectedWarehouse);
        mockMvc.perform(
            post(URI)
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(mapper.writeValueAsString(w))
                .param(NAME_PARAM, expectedWarehouse.getName())
                .param(LATITUDE_PARAM, valueOf(expectedWarehouse.getLatitude()))
                .param(LONGITUDE_PARAM, valueOf(expectedWarehouse.getLongitude()))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON))
            .andExpect(header().string(LOCATION, valueOf(expectedWarehouse.getUuid())));

        verify(service, times(1)).createWarehouse(
            eq(expectedWarehouse.getName()),
            eq(expectedWarehouse.getLatitude()),
            eq(expectedWarehouse.getLongitude()),
            eq(expectedWarehouse.getCapacity())
        );
    }

    @Test
    @DisplayName("Warehouse updated!")
    void updateWarehouse() throws Exception {
        final Warehouse w = randomWarehouse();
        final WarehouseDTO warehouseArg = converter.convert(w);
        when(service.getByUuid(eq(w.getUuid()))).thenReturn(w);

        mockMvc.perform(
            put(URI + "/" + w.getUuid())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(mapper.writeValueAsString(warehouseArg))
        )
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(APPLICATION_JSON));

        verify(service, times(1)).getByUuid(eq(w.getUuid()));
        verify(service, times(1)).saveWarehouse(eq(w));
    }

    @Test
    void updateWarehouseButCreate() throws Exception {
        final Warehouse w = randomWarehouse();
        final WarehouseDTO warehouseArg = converter.convert(w);

        when(service.getByUuid(eq(w.getUuid()))).thenReturn(null);
        when(service.createWarehouse(eq(w.getName()), eq(w.getLatitude()), eq(w.getLongitude()), eq(w.getCapacity())))
            .thenReturn(w);

        mockMvc.perform(
            put(URI + "/" + w.getUuid())
                .contentType(APPLICATION_JSON)
                .accept(APPLICATION_JSON)
                .content(mapper.writeValueAsString(warehouseArg))
        )
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().contentType(APPLICATION_JSON));

        verify(service, times(1)).getByUuid(eq(w.getUuid()));
        verify(service, times(1)).createWarehouse(
            eq(w.getName()),
            eq(w.getLatitude()),
            eq(w.getLongitude()),
            eq(w.getCapacity())
        );
    }

    @Test
    @DisplayName("Warehouse deleted!")
    void deleteWarehouse() throws Exception {
        final Warehouse w = randomWarehouse();
        final UUID uuid = w.getUuid();
        when(service.getByUuid(eq(uuid))).thenReturn(w);
        mockMvc.perform(
            delete(URI + "/" + uuid)
                .accept(APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isNoContent());
        verify(service, times(1)).getByUuid(eq(uuid));
        verify(service, times(1)).removeWarehouse(eq(w.getId()));
    }
}
