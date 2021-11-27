package com.rsjava.pageableperformance.car;

import com.rsjava.pageableperformance.car.model.CarEntity;
import com.rsjava.utils.CriteriaNoCountDao;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public class CarNoCountRepository extends CriteriaNoCountDao<CarEntity, Long> {
    public Page<CarEntity> findAll(Specification<CarEntity> spec, Pageable pageable) {
        return super.findAll(spec, pageable, CarEntity.class);
    }
}
