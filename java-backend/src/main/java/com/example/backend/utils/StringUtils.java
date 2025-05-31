package com.example.backend.utils;

public class StringUtils {

    /**
     * 한 자리 숫자(int)를 입력받아 "01", "09"처럼 앞에 0을 붙이거나 두 자리면 그대로 반환
     *
     * @param num 0~99 범위의 정수
     * @return 한 자리면 앞에 0을 붙인 문자열, 두 자리면 그대로 문자열 반환
     */
    public static String formatTwoDigits(int num) {
        if (num < 0 || num > 99) {
            throw new IllegalArgumentException("숫자는 0 이상 99 이하만 허용됩니다.");
        }
        return num < 10 ? "00" + num : String.valueOf(num);
    }



    /**
     * 한 자리 또는 두 자리 숫자(int)를 입력받아 세 자리 문자열로 반환
     * 예: 7 → "007", 45 → "045", 123 → "123"
     *
     * @param num 0~999 범위의 정수
     * @return 세 자리 문자열로 포맷된 숫자
     */
    public static String formatThreeDigits(int num) {
        if (num < 0 || num > 999) {
            throw new IllegalArgumentException("숫자는 0 이상 999 이하만 허용됩니다.");
        }
        if (num < 10) return "00" + num;
        if (num < 100) return "0" + num;
        return String.valueOf(num);
    }


    /**
     * 문자열을 지정한 길이로 왼쪽에서 padChar를 채워 맞춰주는 함수
     *
     * @param input    원래 문자열 (null 가능)
     * @param length   원하는 최종 문자열 길이
     * @param padChar  채울 문자 (예: '0', '*', ' ')
     * @return 길이가 부족할 경우 왼쪽에서 padChar를 채운 문자열
     *         이미 길거나 길이 초과인 경우 그대로 반환
     */
    public static String lpad(String input, int length, char padChar) {
        if (input == null) input = "";
        if (input.length() >= length) return input;

        StringBuilder sb = new StringBuilder();
        for (int i = input.length(); i < length; i++) {
            sb.append(padChar);
        }
        sb.append(input);
        return sb.toString();
    }

    /**
     * 문자열을 지정한 길이로 오른쪽에서 padChar를 채워 맞춰주는 함수
     *
     * @param input    원래 문자열 (null 가능)
     * @param length   원하는 최종 문자열 길이
     * @param padChar  채울 문자 (예: '0', '*', ' ')
     * @return 길이가 부족할 경우 오른쪽에서 padChar를 채운 문자열
     *         이미 길거나 길이 초과인 경우 그대로 반환
     */
    public static String rpad(String input, int length, char padChar) {
        if (input == null) input = "";
        if (input.length() >= length) return input;

        StringBuilder sb = new StringBuilder(input);
        for (int i = input.length(); i < length; i++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    /**
     * null 또는 빈 문자열("")일 경우 대체 문자열을 반환하는 함수
     *
     * @param target      원본 문자열 (null 또는 공백 가능)
     * @param alternative target이 null이거나 빈 문자열일 때 대신 사용할 문자열
     * @return target이 비어있지 않으면 target, 비어있으면 alternative
     */
    public static String nvl(String target, String alternative) {
        if (target == null || target.trim().isEmpty()) {
            return alternative;
        }
        return target;
    }

    /**
     * 문자열이 null이거나 비어 있거나 공백만 있는지 확인
     *
     * @param str 검사할 문자열
     * @return true이면 null이거나 빈 문자열 또는 공백 문자열
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 문자열이 비어있지 않은지 확인
     *
     * @param str 검사할 문자열
     * @return true이면 내용이 있는 문자열
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }



}
