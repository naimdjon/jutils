package no.ntnu.utils;

import java.util.List;

import static no.ntnu.utils.Col.newList;

public class NGrams {

    public static double computeSim(String element1, String element2, int n) {
        double res;
        if (StringUtils.isEmpty(element1, element2)) return 0.0;
        List<String> triS1 = newList();
        List<String> triS2 = newList();
        int compteur = 0;
        while (element1.substring(compteur).length() >= n) {
            triS1.add(element1.substring(compteur, compteur + n));
            compteur++;
        }
        compteur = 0;
        while (element2.substring(compteur).length() >= n) {
            triS2.add(element2.substring(compteur, compteur + n));
            compteur++;
        }
        int nbCommonTriGr = 0;
        for (String aTriS1 : triS1)
            for (String aTriS2 : triS2)
                if (aTriS1.equals(aTriS2)) nbCommonTriGr++;

        res = (double) (2 * nbCommonTriGr) / ((double) triS1.size() + (double) triS2.size());
        return res;
    }
}
