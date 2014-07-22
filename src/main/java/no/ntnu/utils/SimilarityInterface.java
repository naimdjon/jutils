package no.ntnu.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static no.ntnu.utils.Clazz.invoke;

//this class requires secondstring-20060615.jar
public class SimilarityInterface {



    public static double computeAvg3Sim(String word1, String word2) {
        int cpt = 0;
        double sum = 0;
        if (word1.equals("") || word2.equals("")) return 0.0;
        for (Object mes : get3Measures()) {
            cpt++;
            Double s = (Double) invoke(mes, "score", word1, word2);
            sum += s;
        }
        return (sum / cpt);
    }

    public static double computeNGramsRounded(String word1, String word2) {
        final double d = computeNGrams(word1, word2, 3);
        try {
            if (d == Double.NaN) return d;
            BigDecimal bd = new BigDecimal(d);
            bd = bd.setScale(3, ROUND_HALF_UP);
            return bd.doubleValue();
        } catch (Exception e) {
            //D.e("ex::" + d);
            return d;
        }
    }

    public static double computeNGrams(String word1, String word2) {
        return computeNGrams(word1, word2, 3);
    }

    public static double computeNGrams(String word1, String word2, int n) {
        double sim = NGrams.computeSim(word1, word2, n);
        return sim;
    }

    public static double computeSetSimilarity(Collection<String> collection1, Collection<String> collection2) {
        // compute the similarity between two collections (only by comparing identical strings)
        if (collection1.size() == 0 || collection2.size() == 0) return 0.0;
        int similarCat = 0;
        for (String val1 : collection1)
            for (String val2 : collection2)
                if (val1.equalsIgnoreCase(val2)) {
                    similarCat++;
                    break;
                }
        double averageSizeCollections = ((double) collection1.size() + (double) collection2.size()) / 2.0;
        return (similarCat / averageSizeCollections);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List get3Measures() {
        List res = new ArrayList();
        res.add(Clazz.instantiate("com.wcohen.ss.JaroWinkler"));
        res.add(Clazz.instantiate("com.wcohen.ss.MongeElkan"));
        res.add(Clazz.instantiate("com.wcohen.ss.ScaledLevenstein"));
        return res;
    }


}