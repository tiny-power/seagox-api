package com.seagox.lowcode.business.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Map查询结果时间格式化工具。
 */
public class MapDateFormatUtils {

    private static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private MapDateFormatUtils() {
    }

    public static void formatDateValues(List<Map<String, Object>> list) {
        if (list == null) {
            return;
        }
        list.forEach(MapDateFormatUtils::formatDateValues);
    }

    public static void formatDateValues(Map<String, Object> data) {
        if (data == null) {
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_TIME_PATTERN);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Date) {
                entry.setValue(formatter.format((Date) value));
            }
        }
    }
}
