package ru.yandex.practicum.filmorate.util;

import java.util.regex.Pattern;

public class Validator {

    public static boolean isEmail(String text) {
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        return text != null && Pattern.compile(emailRegex)
                .matcher(text)
                .matches();
    }

}
