package no.ntnu.utils;


import java.util.*;
import java.util.regex.Pattern;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static no.ntnu.utils.Col.asReverseSortedList;

public class StringUtils {
    public static final String ENCODING = System.getProperty("file.encoding");
    public static final transient Pattern numberPattern = compile("-?[0-9]+(\\.[0-9]+)?([Ee][\\+-]?[0-9]+)?$");
    public static final transient Pattern integerPattern = compile("-?[0-9]+$");
    public static final transient Pattern multiWhiteSpacePattern = compile("\\s+");
    private static final String EMPTY = "";
    private static final String NBSP = new String(new char[]{160});
    public static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static boolean containsAnyIgnoreCase(String original, String... containingString) {
        if (!hasLength(original)) return false;
        original = original.toLowerCase();
        for (String s : containingString)
            if (original.contains(s.toLowerCase())) return true;
        return false;
    }

    /**
     * Compares this string to the specified object.
     * The result is <code>true</code> if and only if both arguments are
     * <code>null</code> or <code>s1</code> is a <code>String</code> object that represents
     * the same sequence of characters as <code>s2</code> object.
     *
     * @param s1 first String
     * @param s2 second String
     * @return <code>true</code> if both arguments are null or {@link String#equals(Object)} returns true
     */
    public static boolean equals(String s1, String s2) {
        return s1 == null ? s2 == null : (s1 == s2 || s1.equals(s2));
    }

    public static String trim(String s) {
        if (s == null) return s;
        return s.trim().replaceAll(NBSP, "");
    }

    public static boolean eq(String s1, String s2) {
        return equals(s1, s2);
    }

    public static boolean eqIgnoreCase(String s1, String s2) {
        return s1 == null ? s2 == null : (s1 == s2 || s1.equalsIgnoreCase(s2));
    }

    /**
     * Compares this string to the specified object.
     * The result is <code>true</code> if and only if <code>s1</code> is a <code>String</code> object that represents
     * the same sequence of characters as <code>s2</code> object.
     *
     * @param s1 first String
     * @param s2 second String
     * @return <code>true</code> if both arguments  are equal, i.e. {@link String#equals(Object)} returns true
     */
    public static boolean hardEquals(String s1, String s2) {
        return s1 != null && s2 != null && s1.equals(s2);
    }

    public static boolean anyElemEq(String s, String[] strings) {
        if (strings == null || strings.length < 1) return false;
        for (String elem : strings)
            if (eq(elem, s)) return true;
        return false;
    }

    /**
     * Compares <tt>firstString</tt> to the other specified strings. The method permits null <tt>firstString</tt> argument and any of <tt>strings</tt> can be null as well.
     *
     * @param firstString the first string to be compared
     * @param strings     an array of strings to be compared with <tt>firstString</tt>
     * @return true if any of <tt>strings</tt> equals to <tt>firstString</tt>.
     */
    public static boolean anyEquals(String firstString, String... strings) {
        if (strings == null || strings.length <= 0) return false;
        for (String string : strings)
            if (equals(string, firstString)) return true;
        return false;
    }

    /**
     * Prints and returns a string that contains  the stack trace of this exception
     *
     * @param t throwable that needs to be printed
     * @return a string containing stack trace.
     * @deprecated use #Sys.stackTraceToStr()
     */
    public static String stackTraceToString(Throwable t) {
        java.io.StringWriter str = new java.io.StringWriter();
        java.io.PrintWriter wr = new java.io.PrintWriter(str);
        t.printStackTrace(wr);
        return str.toString();
    }


    /**
     * Checks if the string is null. Returns an empty string if this string is null or the string itself otherwise.
     * <p> This method is usefull when we would like to avoid null strings.
     *
     * @param s string to be checked.
     * @return an empty string if this string is null or the string itself otherwise.
     */
    public static String checkNull(String s) {
        return s == null ? "" : s;
    }

    public static String str(String s) {
        return checkNull(s);
    }

    public static String def(String s, String def) {
        return s == null ? def : s;
    }

