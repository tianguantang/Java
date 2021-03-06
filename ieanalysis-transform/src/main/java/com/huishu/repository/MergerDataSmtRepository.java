package com.huishu.repository;

import com.huishu.entity.MergerDataSmt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangjianchun
 */
public interface MergerDataSmtRepository extends JpaRepository<MergerDataSmt, String> {
    /**
     * 分页查询
     *
     * @param spec
     * @param pageable
     * @return
     */
    Page<MergerDataSmt> findAll(Specification<MergerDataSmt> spec, Pageable pageable);
}
