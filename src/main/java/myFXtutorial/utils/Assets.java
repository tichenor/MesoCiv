package main.java.myFXtutorial.utils;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Assets {

    private static final String TIERS_PATH = "tiers.csv";
    private static final String DELIMITER = ",";

    public static Map<Integer, Tier> tiers = new HashMap<>();

    public static void loadAssets() {

        String line;

        ClassLoader classLoader = Assets.class.getClassLoader();
        URL resource = classLoader.getResource(TIERS_PATH);
        if (resource == null)
            throw new IllegalArgumentException("File cannot be found.");
        File f = new File(resource.getFile());

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String headerLine = reader.readLine(); // Skip first line
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(DELIMITER);
                int tierIndex = Integer.parseInt(row[0]);
                tiers.put(tierIndex, new Tier(row));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String tierName(int tier) {
        return tiers.get(tier).name;
    }

    public static BigInteger tierBaseCost(int tier) {
        return tiers.get(tier).baseCost;
    }

    public static BigInteger tierBaseValue(int tier) {
        return tiers.get(tier).baseValue;
    }

    public static double tierCostMultiplier(int tier) {
        return tiers.get(tier).costMultiplier;
    }

    public static String tierDescription(int tier) {
        return tiers.get(tier).description;
    }

    static class Tier {

        private final String name;
        private final BigInteger baseCost;
        private final double costMultiplier;
        private final BigInteger baseValue;
        private final String description;

        Tier(String[] rowData) {
            this.name = rowData[1];
            this.baseCost = new BigInteger(rowData[2]);
            this.costMultiplier = Double.parseDouble(rowData[3]);
            this.baseValue = new BigInteger(rowData[4]);
            this.description = rowData[5];
        }
    }

}
