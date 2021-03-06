package com.huishu.service.impl;

import com.huishu.entity.ZongheBak;
import com.huishu.repository.ZongheBakRepository;
import com.huishu.service.ZongheBakService;
import org.hibernate.jpa.criteria.OrderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wangjianchun
 */
@Service
public class ZongheBakServiceImpl implements ZongheBakService {

    @Autowired
    private ZongheBakRepository zongheBakRepository;

    @Override
    public List<ZongheBak> findOneHundred(ZongheBak entity, Pageable pageable) {
        if (pageable == null) {
            pageable = new PageRequest(0, 100);
        }
        Page<ZongheBak> page = zongheBakRepository.findAll((root, query, cb) -> {
            Path<Long> id = root.get("id");
            Path<String> biaoShi = root.get("biaoShi");
            if (entity != null) {
                List<Predicate> queryList = new ArrayList<Predicate>();
                if (entity.getId() != null) {
                    queryList.add(cb.greaterThan(id, entity.getId()));
                }
                if (entity.getBiaoShi() != null) {
                    queryList.add(cb.equal(biaoShi, entity.getBiaoShi()));
                }
                Predicate[] querys = new Predicate[queryList.size()];
                if (queryList != null && queryList.size() > 0) {
                    for (int i = 0, len = queryList.size(); i < len; i++) {
                        querys[i] = queryList.get(i);
                    }
                }
                query.where(querys).orderBy(new OrderImpl(id, true));
            }
            return null;
        }, pageable);
        if (page == null || page.getContent() == null || page.getContent().size() == 0) {
            return new ArrayList<ZongheBak>();
        } else {
            return page.getContent();
        }
    }

    @Override
    public void save(List<ZongheBak> list) {
        zongheBakRepository.save(list);
    }

    @Override
    public long findExist(ZongheBak entity) {
        return zongheBakRepository.countByFldUrlAddrAndFldtitle(entity.getFldUrlAddr(), entity.getFldtitle());
    }

    @Override
    public Page<ZongheBak> findByPage(ZongheBak entity, Pageable pageable) {
        if (pageable == null) {
            pageable = new PageRequest(0, 100);
        }
        Page<ZongheBak> page = zongheBakRepository.findAll((root, query, cb) -> {
            Path<Long> id = root.get("id");
            Path<String> biaoShi = root.get("biaoShi");
            if (entity != null) {
                List<Predicate> queryList = new ArrayList<Predicate>();
                if (entity.getId() != null) {
                    queryList.add(cb.greaterThan(id, entity.getId()));
                }
                if (entity.getBiaoShi() != null) {
                    queryList.add(cb.equal(biaoShi, entity.getBiaoShi()));
                }
                Predicate[] querys = new Predicate[queryList.size()];
                if (queryList != null && queryList.size() > 0) {
                    for (int i = 0, len = queryList.size(); i < len; i++) {
                        querys[i] = queryList.get(i);
                    }
                }
                query.where(querys).orderBy(new OrderImpl(id, true));
            }
            return null;
        }, pageable);

        return page;
    }
}
