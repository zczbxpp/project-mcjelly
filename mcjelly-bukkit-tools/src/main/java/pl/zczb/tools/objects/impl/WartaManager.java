package pl.zczb.tools.objects.impl;

import java.util.Random;

public class WartaManager {
    private static final Random random = new Random();


    private static final double[] kwoty = new double[]{0.0D, 0.01D, 0.02D, 0.03D, 0.04D, 0.05D};


    private static final int[] wagi = new int[]{60, 25, 8, 4, 2, 1};

    public static double losujKwote() {
        int sumaWag = 0;
        for (int waga : wagi) {
            sumaWag += waga;
        }

        int los = random.nextInt(sumaWag);
        int suma = 0;

        for (int i = 0; i < kwoty.length; i++) {
            suma += wagi[i];
            if (los < suma) {
                return kwoty[i];
            }
        }
        return 0.0D;
    }
}