    /**
     * Returns empty string in case this string is null
     *
     * @param s string to be normalized
     * @return normalized string, which is usually, a string with possible whitespaces removed.
     */
    public static String normalize(String s) {
        return checkNull(s);
    }

    /**
     * Checks if the specified string is null or not. NOTE: This method trims the string before checking whether or not it is empty, thus
     * sending an empty string will result in false.
     * <p><pre>
     * StringUtils.hasLength(null) = false
     * StringUtils.hasLength("") = false
     * StringUtils.hasLength(" ") = false
     * StringUtils.hasLength("Hei") = true
     * StringUtils.hasLength(" Hei") = true
     * </pre>
     *
     * @param s string to be checked
     * @return true if the string is not empty and false otherwise.
     */
    public static boolean hasLength(String s) {
        return s != null && s.trim().length() > 0;
    }

    public static boolean hasLengthAny(String... s) {
        if (s == null) return false;
        for (String ss : s)
            if (hasLength(ss)) return true;
        return false;
    }

    /**
     * Returns true if all strings satisfy <tt>hasLength(String)</tt>.
     *
     * @param s strings to be checked
     * @return true if all strings are not empty, false otherwise.
     */
    public static boolean hasLength(String... s) {
        for (String str : s)
            if (!hasLength(str)) return false;

        return true;
    }

    /**
     * Checks if the specified string is null or not. NOTE: This method trims the string before checking whether or not it is empty, thus
     * sending an empty string will result in false.
     *
     * @param s string to be checked
     * @return true if the string is empty and false otherwise.
     */
    public static boolean empty(Object s) {
        return s == null || s.toString() == null || s.toString().trim().length() <= 0;
    }

    public static boolean startsWith(String s, String prefix) {
        return s != null && s.startsWith(prefix);
    }

    public static boolean startsWithAny(String s, String... prefix) {
        if (prefix == null || prefix.length <= 0) return false;
        for (String p : prefix) {
            if (startsWith(s, p)) return true;
        }

        return false;
    }

    /**
     * Appends the following value as tag specified by <tt>tagName</tt> as XML tag if value is not null
     *
     * @param tagName the tag name of xml element
     * @param value   the value to be appended.
     * @param s       string buffer to be appended to.
     */
    public static void appendIfNotNull(String tagName, Object value, StringBuffer s) {
        if (empty(value)) return;
        s.append("<").append(tagName).append(">").append(value).append("</").append(tagName).append(">");
    }

    /**
     * Encodes the given string with the default system encoding.
     *
     * @param str string to be encoded.
     * @return encoded string.
     */
/*    public static String encode(String str) {
        if (str == null)
            return "";

        try {
            return java.net.URLEncoder.encode(str, ENCODING);
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return "";
        }
    }*/

    /**
     * Decodes the string with default system encoding.
     *
     * @param str string to be decoded
     * @return decoded string.
     */
    public static String urlDecode(String str) {
        if (str == null)
            return "";

        try {
            return java.net.URLDecoder.decode(str, ENCODING);
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace(System.err);
            return "";
        }
    }

    /**
     * Checks if the specified string represents a number. Usefull for parsing strings that are numbers.
     *
     * @param str string representing number
     * @return true if the <tt>str</tt> is a number, false otherwise
     */
    public static boolean isNumber(String str) {
        return hasLength(str) && numberPattern.matcher(str.trim()).matches();
    }

    /**
     * Checks if the specified string represents an integer-type number.
     *
     * @param str string representing number
     * @return true if the <tt>str</tt> is an integer, false otherwise
     */
    public static boolean isInteger(String str) {
        return hasLength(str) && integerPattern.matcher(str.trim()).matches();
    }


    /**
     * Checks if the type of the Object is string and checks if it can be parsed as a Number.
     *
     * @param obj Object to be checked.
     * @return true if it can be parsed as a Number.
     */
    public static boolean isParseableNumber(Object obj) {
        return obj != null && (obj instanceof String) && isNumber((String) obj);
    }

