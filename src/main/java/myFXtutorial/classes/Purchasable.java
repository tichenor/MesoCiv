package main.java.myFXtutorial.classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

public abstract class Purchasable implements Serializable {

    protected BigInteger baseCost = BigInteger.ONE;
    protected String name = "Nameless item";
    protected String description = "No description.";
    protected long level = 0;
    protected long maxLevel = Long.MAX_VALUE;
    protected double costMultiplier = 1.145;

    private final World world;
    final ArrayList<Modifier> modifiers = new ArrayList<>();

    protected Purchasable(World world) {
        this.world = world;
    }

    protected Purchasable(World world, String name) {
        this.world = world;
        setName(name);
    }

    public PurchaseResult purchaseWith(Currency c) {
        if (c == null) {
            throw new IllegalArgumentException("Currency cannot be null.");
        }
        if (level >= maxLevel) {
            return PurchaseResult.MAX_LEVEL_REACHED;
        }
        BigInteger cost = getCurrentCost();
        BigInteger result = c.getValue().subtract(cost);
        if (result.signum() < 0) {
            return PurchaseResult.INSUFFICIENT;
        }
        c.sub(cost);
        levelUp();
        return PurchaseResult.OK;
    }

    public BigInteger getCurrentCost() {
        BigDecimal temp = new BigDecimal(baseCost);
        temp = temp.multiply(BigDecimal.valueOf(Math.pow(costMultiplier, level)));
        return temp.toBigInteger();
    }

    public void levelUp() {
        if (level < maxLevel) {
            level++;
        }
    }

    public void levelDown() {
        if (level > 0) {
            level--;
        }
    }

    public void maximizeLevel() {
        level = maxLevel;
    }

    // getters & setters

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigInteger getBaseCost() {
        return baseCost;
    }

    public double getCostMultiplier() {
        return costMultiplier;
    }

    public long getMaxLevel() {
        return maxLevel;
    }

    public long getLevel() {
        return level;
    }

    public World getWorld() {
        return world;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new RuntimeException("Purchasable name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setDescription(String text) {
        description = text;
    }

    public void setBaseCost(BigInteger baseCost) {
        if (baseCost == null) {
            throw new RuntimeException("Base cost cannot be null.");
        }
        if (baseCost.equals(BigInteger.ZERO)) {
            throw new RuntimeException("Base cost cannot be zero.");
        }
        this.baseCost = baseCost;
    }

    public void setCostMultiplier(double costMultiplier) {
        this.costMultiplier = costMultiplier;
    }

    public void setMaxLevel(long maxLevel) {
        if (maxLevel <= 0) {
            throw new RuntimeException("Max level of purchasable cannot be zero or negative.");
        }
        this.maxLevel = maxLevel;
    }

    public void setLevel(long level) {
        this.level = level < 0 ? 0 : Math.min(level, maxLevel);
    }
}
