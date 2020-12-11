package com.example.unittester.stringreverser;

public class StringReverser {

    public String reverse(String string) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = string.length() - 1; i >= 0; i--) {
            stringBuilder.append(string.charAt(i));
        }

        return stringBuilder.toString();
    }
}
