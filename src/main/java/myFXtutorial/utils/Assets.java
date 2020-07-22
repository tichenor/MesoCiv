package main.java.myFXtutorial.utils;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Assets {

    private static final String TIERS_PATH = "tiers.csv";
    public static final String TIER_UPGRADES_PATH = "tierUpgrades.csv";
    public static final String GLOBAL_UPGRADES_PATH = "globalUpgrades.csv";

    private static final String DELIMITER = ";";

    /**
     * Dictionary of data corresponding to a particular integer tier.
     */
    public static Map<Integer, TierData> tiers = new HashMap<>();

    /**
     * Upgrades for a specific building/tier.
     */
    public static Map<Integer, List<TierUpgradeData>> tierUpgrades = new HashMap<>();

    /**
     * Upgrades that affect all buildings/tiers or other global parameters.
     */
    public static List<GlobalUpgradeData> globalUpgrades = new ArrayList<>();

    public static void loadAssets() {
        loadTiers();
        loadTierUpgrades();
        loadGlobalUpgrades();
    }

    private static void loadGlobalUpgrades() {

        String line;
        ClassLoader classLoader = Assets.class.getClassLoader();
        URL resource = classLoader.getResource(GLOBAL_UPGRADES_PATH);
        if (resource == null)
            throw new IllegalArgumentException("File cannot be found.");
        File f = new File(resource.getFile());

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String headerLine = reader.readLine(); // Skip first line
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(DELIMITER);
                globalUpgrades.add(new GlobalUpgradeData(rowData));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void loadTierUpgrades() {

        for (int i = 1; i < tiers.keySet().size() + 1; i++) {
            tierUpgrades.put(i, new ArrayList<>());
        }

        String line;
        ClassLoader classLoader = Assets.class.getClassLoader();
        URL resource = classLoader.getResource(TIER_UPGRADES_PATH);
        if (resource == null)
            throw new IllegalArgumentException("File cannot be found.");
        File f = new File(resource.getFile());

        try (BufferedReader reader = new BufferedReader(new FileReader(f))) {
            String headerLine = reader.readLine(); // Skip first line
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(DELIMITER);
                int tierIndex = Integer.parseInt(rowData[0]);
                tierUpgrades.get(tierIndex).add(new TierUpgradeData(rowData));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadTiers() {

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
                tiers.put(tierIndex, new TierData(row));
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

    public static List<TierUpgradeData> upgradesFor(int tier) {
        return tierUpgrades.get(tier);
    }

    public static class GlobalUpgradeData {

        private final String name;
        private final BigInteger cost;
        private final double multiplier;
        private final String unlockRequirement;
        private final int unlockValue;
        private final String type;
        private final String description;

        public GlobalUpgradeData(String[] rowData) {
            name = rowData[0].equals("") ? "Nameless upgrade" : rowData[0];
            cost = rowData[1].equals("") ? BigInteger.ONE : new BigInteger(rowData[1]);
            multiplier = rowData[2].equals("") ? 1.0 : Double.parseDouble(rowData[2]);
            unlockRequirement = rowData[3].equals("") ? "none" : rowData[3];
            unlockValue = rowData[4].equals("") ? 0 : Integer.parseInt(rowData[4]);
            type = rowData[5].equals("") ? "general" : rowData[5];
            description = rowData[6].equals("") ? "No description" : rowData[6];
        }

        public String getName() {
            return name;
        }
        public BigInteger getCost() {
            return cost;
        }
        public double getMultiplier() {
            return multiplier;
        }
        public String getUnlockRequirement() {
            return unlockRequirement;
        }
        public int getUnlockValue() {
            return unlockValue;
        }
        public String getType() { return type; }
        public String getDescription() {
            return description;
        }
    }

    public static class TierUpgradeData {

        private final double productionMultiplier;
        private final int levelRequirement;
        private final String name;
        private final BigInteger cost;
        private final String type;
        private final String description;

        TierUpgradeData(String[] rowData) {
            productionMultiplier = Double.parseDouble(rowData[1]);
            levelRequirement = Integer.parseInt(rowData[2]);
            name = rowData[3].equals("") ? "Nameless upgrade" : rowData[3];
            cost = rowData[4].equals("") ? BigInteger.ONE : new BigInteger(rowData[4]);
            type = rowData[5];
            description = rowData[6];
        }

        public double getProductionMultiplier() {
            return productionMultiplier;
        }

        public int getLevelRequirement() {
            return levelRequirement;
        }

        public String getName() {
            return name;
        }

        public BigInteger getCost() {
            return cost;
        }

        public String getType() {
            return type;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Class that is only meant to hold the specific data for a tier/building. This class is not
     * public because its data is bound to a specific tier and getter methods are located in the
     * outer class (Assets.java) and take an integer tier as argument. Thus retrieving data is made
     * only through its outer class.
     */
    static class TierData {

        private final String name;
        private final BigInteger baseCost;
        private final double costMultiplier;
        private final BigInteger baseValue;
        private final String description;

        TierData(String[] rowData) {
            name = rowData[1];
            baseCost = new BigInteger(rowData[2]);
            costMultiplier = Double.parseDouble(rowData[3]);
            baseValue = new BigInteger(rowData[4]);
            description = rowData[5];
        }
    }

}
