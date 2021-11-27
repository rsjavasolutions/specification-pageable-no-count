package com.rsjava.pageableperformance.car;


import com.rsjava.pageableperformance.car.request.CarRequest;
import com.rsjava.pageableperformance.car.response.CarResponse;
import com.rsjava.pageableperformance.car.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("cars")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;
    private static final String PAGE_NUMBER = "0";
    private static final String PAGE_SIZE = "10";

    @GetMapping("{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public CarResponse getCar(@PathVariable String uuid) {
        return carService.getCar(uuid);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarResponse> getCars(@RequestParam(value = "uuid", required = false) String uuid,
                                     @RequestParam(value = "brand", required = false) String brand,
                                     @RequestParam(value = "model", required = false) String model,
                                     @RequestParam(value = "yearFrom", required = false) Integer yearFrom,
                                     @RequestParam(value = "yearTo", required = false) Integer yearTo,
                                     @RequestParam(value = "priceFrom", required = false) BigDecimal priceFrom,
                                     @RequestParam(value = "priceTo", required = false) BigDecimal priceTo,
                                     @RequestParam(value = "pageNumber", required = false, defaultValue = PAGE_NUMBER) int pageNumber,
                                     @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) int pageSize
    ) {
        return carService.getCars(uuid, brand, model, yearFrom, yearTo, priceFrom, priceTo, pageNumber, pageSize);
    }

    @GetMapping("no-count")
    @ResponseStatus(HttpStatus.OK)
    public List<CarResponse> getCarsNoCount(@RequestParam(value = "uuid", required = false) String uuid,
                                            @RequestParam(value = "brand", required = false) String brand,
                                            @RequestParam(value = "model", required = false) String model,
                                            @RequestParam(value = "yearFrom", required = false) Integer yearFrom,
                                            @RequestParam(value = "yearTo", required = false) Integer yearTo,
                                            @RequestParam(value = "priceFrom", required = false) BigDecimal priceFrom,
                                            @RequestParam(value = "priceTo", required = false) BigDecimal priceTo,
                                            @RequestParam(value = "pageNumber", required = false, defaultValue = PAGE_NUMBER) int pageNumber,
                                            @RequestParam(value = "pageSize", required = false, defaultValue = PAGE_SIZE) int pageSize
    ) {
        return carService.getCarsWithoutCount(uuid, brand, model, yearFrom, yearTo, priceFrom, priceTo, pageNumber, pageSize);
    }

    @PostMapping("people/{personUuid}")
    @ResponseStatus(HttpStatus.CREATED)
    public String saveCar(@PathVariable String personUuid,
                          @RequestBody @Valid CarRequest request) {
        return carService.saveCar(personUuid, request);
    }

    @PutMapping("{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public CarResponse updateCar(@PathVariable String uuid,
                                 @RequestBody CarRequest request) {
        return carService.updateCar(uuid, request);
    }

    @DeleteMapping("{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable String uuid) {
        carService.deleteCar(uuid);
    }
}
