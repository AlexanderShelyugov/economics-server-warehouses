package com.alexander.economics.service.warehouse.converter;

import com.alexander.economics.service.warehouse.dto.WarehouseDTO;
import com.alexander.economics.service.warehouse.model.Warehouse;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.modelmapper.Provider;
import org.modelmapper.TypeMap;

/**
 * Implementation of warehouse's converter
 */
@SuppressWarnings("unused")
class ModelMapperWarehouseConverter implements WarehouseConverter {
    private final ModelMapper mapper;

    ModelMapperWarehouseConverter() {
        mapper = new ModelMapper();
        Provider<WarehouseDTO> provider = request -> {
            Warehouse warehouse = (Warehouse) request.getSource();
            return new WarehouseDTO(warehouse.getUuid().toString(), warehouse.getName(),
                warehouse.getLatitude(), warehouse.getLongitude());
        };
        TypeMap<Warehouse, WarehouseDTO> entityToDTOMap = mapper.createTypeMap(Warehouse.class, WarehouseDTO.class);
        entityToDTOMap.setProvider(provider);
    }

    @Override
    public WarehouseDTO convert(@NonNull Warehouse warehouse) {
        return mapper.map(warehouse, WarehouseDTO.class);
    }
}