    /**
     * Checks if the specified string is true. The check is not case-censitive. The values to be checked true|false are(1|0, true|false, on|off).
     *
     * @param str string to be checked
     * @return true if the str is true, false otherwise.
     */
    public static boolean isTrue(String str) {
        return hasLength(str) && anyEquals(str.toLowerCase().trim(), "1", "on", "true");
    }

    public static String cat(String... strs) {
        return concat(strs);
    }

    public static String concat(String... strs) {
        StringBuilder sb = new StringBuilder();
        for (String s : strs)
            sb.append(s);
        return sb.toString();
    }

    public static boolean contains(String original, String containingString) {
        return hasLength(original) && hasLength(containingString) && original.contains(containingString);
    }

    public static boolean containsAny(String original, Collection<String> containingString) {
        if (!hasLength(original)) return false;
        for (String s : containingString)
            if (original.contains(s)) return true;

        return false;
    }

    public static boolean containsAny(String original, String... containingString) {
        if (!hasLength(original)) return false;
        for (String s : containingString)
            if (original.contains(s)) return true;

        return false;
    }

    public static String substringAfter(String str, String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }


    public static String substringBefore(String str, String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.length() == 0) {
            return EMPTY;
        }
        int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    public static String substringBeforeAny(String original, String... s) {
        if (!hasLength(original)) return original;

        for (String str : s) {
            if (original.indexOf(str) > -1)
                original = original.substring(0, original.indexOf(str));
        }

        return original;
    }

    /**
     * Tests whether the string <tt>s</tt> is uppercase
     *
     * @param s string to be checked
     * @return true if the string is uppercase.
     */
    public static boolean isUppercase(String s) {
        return isUppercase(s, s.length());
    }

    /**
     * Tests whether the number of <tt>numberOfStartChars</tt> characters in the beginning of the string <tt>s</tt> are uppercase
     *
     * @param s                  string to be checked
     * @param numberOfStartChars number of starting characters
     * @return true if the number of <tt>numberOfStartChars</tt> characters in the beginning of the string <tt>s</tt> are uppercase.
     */
    public static boolean isUppercase(String s, int numberOfStartChars) {
        boolean retval = hasLength(s) && s.length() >= numberOfStartChars;
        for (int i = 0; i < numberOfStartChars; i++) {
            final char c = s.charAt(i);
            if (Character.isLetter(c) && !Character.isUpperCase(c)) retval = false;
        }
        return retval;
    }

    /**
     * Returns first non-empty string.
     *
     * @param strs array of strings
     * @return first non-empty string.
     */
    public static String firstNonEmptyStr(String... strs) {
        if (strs == null || strs.length <= 0) {
            return null;
        }
        for (String s : strs) {
            if (hasLength(s)) return s;
        }
        return null;
    }

    /**
     * Determine whether a string is considered to be empty. An empty string is a null
     * pointer to a string or a string of zero length.
     *
     * @param s String to evaluate
     * @return true if the string is null or zero length
     */
    public static boolean isEmpty(String... s) {
        if (s == null || s.length < 1) return true;
        for (String ss : s)
            if (ss == null || ss.length() == 0 || ss.trim().length() == 0)
                return true;
        return false;
    }

    public static boolean isEmptyArr(String[] strings) {
        if (strings == null) return true;
        for (String s : strings)
            if (!isEmpty(s)) return false;
        return true;
    }

    public static boolean anyEmpty(String... strings) {
        if (strings == null) return true;
        for (String s : strings)
            if (isEmpty(s)) return true;
        return false;
    }


    /**
     * Splits the string into a Set of Strings separated by 'delim'.
     * Uses a StringTokenizer and returns a new HashSet, which is empty if no strings, never null.
     *
     * @param string
     * @param delim
     * @return
     */
    public static Set<String> split(String string, String delim) {
        Set<String> set = new HashSet<String>();
        if (string != null && delim != null) {
            StringTokenizer tok = new StringTokenizer(string, delim);
            while (tok.hasMoreTokens())
                set.add(tok.nextToken());
        } else if (hasLength(string)) set.add(string);
        return set;
    }

