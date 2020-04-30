package utils;

import java.math.BigInteger;

public class Formatter {

    public static String formatTierLabel(BigInteger cost, int owned, BigInteger production) {
        //TODO: depends on data formats
        String result =
                "Cost: " + cost.toString()
                        + "\nOwned: " + owned
                        + "\n Producing: " + production.toString();
        return result;
    }

    public static String fCostLabel(int cost) {
        return "Cost: " + cost;
    }

    public static String fCountLabel(int count) {
        return "Count: " + count;
    }

    public static String fProductionLabel(int prod) {
        return prod + " coins/sec";
    }

}
