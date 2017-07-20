package com.lhh.base.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.lhh.base.entity.EntityBase;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface RepositoryBase<T extends EntityBase, ID extends Serializable> extends CrudRepository<T, ID> {
    Page<T> findAll(Pageable pageable);
    Page<T> findAll(Specification<T> var1, Pageable var2);
    List<T> findAll();
}
