package com.huishu.repository;

import com.huishu.entity.QuitDataSmt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangjianchun
 */
public interface QuitDataSmtRepository extends JpaRepository<QuitDataSmt, String> {
    /**
     * 分页查询
     *
     * @param spec
     * @param pageable
     * @return
     */
    Page<QuitDataSmt> findAll(Specification<QuitDataSmt> spec, Pageable pageable);
}
