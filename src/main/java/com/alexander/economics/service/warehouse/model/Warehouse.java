package com.alexander.economics.service.warehouse.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PACKAGE;

/**
 * Warehouse entity
 */
@Entity
@Table(name = "WAREHOUSES")
@AllArgsConstructor
@NoArgsConstructor(access = PACKAGE)
@Getter
@EqualsAndHashCode
@ToString
public class Warehouse implements Serializable {
    private static final long serialVersionUID = -4592163537684517241L;

    /**
     * Identifier
     */
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "ID")
    private Long id;

    /**
     * UUID
     */
    @Column(name = "UUID", columnDefinition = "BINARY(16)")
    private UUID uuid;

    /**
     * Name of warehouse
     */
    @Column(name = "NAME")
    private String name;

    /**
     * Latitude
     */
    @Column(name = "LATITUDE")
    private Double latitude;

    /**
     * Longitude
     */
    @Column(name = "LONGITUDE")
    private Double longitude;
}
