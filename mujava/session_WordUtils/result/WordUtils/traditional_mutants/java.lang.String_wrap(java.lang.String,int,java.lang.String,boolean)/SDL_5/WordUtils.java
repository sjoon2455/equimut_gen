// This is a mutant program.
// Author : ysma

public class WordUtils
{

    public WordUtils()
    {
        super();
    }

    public static  java.lang.String wrap( java.lang.String str, int wrapLength )
    {
        return wrap( str, wrapLength, null, false );
    }

    public static  java.lang.String wrap( java.lang.String str, int wrapLength, java.lang.String newLineStr, boolean wrapLongWords )
    {
        if (str == null) {
            return null;
        }
        if (newLineStr == null) {
            newLineStr = "\n";
        }
        if (wrapLength < 1) {
            wrapLength = 1;
        }
        int inputLineLength = str.length();
        int offset = 0;
        java.lang.StringBuffer wrappedLine = new java.lang.StringBuffer( inputLineLength + 32 );
        wrappedLine.append( str.substring( offset ) );
        return wrappedLine.toString();
    }

    public static  java.lang.String capitalize( java.lang.String str )
    {
        return capitalize( str, null );
    }

    public static  java.lang.String capitalize( java.lang.String str, char[] delimiters )
    {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        java.lang.StringBuffer buffer = new java.lang.StringBuffer( strLen );
        boolean capitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt( i );
            if (isDelimiter( ch, delimiters )) {
                buffer.append( ch );
                capitalizeNext = true;
            } else {
                if (capitalizeNext) {
                    buffer.append( Character.toTitleCase( ch ) );
                    capitalizeNext = false;
                } else {
                    buffer.append( ch );
                }
            }
        }
        return buffer.toString();
    }

    public static  java.lang.String capitalizeFully( java.lang.String str )
    {
        return capitalizeFully( str, null );
    }

    public static  java.lang.String capitalizeFully( java.lang.String str, char[] delimiters )
    {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        str = str.toLowerCase();
        return capitalize( str, delimiters );
    }

    public static  java.lang.String uncapitalize( java.lang.String str )
    {
        return uncapitalize( str, null );
    }

    public static  java.lang.String uncapitalize( java.lang.String str, char[] delimiters )
    {
        int delimLen = delimiters == null ? -1 : delimiters.length;
        if (str == null || str.length() == 0 || delimLen == 0) {
            return str;
        }
        int strLen = str.length();
        java.lang.StringBuffer buffer = new java.lang.StringBuffer( strLen );
        boolean uncapitalizeNext = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt( i );
            if (isDelimiter( ch, delimiters )) {
                buffer.append( ch );
                uncapitalizeNext = true;
            } else {
                if (uncapitalizeNext) {
                    buffer.append( Character.toLowerCase( ch ) );
                    uncapitalizeNext = false;
                } else {
                    buffer.append( ch );
                }
            }
        }
        return buffer.toString();
    }

    public static  java.lang.String swapCase( java.lang.String str )
    {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        java.lang.StringBuffer buffer = new java.lang.StringBuffer( strLen );
        boolean whitespace = true;
        char ch = 0;
        char tmp = 0;
        for (int i = 0; i < strLen; i++) {
            ch = str.charAt( i );
            if (Character.isUpperCase( ch )) {
                tmp = Character.toLowerCase( ch );
            } else {
                if (Character.isTitleCase( ch )) {
                    tmp = Character.toLowerCase( ch );
                } else {
                    if (Character.isLowerCase( ch )) {
                        if (whitespace) {
                            tmp = Character.toTitleCase( ch );
                        } else {
                            tmp = Character.toUpperCase( ch );
                        }
                    } else {
                        tmp = ch;
                    }
                }
            }
            buffer.append( tmp );
            whitespace = Character.isWhitespace( ch );
        }
        return buffer.toString();
    }

    public static  java.lang.String initials( java.lang.String str )
    {
        return initials( str, null );
    }

    public static  java.lang.String initials( java.lang.String str, char[] delimiters )
    {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (delimiters != null && delimiters.length == 0) {
            return "";
        }
        int strLen = str.length();
        char[] buf = new char[strLen / 2 + 1];
        int count = 0;
        boolean lastWasGap = true;
        for (int i = 0; i < strLen; i++) {
            char ch = str.charAt( i );
            if (isDelimiter( ch, delimiters )) {
                lastWasGap = true;
            } else {
                if (lastWasGap) {
                    buf[count++] = ch;
                    lastWasGap = false;
                }
            }
        }
        return new java.lang.String( buf, 0, count );
    }

    private static  boolean isDelimiter( char ch, char[] delimiters )
    {
        if (delimiters == null) {
            return Character.isWhitespace( ch );
        }
        for (int i = 0, isize = delimiters.length; i < isize; i++) {
            if (ch == delimiters[i]) {
                return true;
            }
        }
        return false;
    }

}
