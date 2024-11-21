package com.noize.medicalcenter.util;

public class CheckRegex {

    static String getRegex(String type) {
        return switch (type) {
            case "email" -> "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
            case "password" -> "^\\d+$";
            case "name" -> "^[A-Za-z ]{2,}$";
            case "address" -> "^[a-zA-Z0-9\\s,'-]{5,}$";
            case "username" -> "^[A-Za-z][A-Za-z0-9_]{4,19}$";
            case "otp" -> "^\\d{6}$";
            case "contactNumber" -> "^(\\d{10})$";
            case "qty" -> "^[0-9]+$";
            case "packSize" -> "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9 /]+$";
            case "desc" -> "^[a-zA-Z ]+$";
            case "dosage" -> "^[a-zA-Z0-9 ]+$";
            case "price" -> "^\\d+(\\.\\d{1,2})?$";
            case "docName" -> "^[A-Za-z. ]{2,}$";
            default -> "";
        };
    }

    public static boolean checkRegex(String type, String value) {
        String regex = getRegex(type);
        return value.matches(regex);
    }
}
