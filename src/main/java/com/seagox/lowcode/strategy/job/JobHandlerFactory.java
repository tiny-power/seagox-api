package com.seagox.lowcode.strategy.job;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class JobHandlerFactory {

	private static Map<String, JobHandler> EVENT_SERVICE_MAP = new ConcurrentHashMap<>(255);

	public static JobHandler getHandler(String type) {
		return EVENT_SERVICE_MAP.get(type);
	}

	public static void register(String type, JobHandler jobHandler) {
		EVENT_SERVICE_MAP.put(type, jobHandler);
	}
}
