package com.seagox.lowcode.strategy.annotation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class AnnotationHandlerFactory {

	private static Map<String, AnnotationHandler> EVENT_SERVICE_MAP = new ConcurrentHashMap<>(255);

	public static AnnotationHandler getHandler(String type) {
		return EVENT_SERVICE_MAP.get(type);
	}

	public static void register(String type, AnnotationHandler annotationHandler) {
		EVENT_SERVICE_MAP.put(type, annotationHandler);
	}
}
