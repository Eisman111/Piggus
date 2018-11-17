package com.paoloamosso.piggus.util;

public class Periodicity {

    private static String JANUARY = "January";
    private static String FEBRUARY = "February";
    private static String MARCH = "March";
    private static String APRIL = "April";
    private static String MAY = "May";
    private static String JUNE = "June";
    private static String JULY = "July";
    private static String AUGUST = "August";
    private static String SEPTEMBER = "September";
    private static String OCTOBER = "October";
    private static String NOVEMBER = "November";
    private static String DECEMBER = "December";

    public static String convertMonthAndYear(String intMonth, String year) {
        String month;
        switch (intMonth) {
            case "01":
                month = JANUARY;
                break;
            case "02":
                month = FEBRUARY;
                break;
            case "03":
                month = MARCH;
                break;
            case "04":
                month = APRIL;
                break;
            case "05":
                month = MAY;
                break;
            case "06":
                month = JUNE;
                break;
            case "07":
                month = JULY;
                break;
            case "08":
                month = AUGUST;
                break;
            case "09":
                month = SEPTEMBER;
                break;
            case "10":
                month = OCTOBER;
                break;
            case "11":
                month = NOVEMBER;
                break;
            case "12":
                month = DECEMBER;
                break;
            default:
                throw new IllegalArgumentException("This is not an existing month, what is happening? period: " + intMonth);
        }
        String monthAndYear = month + " " + year;
        return monthAndYear;
    }
}
