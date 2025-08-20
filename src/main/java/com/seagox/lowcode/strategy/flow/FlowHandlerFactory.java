package com.seagox.lowcode.strategy.flow;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

@Component
public class FlowHandlerFactory   {

    private static  Map<String, FlowHandler> EVENT_SERVICE_MAP = new ConcurrentHashMap<>(255);

    public static FlowHandler getHandler(String type) {
        return EVENT_SERVICE_MAP.get(type);
    }

    public static void register(String type, FlowHandler eventService){
        EVENT_SERVICE_MAP.put(type,eventService);
    }
    
}
