package com.rsjava.utils;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

public abstract class CriteriaNoCountDao <T, ID> {

    @PersistenceContext
    protected EntityManager em;

    public <T, ID extends Serializable> Page<T> findAll(Specification<T> spec, Pageable pageable, Class<T> clazz){
        SimpleJpaNoCountRepository<T, ID> noCountDao = new SimpleJpaNoCountRepository<T, ID>(clazz, em);
        return noCountDao.findAll(spec, pageable);
    }

    /**
     * Custom repository type that disable count query.
     */
    public static class SimpleJpaNoCountRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

        public SimpleJpaNoCountRepository(Class<T> domainClass, EntityManager em) {
            super(domainClass, em);
        }

        /**
         * Override {@link SimpleJpaRepository#readPage(TypedQuery, Class, Pageable, Specification)}
         */
        protected <S extends T> Page<S> readPage(TypedQuery<S> query, final Class<S> domainClass, Pageable pageable,
                                                 @Nullable Specification<S> spec) {

            if (pageable.isPaged()) {
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
            }

            List<S> content = query.getResultList();

            return new PageImpl<S>(content, pageable, content.size());
        }
    }
}

