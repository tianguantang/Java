package com.huishu.repository;

import com.huishu.entity.QuitDataTz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangjianchun
 */
public interface QuitDataTzRepository extends JpaRepository<QuitDataTz, String> {
    /**
     * 分页查询
     *
     * @param spec
     * @param pageable
     * @return
     */
    Page<QuitDataTz> findAll(Specification<QuitDataTz> spec, Pageable pageable);
}
