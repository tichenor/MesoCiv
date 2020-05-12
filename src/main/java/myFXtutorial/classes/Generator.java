package main.java.myFXtutorial.classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

import main.java.myFXtutorial.classes.Modifier.GeneratorModifier;

/**
 * Base class for anything that produces currencies. Intended to be controlled
 * either manually by clicking or by using an Automator.
 */
public class Generator extends Purchasable implements Serializable {

    public interface Callback {
        void onProcessed();
    }

    private Callback callback = null;

    /*
    The currency a generator should produce.
     */
    private Currency targetCurrency;

    /*
    How many times a generator has produced.
     */
    private long timesProcessed = 0;

    /*
    The base amount a generator produces.
     */
    private BigInteger baseProduction;

    /*
    The increase in amount generated when a generator is upgraded/leveled up.
    The base formula is (baseProduction) * (productionMultiplier) ^ (level - 1).
    This amount is further potentially altered by modifiers.
     */
    private double productionMultiplier;

    /*
    Cooldown time between processing cycles.
     */
    private double cooldown;

    /*
    List of active modifiers attached to this generator.
     */
    private ArrayList<GeneratorModifier> modifiers = new ArrayList<>();

    public static class Builder {

        private final World world;
        private String name = "Nameless generator";
        private Callback onProcessed = null;
        private Currency targetCurrency = null;
        private BigInteger baseProduction = BigInteger.ONE;
        private double productionMultiplier = 1.1;
        private long maxLevel = Long.MAX_VALUE;
        private BigInteger baseCost = BigInteger.ONE;
        private double costMultiplier = 1.1;
        private double cooldown = 0.0;

        public Builder(World world) {
            this.world = world;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder valueMultiplier(double multiplier) {
            productionMultiplier = multiplier;
            return this;
        }

        public Builder maxLevel(long maxLevel) {
            if (maxLevel <= 0) {
                throw new IllegalArgumentException("Max level must be greater than 0.");
            }
            this.maxLevel = maxLevel;
            return this;
        }

        public Builder baseValue(BigInteger value) {
            if (value == null) {
                throw new IllegalArgumentException("Base value cannot be null.");
            }
            baseProduction = value;
            return this;
        }

        public Builder baseValue(long value) {
            baseProduction = new BigInteger("" + value);
            return this;
        }

        public Builder baseValue(int value) {
            baseProduction = new BigInteger("" + value);
            return this;
        }

        public Builder generate(Currency c) {
            if (c == null) {
                throw new IllegalArgumentException("Currency cannot be null.");
            }
            targetCurrency = c;
            return this;
        }

        public Builder callback(Callback callback) {
            onProcessed = callback;
            return this;
        }

        public Builder baseCost(BigInteger cost) {
            baseCost = cost;
            return this;
        }

        public Builder baseCost(long cost) {
            baseCost = new BigInteger("" + cost);
            return this;
        }

        public Builder baseCost(int cost) {
            baseCost = new BigInteger("" + cost);
            return this;
        }

        public Builder costMultiplier(double multiplier) {
            costMultiplier = multiplier;
            return this;
        }

        public Generator build() {

            Generator g = new Generator(world, name);
            g.callback = onProcessed;
            g.targetCurrency = targetCurrency;
            g.productionMultiplier = productionMultiplier;
            g.baseProduction = baseProduction;
            g.maxLevel = maxLevel;
            g.baseCost = baseCost;
            g.costMultiplier = costMultiplier;
            g.cooldown = cooldown;
            world.addGenerator(g);
            return g;

        }
    }

    private Generator(World world) {
        super(world);
    }

    private Generator(World world, String name) {
        super(world, name);
    }

    public BigInteger getGeneratedAmount() {
        /*
        The base amount generated is equal to
        (baseProduction) * (productionMultiplier) ^ (level - 1).
        This value is then possibly further altered by modifiers.
         */
        if (level == 0) {
            return BigInteger.ZERO;
        }
        BigDecimal temp = new BigDecimal(baseProduction);
        temp = temp.multiply(BigDecimal.valueOf(Math.pow(productionMultiplier, level - 1)));
        temp = processModifiers(temp);
        return temp.toBigInteger();
    }

    private BigDecimal processModifiers(BigDecimal val) {
        /*
        The final value is equal to
        (m1) * (m2) * ... (mn) * val
        where n is the number of modifiers and mi is the production multiplier of
        the i'th modifier.
         */
        if (modifiers.size() == 0) {
            return val;
        }
        for (GeneratorModifier m : modifiers) {
            double d = m.getProductionMultiplier();
            if (d != 1.0) {
                val = val.multiply(new BigDecimal(d));
            }
        }
        return val;
    }

    public void process() {
        targetCurrency.add(getGeneratedAmount());
        timesProcessed++;
        if (callback != null) {
            callback.onProcessed();
        }
    }

    public long getTimesProcessed() {
        return timesProcessed;
    }

    void attachModifier(Modifier.GeneratorModifier m) {
        if (m != null && !modifiers.contains(m)) {
            modifiers.add(m);
        }
    }

    void detachModifier(Modifier.GeneratorModifier m) {
        if (m != null) {
            modifiers.remove(m);
        }
    }

}
