package main.java.myFXtutorial.utils;

import java.math.BigInteger;

public class StrFormatter {

    public static String formatTierLabel(BigInteger cost, int owned, BigInteger production) {
        //TODO: depends on data formats
        return "Cost: " + cost.toString()
                + "\nOwned: " + owned
                + "\n Producing: " + production.toString();
    }

    public static String fCostLabel(int cost) {
        return "Cost: " + cost;
    }

    public static String fCountLabel(int count) {
        return "Count: " + count;
    }

    public static String fProductionLabel(int prod) {
        return prod + " /s";
    }

}