    public static List<String> splitOrdered(String string, String delim) {
        ArrayList<String> list = new ArrayList<String>();
        if (string != null && delim != null) {
            int i = 0;
            StringTokenizer tok = new StringTokenizer(string, delim);
            while (tok.hasMoreTokens())
                list.add(i++, tok.nextToken());
        } else if (hasLength(string)) list.add(string);
        return list;
    }

    public static Set<Long> parseCommaSeparatedLongs(String commaSeparated) {
        Set<Long> result = new HashSet<Long>();
        if (commaSeparated == null) return result;
        for (String s : commaSeparated.split(",")) if (StringUtils.isInteger(s)) result.add(Long.valueOf(s));
        return result;
    }


    /**
     * Replaces the first occurances of each string in <tt>oldStrings</tt> array with specified <tt>replacement</tt>
     *
     * @param originalString the original string.
     * @param replacement    the replacement string.
     * @param oldStrings     the array of regex string to be replaced.
     * @return the original string where each occurance of <tt>oldStrings</tt> was replaced with <tt>replacement</tt>
     */
    public static String replaceFirst(String originalString, String replacement, String... oldStrings) {
        if (oldStrings == null || oldStrings.length <= 0) return originalString;

        for (String s : oldStrings) {
            if (contains(originalString, s))
                originalString = originalString.replaceFirst(s, replacement);
        }
        return originalString;
    }

    /**
     * Replaces all occurrences of each string in <tt>oldStrings</tt> array with specified <tt>replacement</tt>
     *
     * @param originalString the original string.
     * @param replacement    the replacement string.
     * @param oldStrings     the array of regex string to be replaced.
     * @return the original string where each occurance of <tt>oldStrings</tt> was replaced with <tt>replacement</tt>
     */
    public static String replaceAll(String originalString, String replacement, String... oldStrings) {
        if (oldStrings == null || oldStrings.length <= 0) return originalString;

        for (String s : oldStrings) {
            if (contains(originalString, s))
                originalString = originalString.replaceAll(s, replacement);
        }
        return originalString;
    }

    /**
     * Removes the first occurrence of each string in <tt>oldStrings</tt>.
     * <p>Example:<br/><br/>
     * <code>
     * StringUtils.removeFirstOccurance("www.creaza.no","www.","test.")
     * </code>
     * will return creaza.no.
     *
     * @param originalString the original string.
     * @param occurrences    the array of regex string to be removed.
     * @return the original string where each string in oldStrings array getting removed.
     */
    public static String removeFirstOccurrence(String originalString, String... occurrences) {
        return replaceFirst(originalString, "", occurrences);
    }

    public static String replaceLast(String originalString, String str, String replacement) {
        final int index = originalString.lastIndexOf(str);
        if (index < 0) return originalString;

        StringBuilder b = new StringBuilder(originalString);
        b.replace(index, index + 1, replacement);
        return b.toString();
    }

    public static String removeLast(String originalString, String str) {
        return originalString.substring(0, originalString.lastIndexOf(str));
    }

    public static String removeAll(String str, String... stringsToBeRemoved) {
        for (String s : stringsToBeRemoved)
            str = str.replaceAll(s, "");
        return str;
    }

    public static String joinFiles(String... files) {
        return join(java.io.File.separator, files);
    }

