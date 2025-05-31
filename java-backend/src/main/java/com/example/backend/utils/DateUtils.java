package com.example.backend.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    /**
     * yymmdd 형식의 날짜를 받아 해당 월의 n번째 특정 요일인지 반환
     *
     * @param yymmdd ex) "250508" (2025년 5월 8일)
     * @param targetDay 요일 (ex: DayOfWeek.THURSDAY)
     * @return "YYMM0n" 형식 문자열 반환 (ex: "250502"), 요일이 일치하지 않으면 null
     */
    public static String getMonthlyWeekdayOrder(String yymmdd, DayOfWeek targetDay) {
        try {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyMMdd");
            LocalDate date = LocalDate.parse(yymmdd, inputFormatter);

            if (!date.getDayOfWeek().equals(targetDay)) {
                return null;
            }

            int count = 0;
            LocalDate current = date.withDayOfMonth(1);
            while (!current.isAfter(date)) {
                if (current.getDayOfWeek().equals(targetDay)) {
                    count++;
                }
                current = current.plusDays(1);
            }

            String yy = String.format("%02d", date.getYear() % 100);
            String mm = String.format("%02d", date.getMonthValue());
            return yy + mm + "0" + count;
        } catch (Exception e) {
            return null;
        }
    }
}
