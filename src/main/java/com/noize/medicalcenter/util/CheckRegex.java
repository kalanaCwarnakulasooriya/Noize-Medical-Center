package com.noize.medicalcenter.util;

public class CheckRegex {

    static String getRegex(String type) {
        return switch (type) {
            case "email" -> "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
            case "password" -> "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
            case "name" -> "^[A-Za-z ]{2,}$";
            case "address" -> "^[a-zA-Z0-9\\s,'-]{5,}$";
            case "username" -> "^[a-zA-Z0-9_]{3,16}$";
            case "otp" -> "^\\d{6}$";
            case "contactNumber" -> "^(\\d{10})$";
            case "qty" -> "^[0-9]+$";
            case "packSize" -> "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9 /]+$";
            case "desc" -> "^[a-zA-Z ]+$";
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
