package main.java.myFXtutorial.classes;

import java.io.Serializable;
import java.math.BigInteger;

public class Automator extends Purchasable implements Serializable {

    private Generator generator;
    private double tickRate = 1.0;
    private double tickTimer = 0.0;
    private double multiplier;
    private boolean enabled;
    private double actualTickRate;

    public static class Builder {

        private final World world;
        private Generator generator;
        private double tickRate = 1.0;
        private String name = "Nameless automator";
        private boolean enabled = true;
        private BigInteger baseCost = BigInteger.ONE;
        private double costMultiplier = 1.1;
        private double tickRateMultiplier = 1.08;

        public Builder(World world) {
            this.world = world;
        }

        public Builder baseCost(int cost) {
            baseCost = new BigInteger("" + cost);
            return this;
        }

        public Builder baseCost(long cost) {
            baseCost = new BigInteger("" + cost);
            return this;
        }

        public Builder baseCost(BigInteger cost) {
            baseCost = cost;
            return this;
        }

        public Builder costMultiplier(double multiplier) {
            costMultiplier = multiplier;
            return this;
        }

        public Builder tickRateMultiplier(double multiplier) {
            tickRateMultiplier = multiplier;
            return this;
        }

        public Builder automate(Generator generator) {
            this.generator = generator;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder tickRate(double seconds) {
            tickRate = seconds;
            return this;
        }

        public Automator build() {
            if (generator == null) {
                throw new IllegalStateException("Generator cannot be null.");
            }
            Automator a = new Automator(world, name);
            a.generator = generator;
            a.tickRate = tickRate;
            a.enabled = enabled;
            a.baseCost = baseCost;
            a.costMultiplier = costMultiplier;
            a.multiplier = tickRateMultiplier;
            world.addAutomator(a);
            return a;
        }
    }

    private Automator(World world, String name) {
        super(world, name);
    }

    void update(double seconds) {
        if (!enabled || level == 0) {
            return;
        }
        tickTimer += seconds;
        while (tickTimer >= actualTickRate) {
            tickTimer -= actualTickRate;
            generator.process();
        }
    }

    public void enable() {
        if (!enabled) {
            getWorld().addAutomator(this);
            enabled = true;
        }
    }

    public void disable() {
        if (enabled) {
            getWorld().removeAutomator(this);
            enabled = false;
        }
    }

    @Override
    public void levelUp() {
        super.levelUp();
        actualTickRate = getFinalTickRate();
    }

    private double getFinalTickRate() {
        if (level == 0) {
            return 0.0;
        }
        double rate = tickRate;
        double mult = Math.pow(multiplier, level - 1);
        return  rate / mult;
    }

    public double getTickRate() {
        return tickRate;
    }

    public void setTickRate(double tickRate) {
        this.tickRate = tickRate;
        if (this.tickRate < 0.0) {
            this.tickRate = 0.0;
        }
    }

    public double getTimerPercentage() {
        return tickRate != 0.0 ? tickTimer / tickRate : 1.0;
    }
}
