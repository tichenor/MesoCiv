package main.java.myFXtutorial;

import main.java.myFXtutorial.classes.*;
import main.java.myFXtutorial.utils.Constants;

import java.math.BigInteger;

public class GameManager {

    private World world;

    private Currency coins;

    private Generator g1, g2, g3, g4, g5, g6, g7, g8, g9, g10, g11;

    // ALL AUTOMATORS TICK ONCE A SECOND; THIS GAME DOESN'T USE AUTOMATION AS A MECHANIC.
    private Automator a1, a2, a3, a4, a5, a6, a7, a8, a9, a10, a11;

    private Formatter printCoins;

    private double testTimer = 0.0;

    public GameManager() {
        world = new World();
    }

    /**
     *
     */
    public void initialize() {

        coins = new Currency.Builder(world)
                .name("Coins")
                .build();

        // Generators must be created in order so that indicies of world.getGenerators() is in the correct order
        g1 = new Generator.Builder(world)
                .baseCost(Constants.T1_BASE_COST)
                .baseValue(Constants.T1_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g2 = new Generator.Builder(world)
                .baseCost(Constants.T2_BASE_COST)
                .baseValue(Constants.T2_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g3 = new Generator.Builder(world)
                .baseCost(Constants.T3_BASE_COST)
                .baseValue(Constants.T3_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g4 = new Generator.Builder(world)
                .baseCost(Constants.T4_BASE_COST)
                .baseValue(Constants.T4_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g5 = new Generator.Builder(world)
                .baseCost(Constants.T5_BASE_COST)
                .baseValue(Constants.T5_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g6 = new Generator.Builder(world)
                .baseCost(Constants.T6_BASE_COST)
                .baseValue(Constants.T6_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g7 = new Generator.Builder(world)
                .baseCost(Constants.T7_BASE_COST)
                .baseValue(Constants.T7_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g8 = new Generator.Builder(world)
                .baseCost(Constants.T8_BASE_COST)
                .baseValue(Constants.T8_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g9 = new Generator.Builder(world)
                .baseCost(Constants.T9_BASE_COST)
                .baseValue(Constants.T9_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g10 = new Generator.Builder(world)
                .baseCost(Constants.T10_BASE_COST)
                .baseValue(Constants.T10_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        g11 = new Generator.Builder(world)
                .baseCost(Constants.T11_BASE_COST)
                .baseValue(Constants.T11_BASE_PROD)
                .costMultiplier(Constants.STD_COST_MULT)
                .generate(coins)
                .build();

        a1 = new Automator.Builder(world)
                .automate(g1)
                .tickRate(1.0)
                .build();

        a2 = new Automator.Builder(world)
                .automate(g2)
                .tickRate(1.0)
                .build();

        a3 = new Automator.Builder(world)
                .automate(g3)
                .tickRate(1.0)
                .build();

        a4 = new Automator.Builder(world)
                .automate(g4)
                .tickRate(1.0)
                .build();

        a5 = new Automator.Builder(world)
                .automate(g5)
                .tickRate(1.0)
                .build();

        a6 = new Automator.Builder(world)
                .automate(g6)
                .tickRate(1.0)
                .build();

        a7 = new Automator.Builder(world)
                .automate(g7)
                .tickRate(1.0)
                .build();

        a8 = new Automator.Builder(world)
                .automate(g8)
                .tickRate(1.0)
                .build();

        a9 = new Automator.Builder(world)
                .automate(g9)
                .tickRate(1.0)
                .build();

        a10 = new Automator.Builder(world)
                .automate(g10)
                .tickRate(1.0)
                .build();

        a11 = new Automator.Builder(world)
                .automate(g11)
                .tickRate(1.0)
                .build();

        // Set all automator levels to 1 so that each generator automatically produces once a second.
        a1.levelUp();
        a2.levelUp();
        a3.levelUp();
        a4.levelUp();
        a5.levelUp();
        a6.levelUp();
        a7.levelUp();
        a8.levelUp();
        a9.levelUp();
        a10.levelUp();
        a11.levelUp();

        if (MesoCiv.DEV_MODE) {
            coins.add(BigInteger.valueOf(1000));
        }

    }

    public void update(double deltaTime) {
        world.update(deltaTime);
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
            total = total.add(g.getGeneratedAmountCount());
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
            perSecond = g.getGeneratedAmountCount();
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
                return g.purchaseWith(coins);
            }
        }
        return PurchaseResult.NOT_AVAILABLE;
    }
}
