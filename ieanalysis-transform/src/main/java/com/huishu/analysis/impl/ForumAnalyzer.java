package com.huishu.analysis.impl;

import com.huishu.analysis.vo.NewsVO;
import com.huishu.analysis.vo.ValidationVO;
import com.huishu.config.AnalysisConfig;
import com.huishu.constants.SysConst;
import com.huishu.entity.ForumLibBak;
import com.huishu.entity.KingBaseDgap;
import com.huishu.entity.SiteLib;
import com.huishu.service.ForumLibBakService;
import com.huishu.service.SiteLibService;
import com.huishu.vo.DgapData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 分析论坛数据
 *
 * @author wangjianchun
 * @create 2018/5/26
 */
@Component("forumAnalyzer")
public class ForumAnalyzer extends DefaultAnalyzer {

    private static final List<DgapData> STATIC_LIST = new ArrayList<DgapData>();

    @Autowired
    private ForumLibBakService forumLibBakService;
    @Autowired
    private SiteLibService siteLibService;

    @Override
    public String getName() {
        return "论坛";
    }

    @Override
    public boolean getMark() {
        return analysisConfig.isForumMark();
    }

    @Override
    public String getType() {
        return SysConst.FORUM;
    }

    /**
     * 分析数据
     * @param analysisConfig
     * @param indexMap
     * @param pageNumber
     */
    @Override
    protected void analysisData(AnalysisConfig analysisConfig, Map<String, String> indexMap, int pageNumber) {
        STATIC_LIST.clear();
        Map<String, String> newIndexMap = new HashMap<>(indexMap);

        ForumLibBak entity = new ForumLibBak();
        entity.setId(Long.valueOf(newIndexMap.get(getType())));
        Pageable pageable = new PageRequest(pageNumber, analysisConfig.getTransformNum());
        List<ForumLibBak> list = forumLibBakService.findOneHundred(entity, pageable);

        logger.info("{}分析,读取 {} 条", getName(), list.size());

        if (list.size() <= 0) {
            return;
        }

        String newId = list.get(list.size() - 1).getId() + "";
        String oldId = newIndexMap.get(getType());
        if (Long.parseLong(newId) > Long.parseLong(oldId)) {
            newIndexMap.put(getType(), newId);
        }

        List<DgapData> saveList = new ArrayList<DgapData>();
        List<ForumLibBak> readList = new ArrayList<ForumLibBak>();
        List<KingBaseDgap> historyList = new ArrayList<KingBaseDgap>();
        for (ForumLibBak item : list) {
            if (isNotExists(STATIC_LIST, item.getFldUrlAddr())) {
                // 分析
                SiteLib site = siteLibService.findByName(item.getWebname());
                SiteLib newSite = fillAreaInfoForSiteLib(item.getFldtitle(), item.getFldcontent(), site);
                if (newSite != null) {
                    ValidationVO validationVO = ValidationVO.create(item, newSite);
                    if (validate(validationVO)) {
                        NewsVO newsVO = NewsVO.create(item, newSite);
                        DgapData dgapData = fillDgapData(newsVO);
                        item.setBiaoShi(SysConst.ESDataStatus.EXISTS_IN_ES.getCode());
                        addKingBaseData(historyList, dgapData);
                        dgapData.setId(String.valueOf(item.getId()));
                        saveList.add(dgapData);
                        STATIC_LIST.add(dgapData);
                    }
                }
                if (SysConst.ESDataStatus.NOT_EXISTS_IN_ES.getCode().equals(item.getBiaoShi())) {
                    item.setBiaoShi(SysConst.ESDataStatus.EXCEPTION.getCode());
                }
                readList.add(item);
            }
        }

        if (saveList.size() > 0) {
            saveToFile(saveList, getType(), analysisConfig.getSourceMorePath());
            saveToKingBase(historyList);
        }
        if (readList.size() > 0) {
            forumLibBakService.save(readList);
        }

        logger.info("{}分析,入库 {} 条", getName(), saveList.size());
        logger.info("{}分析,分析 {} 条", getName(), readList.size());

        recordNum(newIndexMap);
    }

