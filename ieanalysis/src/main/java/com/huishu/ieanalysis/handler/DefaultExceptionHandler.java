package com.huishu.ieanalysis.handler;

import com.huishu.base.bean.BaseAjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 
 * <p>Title: OpinionExceptionHandler</p>
 * <p>Description: 统一的异常处理器</p>
 * @author wjc
 * @date 2017年6月20日
 */
@ControllerAdvice
public class DefaultExceptionHandler {
	
    private final static Logger logger = LoggerFactory.getLogger(DefaultExceptionHandler.class);
    
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public BaseAjaxResult handle(Exception e){
    	logger.error("异常信息：{}", e);

        BaseAjaxResult result = new BaseAjaxResult();
        result.setStatus(500);
        result.setMessage("系统错误，请联系管理员解决");

        return result;
    }
}