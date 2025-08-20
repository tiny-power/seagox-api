package com.seagox.lowcode.strategy.node;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class NodeHandlerFactory {

	private static Map<String, NodeHandler> EVENT_SERVICE_MAP = new ConcurrentHashMap<>(255);

	public static NodeHandler getHandler(String type) {
		return EVENT_SERVICE_MAP.get(type);
	}

	public static void register(String type, NodeHandler nodeHandler) {
		EVENT_SERVICE_MAP.put(type, nodeHandler);
	}
}