    /**
     * 分析数据
     * @param analysisConfig
     * @param indexMap
     */
    @Override
    protected void analysisData(AnalysisConfig analysisConfig, Map<String, String> indexMap) {
        STATIC_LIST.clear();
        Map<String, String> newIndexMap = new HashMap<>(indexMap);

        int pageNumber = 0;
        int totalPages = 10;
        ForumLibBak entity = new ForumLibBak();
        while (pageNumber <= totalPages){
            try {
                entity.setId(Long.valueOf(newIndexMap.get(getType())));
                Pageable pageable = new PageRequest(pageNumber, analysisConfig.getTransformNum());
                Page<ForumLibBak> page = forumLibBakService.findByPage(entity, pageable);
                totalPages = page.getTotalPages();

                List<ForumLibBak> list = page.getContent();
                if(list != null && list.size() > 0){
                    logger.info("{}分析,读取 {} 条", getName(), list.size());
                    logger.info("总页数：{}，每页记录数：{}，剩余 {} 条{}数据待分析", page.getTotalPages(),
                            analysisConfig.getTransformNum(), page.getTotalElements(), getName());
                    logger.info("第 {} 页{}数据分析开始", pageNumber, getName());

                    String newId = list.get(list.size() - 1).getId() + "";
                    String oldId = newIndexMap.get(getType());
                    if (Long.parseLong(newId) > Long.parseLong(oldId)) {
                        newIndexMap.put(getType(), newId);
                    }

                    List<DgapData> saveList = new ArrayList<DgapData>();
                    List<ForumLibBak> readList = new ArrayList<ForumLibBak>();
                    List<KingBaseDgap> historyList = new ArrayList<KingBaseDgap>();
                    for (ForumLibBak item : list) {
                        if (isNotExists(STATIC_LIST, item.getFldUrlAddr())) {
                            // 分析
                            SiteLib site = siteLibService.findByName(item.getWebname());
                            SiteLib newSite = fillAreaInfoForSiteLib(item.getFldtitle(), item.getFldcontent(), site);
                            if (newSite != null) {
                                ValidationVO validationVO = ValidationVO.create(item, newSite);
                                if (validate(validationVO)) {
                                    NewsVO newsVO = NewsVO.create(item, newSite);
                                    DgapData dgapData = fillDgapData(newsVO);
                                    item.setBiaoShi(SysConst.ESDataStatus.EXISTS_IN_ES.getCode());
                                    addKingBaseData(historyList, dgapData);
                                    dgapData.setId(String.valueOf(item.getId()));
                                    saveList.add(dgapData);
                                    STATIC_LIST.add(dgapData);
                                }
                            }
                            if (SysConst.ESDataStatus.NOT_EXISTS_IN_ES.getCode().equals(item.getBiaoShi())) {
                                item.setBiaoShi(SysConst.ESDataStatus.EXCEPTION.getCode());
                            }
                            readList.add(item);
                        }
                    }

                    if (saveList.size() > 0) {
                        saveToFile(saveList, getType(), analysisConfig.getSourceMorePath());
                        saveToKingBase(historyList);
                    }
                    if (readList.size() > 0) {
                        forumLibBakService.save(readList);
                    }

                    logger.info("{}分析,入库 {} 条", getName(), saveList.size());
                    logger.info("{}分析,分析 {} 条", getName(), readList.size());

                    recordNum(newIndexMap);

                    logger.info("第 {} 页{}数据分析结束", pageNumber, getName());

                    pageNumber++;
                }else{
                    pageNumber = 0;
                    totalPages = 10;
                    //如果没有数据需要分析，那么当前线程休眠5分钟
                    logger.info("没有{}数据需要分析，线程休眠 5 分钟", getName());
                    Thread.sleep(300000);
                }
            } catch (Exception e) {
                logger.error("第 {} 页的{}数据分析出错", pageNumber, getName(), e);
            }
        }
    }

    @Override
    protected boolean validate(ValidationVO validationVO) {
        String publishDate = validationVO.getFldrecddate();
        if (StringUtils.isEmpty(validationVO.getProvince())
                || StringUtils.isEmpty(validationVO.getIndustry())
                || StringUtils.isEmpty(validationVO.getFldcontent())
                || StringUtils.isEmpty(validationVO.getFldtitle())
                || StringUtils.isEmpty(publishDate)) {
            return false;
        }

        String tempTime = com.huishu.utils.StringUtils.transformTime(publishDate);
        int yearIndex = tempTime.indexOf("-");
        if (yearIndex <= 0) {
            return false;
        }
        int monthIndex = tempTime.indexOf("-", yearIndex + 1);
        if (monthIndex <= 0) {
            return false;
        }

        return true;
    }

    @Override
    protected DgapData fillDgapData(NewsVO newsVO) {
        DgapData result = super.fillDgapData(newsVO);
        result.setPublishType(SysConst.PublishType.FORUM.getCode());
        result.setSocialChannel(SysConst.SocialChannel.FORUM.getCode());

        return result;
    }

}
