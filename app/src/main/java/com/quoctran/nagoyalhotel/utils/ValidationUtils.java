package com.quoctran.nagoyalhotel.utils;

import android.text.Editable;
import android.widget.EditText;

import androidx.annotation.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean isPasswordValid(@Nullable Editable text) {
        return text != null && text.length() >= 6;
    }

    public static boolean isEmailValid(@Nullable Editable email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email != null && email.toString().matches(regex);
    }

    public static boolean isValidText(@Nullable Editable text) {
        return text != null && text.length() > 0;
    }

    public static boolean isCorrectVerifyPassword(@Nullable Editable password,
                                                  @Nullable Editable confirmPassword) {
        return password.toString().equals(confirmPassword.toString())
                && password.toString().length() > 0;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("\\d{11}");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
