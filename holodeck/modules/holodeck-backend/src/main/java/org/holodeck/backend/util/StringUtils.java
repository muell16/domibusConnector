/*
 * 
 */
package org.holodeck.backend.util;

/**
 * The Class StringUtils.
 */
public class StringUtils {
    
    /**
     * Instantiates a new string utils.
     */
    public StringUtils() {
    }

    /**
     * Checks if is empty.
     *
     * @param str the str
     * @return true, if is empty
     */
    public static boolean isEmpty(String str) {
        return (str == null || str.length() == 0);
    }

    /**
     * Checks if is not empty.
     *
     * @param str the str
     * @return true, if is not empty
     */
    public static boolean isNotEmpty(String str) {
        return (str != null && str.length() > 0);
    }

    /**
     * Equals.
     *
     * @param str1 the str1
     * @param str2 the str2
     * @return true, if successful
     */
    public static boolean equals(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equals(str2));
    }

    /**
     * Equals ignore case.
     *
     * @param str1 the str1
     * @param str2 the str2
     * @return true, if successful
     */
    public static boolean equalsIgnoreCase(String str1, String str2) {
        return (str1 == null ? str2 == null : str1.equalsIgnoreCase(str2));
    }

    /**
     * Checks if is numeric.
     *
     * @param str the str
     * @return true, if is numeric
     */
    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        int sz = str.length();
        for (int i = 0; i < sz; i++) {
            if (Character.isDigit(str.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }
}