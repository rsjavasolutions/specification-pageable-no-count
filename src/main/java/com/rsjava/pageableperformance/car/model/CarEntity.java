package com.rsjava.pageableperformance.car.model;

import lombok.*;
import lombok.experimental.FieldNameConstants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "car")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@FieldNameConstants
public class CarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    private String uuid;
    private String brand;
    private String model;
    private Integer year;
    private BigDecimal price;
    @Setter(AccessLevel.NONE)
    private LocalDateTime creationDateTime;

    public CarEntity(String brand,
                     String model,
                     int year,
                     BigDecimal price) {
        this.uuid = UUID.randomUUID().toString();
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.price = price;
        this.creationDateTime = LocalDateTime.now();
    }
}

