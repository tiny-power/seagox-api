package com.seagox.lowcode.strategy.rule;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class RuleHandlerFactory {

	private static Map<String, RuleHandler> EVENT_SERVICE_MAP = new ConcurrentHashMap<>(255);

	public static RuleHandler getHandler(String type) {
		return EVENT_SERVICE_MAP.get(type);
	}

	public static void register(String type, RuleHandler eventService) {
		EVENT_SERVICE_MAP.put(type, eventService);
	}
}
