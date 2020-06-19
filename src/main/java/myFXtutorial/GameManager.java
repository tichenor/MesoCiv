package main.java.myFXtutorial;

import main.java.myFXtutorial.classes.*;
import main.java.myFXtutorial.classes.Currency;
import main.java.myFXtutorial.utils.Assets;

import java.math.BigInteger;
import java.util.*;

public class GameManager {

    private final World world;

    private Currency coins;

    private final ProgressManager progressManager;

    private Stack<ProgressManager.Achievement> achievementStack;

    // ALL AUTOMATORS TICK ONCE A SECOND; THIS GAME DOESN'T USE AUTOMATION AS A MECHANIC.

    private final Map<Generator, List<Modifier>> generatorUpgrades = new HashMap<>();
    private final List<Modifier> globalUpgrades = new ArrayList<>();


    public GameManager() {
        world = new World();
        progressManager = new ProgressManager();
    }

    /**
     *
     */
    public void initialize() {

        achievementStack = new Stack<>();

        coins = new Currency.Builder(world)
                .name("Coins")
                .build();

        if (MesoCiv.DEV_MODE) {
            coins.add(BigInteger.valueOf(2000));
        }

        // Generators must be created in order so that indices of world.getGenerators() is in the correct order
        for (int tier = 1; tier < 11 + 1; tier++) {

            Generator g = new Generator.Builder(world)
                    .name(Assets.tierName(tier))
                    .baseCost(Assets.tierBaseCost(tier))
                    .baseValue(Assets.tierBaseValue(tier))
                    .costMultiplier(Assets.tierCostMultiplier(tier))
                    .generate(coins)
                    .build();

            // Automaton is not used in this game currently as a mechanic, all automators are set to tick
            // once a second and are level 1. This means each generator always produces its production value
            // once a second.
            Automator a = new Automator.Builder(world)
                    .automate(g)
                    .tickRate(1.0)
                    .build();

            a.levelUp(); // Level it up to 1 so it has effect.
        }

        // Initialize tier upgrades
        for (int i = 1; i < 11 + 1; i++) {
            Generator target = world.getGenerators().get(i - 1);
            generatorUpgrades.put(target, new ArrayList<>());
            List<Assets.TierUpgradeData> tierUpgradeData = Assets.tierUpgrades.get(i);
            for (Assets.TierUpgradeData upgradeData : tierUpgradeData) {
                Modifier mod = new Modifier.Builder()
                        .modify(target)
                        .productionMultiplier(upgradeData.getProductionMultiplier())
                        .levelRequirement(upgradeData.getLevelRequirement())
                        .build();
                mod.setName(upgradeData.getName());
                mod.setDescription(upgradeData.getDescription());
                mod.setBaseCost(upgradeData.getCost());
                generatorUpgrades.get(target).add(mod);
            }
        }

        // Initialize global/other upgrades
        for (Assets.GlobalUpgradeData globalUpgradeData : Assets.globalUpgrades) {
            Modifier mod = new Modifier.Builder()
                    .modify(world)
                    .multiplier(globalUpgradeData.getMultiplier())
                    .build();
            mod.setName(globalUpgradeData.getName());
            mod.setDescription(globalUpgradeData.getDescription());
            mod.setBaseCost(globalUpgradeData.getCost());
            globalUpgrades.add(mod);

        }

        // Initialize progress manager: properties and achievements
        progressManager.defineProperty("totalBuildings10",
                0,
                ProgressManager.ActivationRule.GREATER_THAN,
                10,
                new ArrayList<>());
        progressManager.defineAchievement("Building apprentice",
                Collections.singletonList("totalBuildings10"));
    }

    public void update(double deltaTime) {
        world.update(deltaTime);
        List<ProgressManager.Achievement> unlockedAchievements = progressManager.checkAllAchievements();
        for (ProgressManager.Achievement a : unlockedAchievements) {
            achievementStack.push(a);
        }
        while (!achievementStack.empty())
            System.out.println("Unlocked " + achievementStack.pop().getName() + "!");
    }

    public void increment(int n) {
        coins.add(BigInteger.valueOf(n));
    }

    public BigInteger getCoins() {
        return coins.getValue();
    }

    /**
     * Compute the total income of coins per second.
     * @return A string representation of the total coins produced per second.
     */
    public BigInteger getPerSecond() {
        BigInteger total = BigInteger.ZERO;
        for (Generator g : world.getGenerators()) {
            total = total.add(g.getGeneratedAmount());
        }
        return total;
    }

    /**
     * Compute the income of coins per second of a particular building tier.
     * @param tier
     * @return
     */
    public BigInteger getPerSecond(int tier) {
        BigInteger perSecond = BigInteger.ZERO;
        Generator g = world.getGenerators().get(tier - 1);
        if (g != null) {
            perSecond = g.getGeneratedAmount();
        }
        return perSecond;
    }

    public BigInteger getPerSecondPerLevel(int tier) {
        BigInteger perSecond = BigInteger.ZERO;
        Generator g = world.getGenerators().get(tier - 1);
        if (g != null) {
            perSecond = g.getGeneratedAmountPerLevel();
        }
        return perSecond;
    }

    public long getLevelOf(int tier) {
        long level = 0;
        Generator g = world.getGenerators().get(tier - 1);
        if (g != null) {
            level = g.getLevel();
        }
        return level;
    }

    public BigInteger getCostOf(int tier) {
        BigInteger cost = BigInteger.ZERO;
        Generator g = world.getGenerators().get(tier - 1);
        if (g != null) {
            cost = g.getCurrentCost();
        }
        return cost;
    }

    public List<Modifier> getAvailableUpgradesFor(int tier) {
        List<Modifier> result = new ArrayList<>();
        Generator target = world.getGenerators().get(tier - 1);
        for (Modifier upgrade : generatorUpgrades.get(target)) {
            // An upgrade is available if the requirement is fulfilled and it hasn't been purchased (enabled).
            if (upgrade.getLevelRequirement() <= target.getLevel() && !upgrade.isEnabled()) {
                result.add(upgrade);
            }
        }
        return result;
    }

    public String getNameOf(int tier) {
        return world.getGenerators().get(tier - 1).getName();
    }

    /**
     * Attempt to purchase something and return the result.
     * @param type The type of item to purchase (tier/building, upgrade, ...).
     * @param tier The tier of the building or upgrade.
     * @return A PurchaseResult depending on the result of the purchase.
     */
    public PurchaseResult onPurchase(Purchasables type, int tier) {
        if (type == Purchasables.TIER) {
            Generator g = world.getGenerators().get(tier - 1);
            if (g != null) {
                PurchaseResult result = g.purchaseWith(coins);
                if (result == PurchaseResult.OK) {
                    progressManager.addValue("totalBuildings10", 1);
                    return result;
                }
            }
        }
        return PurchaseResult.NOT_AVAILABLE;
    }

    public PurchaseResult onUpgrade(Modifier modifier) {
        PurchaseResult result = modifier.purchaseWith(coins);
        if (result == PurchaseResult.OK) {
            modifier.enable();
        }
        return result;
    }
}
