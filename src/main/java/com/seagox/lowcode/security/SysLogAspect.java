package com.seagox.lowcode.security;

import com.alibaba.fastjson.JSON;
import com.seagox.lowcode.annotation.LogPoint;
import com.seagox.lowcode.common.ResultCode;
import com.seagox.lowcode.common.ResultData;
import com.seagox.lowcode.entity.SysLog;
import com.seagox.lowcode.mapper.LogMapper;
import com.seagox.lowcode.util.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Spring AOP实现日志管理
 */
@Aspect
@Component
public class SysLogAspect {

	private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<Date>("ThreadLocal beginTime");

	@Autowired
	private LogMapper sysLogMapper;

	/**
	 * 设置操作日志切入点 记录操作日志 在注解的位置切入代码
	 */
	@Pointcut("@annotation(com.seagox.lowcode.annotation.LogPoint)")
	public void operLogPoinCut() {

	}

	@Before("operLogPoinCut()")
	public void doBefore(JoinPoint joinPoint) throws InterruptedException {
		// 线程绑定变量（该数据只有当前请求的线程可见）
		Date beginTime = new Date();
		beginTimeThreadLocal.set(beginTime);
	}

	/**
	 * 正常返回处理
	 *
	 * @param joinPoint 连接点
	 * @param result    返回结果
	 */
	@AfterReturning(value = "operLogPoinCut()", returning = "result")
	public void afterReturn(JoinPoint joinPoint, Object result) {
		ResultData resultData = (ResultData) result;
		if (resultData.getCode() == ResultCode.SUCCESS.getCode()) {
			saveSysLog(joinPoint, 1, JSON.toJSONString(result));
		} else {
			saveSysLog(joinPoint, 2, JSON.toJSONString(result));
		}
	}

	/**
	 * 抛出异常的处理
	 *
	 * @param joinPoint 连接点
	 * @param ex        异常对象
	 */
	@AfterThrowing(value = "operLogPoinCut()", throwing = "ex")
	public void afterThrowing(JoinPoint joinPoint, Throwable ex) {
		saveSysLog(joinPoint, 2, stackTraceToString(ex.getClass().getName(), ex.getMessage(), ex.getStackTrace()));
	}

	public void saveSysLog(JoinPoint joinPoint, int status, String result) {
		SysLog sysLog = new SysLog();
		// 获取RequestAttributes
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		// 从获取RequestAttributes中获取HttpServletRequest的信息
		HttpServletRequest request = (HttpServletRequest) requestAttributes
				.resolveReference(RequestAttributes.REFERENCE_REQUEST);
		String authToken = request.getHeader("Authorization");
		if (!StringUtils.isEmpty(authToken) && authToken.startsWith("Bearer ")) {
			Map<String, Object> payload = JwtTokenUtils.unSign(authToken);
			if ((boolean) payload.get("verify")) {
				String companyId = payload.get("companyId").toString();
				String userId = payload.get("userId").toString();
				sysLog.setCompanyId(Long.valueOf(companyId));
				sysLog.setUserId(Long.valueOf(userId));
			} else {
				sysLog.setCompanyId(0L);
				sysLog.setUserId(0L);
			}
		} else {
			sysLog.setCompanyId(0L);
			sysLog.setUserId(0L);
		}
		sysLog.setUri(request.getRequestURI());

		// 从切面织入点处通过反射机制获取织入点处的方法
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		// 获取切入点所在的方法
		Method method = signature.getMethod();
		LogPoint logPoint = method.getAnnotation(LogPoint.class);
		if (logPoint != null) {
			sysLog.setName(logPoint.value());

		}
		// 获取浏览器信息
		String ua = request.getHeader("User-Agent");
		// 获取请求的类名
		String className = joinPoint.getTarget().getClass().getName();
		// 获取请求的方法名
		String methodName = method.getName();
		sysLog.setMethod(className + "." + methodName);
		// 请求的参数
		Map<String, String> rtnMap = converMap(request.getParameterMap());
		// 将参数所在的数组转换成json
		String params = JSON.toJSONString(rtnMap);
		sysLog.setParams(params);
		sysLog.setIp(IpUtils.getIpAddr(request));
		long beginTime = beginTimeThreadLocal.get().getTime();
		long endTime = System.currentTimeMillis();
		sysLog.setCostTime((int) (endTime - beginTime));
		sysLog.setStatus(status);
		sysLog.setResult(result);
		sysLog.setUa(ua);
		sysLogMapper.insert(sysLog);
	}

	/**
	 * 转换request 请求参数
	 *
	 * @param paramMap request获取的参数数组
	 */
	public Map<String, String> converMap(Map<String, String[]> paramMap) {
		Map<String, String> rtnMap = new HashMap<String, String>();
		for (String key : paramMap.keySet()) {
			rtnMap.put(key, paramMap.get(key)[0]);
		}
		return rtnMap;
	}

	/**
	 * 转换异常信息为字符串
	 *
	 * @param exceptionName    异常名称
	 * @param exceptionMessage 异常信息
	 * @param elements         堆栈信息
	 */
	public String stackTraceToString(String exceptionName, String exceptionMessage, StackTraceElement[] elements) {
		StringBuffer strbuff = new StringBuffer();
		for (StackTraceElement stet : elements) {
			strbuff.append(stet + "\n");
		}
		String message = exceptionName + ":" + exceptionMessage + "\n\t" + strbuff.toString();
		ResultData resultData = new ResultData();
		resultData.setCode(ResultCode.INTERNAL_SERVER_ERROR.getCode());
		resultData.setMessage(message);
		return JSON.toJSONString(resultData);
	}

}
