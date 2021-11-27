package com.rsjava.pageableperformance.car;

import com.rsjava.pageableperformance.car.model.CarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<CarEntity, Long>, JpaSpecificationExecutor<CarEntity> {

    Optional<CarEntity> findByUuid(String uuid);

    void deleteByUuid(String uuid);

}
