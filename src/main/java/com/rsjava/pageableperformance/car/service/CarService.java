package com.rsjava.pageableperformance.car.service;


import com.rsjava.pageableperformance.car.CarNoCountRepository;
import com.rsjava.pageableperformance.car.CarRepository;
import com.rsjava.pageableperformance.car.exception.CarNotFoundException;
import com.rsjava.pageableperformance.car.mapper.CarMapper;
import com.rsjava.pageableperformance.car.model.CarEntity;
import com.rsjava.pageableperformance.car.request.CarRequest;
import com.rsjava.pageableperformance.car.response.CarResponse;
import com.rsjava.utils.PredicatesBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rsjava.pageableperformance.car.mapper.CarMapper.mapToEntity;
import static com.rsjava.pageableperformance.car.mapper.CarMapper.mapToResponse;


@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {

    private final CarRepository carRepository;
    private final CarNoCountRepository carNoCountRepository;

    @Transactional
    public List<CarResponse> getCars(String uuid,
                                     String brand,
                                     String model,
                                     Integer yearFrom,
                                     Integer yearTo,
                                     BigDecimal priceFrom,
                                     BigDecimal priceTo,
                                     int pageNumber,
                                     int pageSize
    ) {
        Specification<CarEntity> specification = getCarEntityQuery(uuid, brand, model, yearFrom, yearTo, priceFrom, priceTo);

        return carRepository.findAll(specification, PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(CarMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CarResponse> getCarsWithoutCount(String uuid,
                                                 String brand,
                                                 String model,
                                                 Integer yearFrom,
                                                 Integer yearTo,
                                                 BigDecimal priceFrom,
                                                 BigDecimal priceTo,
                                                 int pageNumber,
                                                 int pageSize
    ) {
        Specification<CarEntity> specification = getCarEntityQuery(uuid, brand, model, yearFrom, yearTo, priceFrom, priceTo);

        return carNoCountRepository.findAll(specification, PageRequest.of(pageNumber, pageSize))
                .stream()
                .map(CarMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public CarResponse getCar(String uuid) {
        CarEntity carEntity = carRepository.findByUuid(uuid).orElseThrow(() -> new CarNotFoundException(uuid));
        return mapToResponse(carEntity);
    }

    @Transactional
    public String saveCar(String personUuid, CarRequest request) {
        log.debug("Save car request with params: {}", request);

        CarEntity carEntity = mapToEntity(request);

        return carRepository.save(carEntity).getUuid();
    }

    @PostConstruct
    public void saveCars() {

        List<CarEntity> cars = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            cars.add(
                    new CarEntity(
                            "Ford " + i,
                            "Mustang" + i,
                            i,
                            new BigDecimal("80000")
                    ));
        }
        carRepository.saveAll(cars);
    }

    @Transactional
    public CarResponse updateCar(String uuid, CarRequest request) {
        CarEntity carEntity = carRepository.findByUuid(uuid).orElseThrow(() -> new CarNotFoundException(uuid));

        carEntity.setBrand(request.getBrand());
        carEntity.setModel(request.getModel());

        return mapToResponse(carEntity);
    }

    @Transactional
    public void deleteCar(String uuid) {
        carRepository.deleteByUuid(uuid);
    }

    private Specification<CarEntity> getCarEntityQuery(String uuid,
                                                       String brand,
                                                       String model,
                                                       Integer yearFrom,
                                                       Integer yearTo,
                                                       BigDecimal priceFrom,
                                                       BigDecimal priceTo) {
        return (root, query, criteriaBuilder) ->
                new PredicatesBuilder<>(root, criteriaBuilder)
                        .caseInsensitiveLike(CarEntity.Fields.uuid, uuid)
                        .caseInsensitiveLike(CarEntity.Fields.brand, brand)
                        .caseInsensitiveLike(CarEntity.Fields.model, model)
                        .greaterThanOrEqualTo(root.get(CarEntity.Fields.year), yearFrom)
                        .lessThanOrEqualTo(root.get(CarEntity.Fields.year), yearTo)
                        .greaterThanOrEqualTo(root.get(CarEntity.Fields.price), priceFrom)
                        .lessThanOrEqualTo(root.get(CarEntity.Fields.price), priceTo)
                        .build();
    }
}
