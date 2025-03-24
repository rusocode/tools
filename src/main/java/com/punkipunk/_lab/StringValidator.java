package com.punkipunk._lab;

public class StringValidator {

    public static void main(String[] args) {
        if (isValidAsciiString("juan")) System.out.println("Is valid!");
        else System.out.println("Not valid!");
    }

    /**
     * Checks if the string contains only lowercase ASCII letters.
     *
     * @param str the text string to verify.
     * @return true if the string is valid, false otherwise.
     */
    public static boolean isValidAsciiString(String str) {
        if (str == null) return false;
        for (char c : str.toCharArray())
            // If the character is NOT between 'a' and 'z'
            if (!(c >= 'a' && c <= 'z'))
                return false;
        return true;
    }

}
