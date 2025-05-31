package com.example.backend.utils;

public class CommonUtils {

    /**
     * null이거나 비어 있거나 공백으로만 구성된 문자열인지 확인
     *
     * @param obj Object (String 또는 null 허용)
     * @return true: null 또는 공백만 있는 문자열 / false: 유의미한 문자열
     */
    public static boolean isNullOrBlank(Object obj) {
        if (obj == null) return true;
        if (obj instanceof String str) {
            return str.trim().isEmpty();
        }
        return false;
    }

    /**
     * null 또는 0, "", " " 등 무의미한 값인지 판단
     *
     * @param obj Object
     * @return true: 의미 없는 값 / false: 의미 있음
     */
    public static boolean isNullOrZero(Object obj) {
        if (obj == null) return true;
        if (obj instanceof String str) {
            return str.trim().isEmpty() || str.trim().equals("0");
        }
        if (obj instanceof Number num) {
            return num.doubleValue() == 0.0;
        }
        return false;
    }


    /**
     * 문자열 또는 숫자 형태의 Object를 안전하게 int로 변환
     * 소수점 포함 시 소수점 이하 버림
     */
    public static int safeToInt(Object obj) {
        if (obj == null) return 0;
        try {
            String str = obj.toString().trim();
            return str.contains(".") ? (int) Double.parseDouble(str) : Integer.parseInt(str);
        } catch (Exception e) {
            return 0;
        }
    }

    public static String safeToString(Object obj) {
        return obj != null ? obj.toString() : "";
    }


}
