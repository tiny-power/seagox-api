package com.seagox.lowcode.strategy.rule;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;

import com.alibaba.fastjson.JSONObject;
import com.seagox.lowcode.util.VerifyHandlerResult;

public interface RuleHandler extends InitializingBean {

	public void insertBefore(Map<String, Object> params);

    public void insertAfter(Map<String, Object> params);

    public void updateBefore(Map<String, Object> params);

    public void updateAfter(Map<String, Object> params);
    
    public void deleteBefore(Map<String, Object> params);

    public void deleteAfter(Map<String, Object> params);
    
    public VerifyHandlerResult importVerify(HttpServletRequest request, JSONObject row);
    
    public void importHandle(HttpServletRequest request, List<Map<String, Object>> result, Map<String, Object> params);
    
    public Map<String, Object> detailData(Map<String, Object> params);
    
    public void flowFinish(Map<String, Object> params);
    
}
