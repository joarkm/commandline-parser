package com.joarkm.util;

public class StringUtils {
	public static boolean isNullOrEmpty(String[] s)
	{
		if (s == null || s.length == 0) return true;
		for (String str : s) {
			if(!StringUtils.isNullOrEmpty(str))
				return false;
		}
		return true;
	}
	public static boolean isNullOrEmpty(String s)
	{
	    return s == null || s.length() == 0;
	}
	public static boolean isNullOrWhitespace(String s)
	{
	    return s == null || isWhitespace(s);

	}
	private static boolean isWhitespace(String s)
	{
	    int length = s.length();
	    if (length > 0) {
	        for (int i = 0; i < length; i++) {
	            if (!Character.isWhitespace(s.charAt(i))) {
	                return false;
	            }
	        }
	        return true;
	    }
	    return false;
	}
}
