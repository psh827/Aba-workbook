package com.example.backend.utils;

import java.util.Map;

public class ValidationUtils {
    public static boolean isAllRatioFieldsEmpty(Map<String, Object> block, String[] keysToCheck) {
        if (block == null) return true; // null이면 true

        for (String key : keysToCheck) {
            Object value = block.get(key);
            if (value != null && !value.toString().trim().isEmpty()) {
                return false;  // 하나라도 값이 있으면 false
            }
        }

        return true;  // 전부 null 또는 빈 문자열이면 true
    }
}
