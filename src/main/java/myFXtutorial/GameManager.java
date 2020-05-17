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
                .baseCost(100)
                .baseValue(5)
                .costMultiplier(1.28)
                .generate(coins)
                .valueMultiplier(1.23)
                .build();

        a1 = new Automator.Builder(world)
                .automate(g1)
                .tickRate(1.0)
                .build();

        printCoins = new Formatter.ForCurrency(coins)
                .groupDigits() // Group digits into groups of three
                .showHighestThousand() // Show only the highest triplet in the amount
                .showDecimals(2) // Show two decimals if truncating lower numbers
                .build();

        // Set all automator levels to 1 so that each generator automatically produces once a second.
        a1.levelUp();

    }

    public void update(double deltaTime) {
        world.update(deltaTime);
    }

    public void increment(int n) {
        coins.add(BigInteger.valueOf(n));
    }

    public String getCoins() {
        return printCoins.toString();
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