    public static String join(String delimiter, String... strings) {
        if (strings == null || strings.length <= 0)
            return "";
        StringBuffer sb = new StringBuffer();
        delimiter = checkNull(delimiter);
        boolean isFirst = true;
        for (String s : strings) {
            if (isFirst) isFirst = false;
            else sb.append(delimiter);
            sb.append(s);
        }
        //sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Join a collection of objects into a String using the specified delimiter and toString on each object
     * From http://snippets.dzone.com/posts/show/91
     */
    public static <T> String join(final Collection<T> objs, final String delimiter) {
        if (objs == null || objs.isEmpty())
            return "";
        Iterator<T> iter = objs.iterator();
        if (!iter.hasNext())
            return "";
        StringBuffer buffer = new StringBuffer(String.valueOf(iter.next()));
        while (iter.hasNext()) {
            buffer.append(delimiter).append(String.valueOf(iter.next()));
        }
        return buffer.toString();
    }

    /**
     * Returns the size of this string in Bytes. The UTF-8 charset is used in conversion to bytes.
     *
     * @param s
     * @return
     */
    public static int size(String s) {
        return sizeUTF8(s);
    }

    public static int sizeUTF8(String s) {
        try {
            return s.getBytes("UTF-8").length;
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String substrBef(String s, String suf) {
        return s.substring(0, s.lastIndexOf(suf));
    }

    /**
     * Tokenizes string with the following delimeters:
     * <ul>
     * <li> space
     * <li> ,
     * <li> -
     * </ul>
     *
     * @param str
     * @return
     */
    public static Collection<String> tokenize(String str) {
        return tokenize(str, ", -");
    }

    public static Collection<String> tokenize(String str, String... delims) {
        String delim = "";
        for (String del : delims)
            delim = delim.concat(del);
        return tokenize(str, delim);
    }

    public static int countRegex(String content, String s) {
        String[] split = content.split(s);
        if (split != null && split.length > 0)
            return split.length - 1;
        return 0;
    }

    public static int count(String content, String s) {
        int counter = 0;
        int idx = 0;
        while ((idx = content.indexOf(s)) != -1) {
            counter++;
            final int beginIndex = idx + s.length() + 1;
            if (content.length() > beginIndex)
                content = content.substring(beginIndex);
            else break;
        }

        return counter;
    }

    public static Collection<String> tokenize(String str, String delim) {
        StringTokenizer st = new StringTokenizer(str, delim);
        Collection<String> l = Col.newSet();
        while (st.hasMoreTokens())
            l.add(st.nextToken());
        return l;
    }

    public static boolean endsWithAny(String url, String... s) {
        if (empty(url)) return false;
        for (String ss : s) {
            if (url.endsWith(ss))
                return true;
        }
        return false;
    }

    public static boolean endsWithAnyIgnoreCase(String str, String... s) {
        if (empty(str)) return false;
        str = str.toLowerCase();
        for (String ss : s) {
            if (str.endsWith(ss.toLowerCase()))
                return true;
        }
        return false;
    }

    public static StringBuilder clear(StringBuilder s) {
        s.delete(0, s.length());
        return s;
    }


    // this function replaces all occurrences by the delimiter in content, (process by longest occurrence)
    public static String maskOccurrences(String content, final String delimeter, final Collection<String> labels) {
        String tmp = content.toLowerCase();
        List<String> sortedLabels = asReverseSortedList(labels, new StrLenComparator());
        for (String label : sortedLabels) {
            label = label.toLowerCase();
            //D.d("checking %s",extension);
            int i;
            while ((i = tmp.indexOf(label)) != -1) {
                int endIndex = i + label.length();
                String occurrenceText = content.substring(i, endIndex);
                //D.d("occurrence text '%s'",occurrenceText);
                if (!occurrenceText.toLowerCase().equals(label)) {
                    Debug.d("tmp=%s", tmp.substring(0, endIndex + 100));
                    throw new IllegalStateException(format("'%s' does not equal to '%s' at index '%s':'%s'", occurrenceText, label, i, endIndex));
                }
                content = new StringBuilder()
                        .append(content.substring(0, i))
                        .append(delimeter)
                        .append(content.substring(endIndex, content.length()))
                        .toString();
                tmp = content.toLowerCase();//TODO!!!//tmp.replaceFirst(label, "XXXXX");//crucial that it is of 5 characters!!!
            }
        }
        return content;
    }


    public static Set<Integer> extractIndexes(String delim, String content) {
        //content=content.toLowerCase();
        // this function searches in the buffer for all indexes of a given string entity
        Set<Integer> indexes = new TreeSet<Integer>();
        int index = 0;
        while (index != -1 && index < content.length()) {
            index = content.indexOf(delim, index);
            if (index != -1) {
                indexes.add(index);
                index++;
            }
        }
        return indexes;
    }

    /**
     * Returns the token from the specified index in reverse order. E.g:
     * <p/>
     * <p><pre>
     * StringUtils.tokenReverse("This is str",9) = is
     * StringUtils.tokenReverse("Barack Obama is the 44th President",12) = Obama
     * </pre>
     *
     * @param s   the string
     * @param idx the offset
     * @return token
     */
    public static String tokenReverse(String s, final int idx) {
        return tokenReverse(new StringBuilder(s), idx);
    }

    public static String tokenReverse(StringBuilder s, final int idx) {
        //StringBuilder sb= new StringBuilder();
        int k = 0;
        for (int i = idx - 1; i >= 0; i--) {
            char c = s.charAt(i);
            //System.out.print(c);
            if (c != ' ')
                k++;
            else if (c == ' ' && k == 0)
                k++;
            else if (k > 0)
                break;
        }
        /*System.out.println();
        System.out.print(idx - k);
        System.out.println(":"+idx);*/
        return s.substring(idx - k, idx);
    }

    /**
     * @param s
     * @param idx
     * @return
     */
    public static String deleteAdj(String s, final int idx) {
        return deleteAdj(new StringBuilder(s), idx).toString();
    }


    public static StringBuilder deleteAdjacentTokens(String sb, final String del) {
        return deleteAdjacentTokens(new StringBuilder(sb), del);
    }

    public static StringBuilder deleteAdjacentTokens(StringBuilder sb, final String del) {
        int l = del.length();
        int idx;
        while ((idx = sb.indexOf(del)) > -1) {
            deleteAdj(sb, idx + l);
        }
        return sb;
    }

    public static StringBuilder deleteAdj(StringBuilder s, final int idx) {
        for (int i = idx - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c == ' ') break;
            s.deleteCharAt(i);
        }
        return s;
    }

    /**
     * Removes all the LF (new lines from this string).
     *
     * @param inputStr input string.
     * @return inputStr with all the new lines removed.
     */
    public static String removeLF(String inputStr) {
        return inputStr.replaceAll("\n", " ");
    }


    /**
     * Removes multi-whitespaces, e.g. "     " results in " ".
     *
     * @param inputStr input string.
     * @return inputStr with all the multiple whitespaces normalized into a single whitespace.
     */
    public static String removeMultipleWhitespace(String inputStr) {
        return multiWhiteSpacePattern.matcher(inputStr).replaceAll(" ");
    }

    /**
     * Causes to call <tt>{@link #checkNull(String)}</tt>, <tt>{@link #removeLF(String)}</tt> and <tt>{@link #removeMultipleWhitespace(String)}</tt>
     * <br/>
     * i.e., checks for null str, removes LF, and removes multiple whitespaces.
     *
     * @param s the string
     * @return normalized string.
     */
    public static String norm(String s) {
        s = checkNull(s);
        s = removeLF(s);
        s = removeMultipleWhitespace(s);
        return s;
    }


    public static String lowerCase(String str) {
        if (str == null) {
            return null;
        }
        return str.toLowerCase();
    }


    public static String join(Iterator iterator, char separator) {
        if (iterator == null) {
            return null;
        }
        StringBuffer buf = new StringBuffer(256);
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            if (obj != null) {
                buf.append(obj);
            }
            if (iterator.hasNext()) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }


    public static String[] split(String str, char separatorChar) {
        // Performance tuned for 2.0 (JDK1.4)

        if (str == null) {
            return null;
        }
        int len = str.length();
        if (len == 0) {
            return EMPTY_STRING_ARRAY;
        }
        List<String> list = Col.newList();
        int i = 0, start = 0;
        boolean match = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match) {
                    list.add(str.substring(start, i));
                    match = false;
                }
                start = ++i;
                continue;
            }
            match = true;
            i++;
        }
        if (match) {
            list.add(str.substring(start, i));
        }
        return (String[]) list.toArray(new String[list.size()]);
    }


}