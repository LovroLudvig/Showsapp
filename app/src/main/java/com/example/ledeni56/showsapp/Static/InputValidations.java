package com.example.ledeni56.showsapp.Static;


import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidations {
    private static Pattern pattern = Pattern.compile(Patterns.EMAIL_ADDRESS.toString());
    private static Matcher matcher;

    public static boolean validateEmail(String email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean validatePassword(String password) {
        return password.length() >= 5;
    }

    public static boolean validatePasswordsMatch(String password, String otherPassword) {
        return password.equals(otherPassword);
    }
}
