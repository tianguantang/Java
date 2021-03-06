package com.huishu.repository;

import com.huishu.entity.Recruitment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wangjianchun
 */
public interface RecruitmentRepository extends JpaRepository<Recruitment, String> {
    /**
     * 分页查询
     *
     * @param spec
     * @param pageable
     * @return
     */
    Page<Recruitment> findAll(Specification<Recruitment> spec, Pageable pageable);
}
