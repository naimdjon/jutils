package no.ntnu.utils;


import java.util.Collection;

import static java.lang.Character.codePointAt;
import static no.ntnu.utils.Col.toCol;
import static no.ntnu.utils.StringUtils.*;

public class Clean {
    private static final Collection<String> wordsToClean = Col.toCol(
            "(", ")", "\"", "[", "]", ":", ".", ">", "<", "|", ".", ",", ";", ":"
    );

    private static final String[] wordsTrailingLeadingRemove = new String[]{
            "'", "´"
    };

    public static String clean(String token) {
//        String bef=token;
//        D.d("token bef:"+bef);
        StringBuilder sb = new StringBuilder(token);
        for (String s : wordsToClean) {
            int idx;
            while ((idx = token.indexOf(s)) != -1) {
                sb.deleteCharAt(idx);
                token = sb.toString();
            }
        }
        token = sb.toString();
        while (startsWithAny(token, wordsTrailingLeadingRemove) || endsWithAny(token, wordsTrailingLeadingRemove)) {
            for (String s : wordsTrailingLeadingRemove) {
                if (token.startsWith(s))
                    token = token.substring(s.length());
                if (token.endsWith(s))
                    token = token.substring(0, token.lastIndexOf(s));
            }
        }
        //D.d("token bef=%s, aft=%s",bef,token);
        return token.trim();
    }

    public static boolean shouldSkip(String token) {
        //if(containsAny(token, "e1", "e2")) D.d("token::::" + token);
        return empty(token)
                || stopWords.contains(token)
                || token.trim().length() == 1
                || containsAny(token, "${e1}", "${e2}", "http://", "http//")
                || (token.length() == 2 && codePointAt(token, 0) == 65533 && codePointAt(token, 1) == 65533)
                || (token.length() == 2 && token.contains("."))
                || allTheSameCodePoints(token);
    }

    static Collection<String> stopWords = toCol("${e1}", "${e2}", "${ent}", "of", "a", "and",
            "the", "in", "/", "from", "was", "on", "as", "with", "for", "same",
            "--the", "to", "at", "or", "this", "are", "no");

    private static boolean allTheSameCodePoints(String token) {
        int prev = codePointAt(token, 0);
        for (int i = 0; i < token.length(); i++) {
            if (prev != codePointAt(token, i))
                return false;
        }
        return true;
    }
}