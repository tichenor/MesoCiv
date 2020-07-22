package main.java.myFXtutorial.classes;

import java.io.Serializable;

/**
 * A modifier does something to a component (generator, automator, the world, etc).
 * All modifier main.java.myFXtutorial.classes extending the abstract base class must implement methods onEnable and onDisable.
 * These methods are supposed to apply (or remove) the modification e.g. production or speed
 * bonus to the component they are affecting.
 */
public abstract class Modifier extends Purchasable implements Serializable {

    private boolean enabled = false;
    private long levelRequirement = 0;

    /**
     * Semantic string description of a modifier's type (is it a new technology, a scientific development, etc.?)
     */
    private String type = "no type";

    private Modifier(World world) {
        super(world);
    }

    protected abstract void onEnable();
    protected abstract void onDisable();
    public abstract double getMultiplier();
    public abstract long getLevelRequirement();

    public void enable() {
        if (!enabled) {
            enabled = true;
            getWorld().addModifier(this);
            onEnable();
        }
    }

    public void disable() {
        if (enabled) {
            onDisable();
            getWorld().removeModifier(this);
            enabled = false;
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Generator getTarget() {
        return null;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    /**
     * Modifier for worlds. Affects global things in the game, such as
     * how fast time elapses.
     */
    static class WorldModifier extends Modifier {

        private double globalMultiplier;

        WorldModifier(World w) {
            super(w);
        }

        @Override
        protected void onEnable() {
            // Apply the speed modifier to the world.
            if (globalMultiplier != 1.0) {
                double multiplierBefore = getWorld().getGlobalMultiplier();
                double multiplierAfter = globalMultiplier *= multiplierBefore;
                getWorld().setGlobalMultiplier(multiplierAfter);
            }
        }

        @Override
        protected void onDisable() {
            // Remove the speed modifier from the world.
            if (globalMultiplier != 1.0) {
                double d = getWorld().getGlobalMultiplier();
                d /= globalMultiplier;
                getWorld().setGlobalMultiplier(d);
            }
        }

        @Override
        public double getMultiplier() {
            return globalMultiplier;
        }

        @Override
        public long getLevelRequirement() {
            return 0;
        }
    }

    /**
     * Modifier for generators. Modifies properties of a target generator.
     */
    public static class GeneratorModifier extends Modifier {

        private final Generator generator;
        private double productionMultiplier = 1.0;
        private long levelRequirement = 0;

        GeneratorModifier(Generator g) {
            super(g.getWorld());
            generator = g;
        }

        @Override
        protected void onEnable() {
            // The generator handles the logic of modifiers affecting it.
            generator.attachModifier(this);
        }

        @Override
        protected void onDisable() {
            generator.detachModifier(this);
        }

        @Override
        public double getMultiplier() {
            return productionMultiplier;
        }

        @Override
        public long getLevelRequirement() {
            return levelRequirement;
        }

        @Override
        public Generator getTarget() {
            return generator;
        }

    }

    /**
     * Builder class for the modifiers.
     */
    public static class Builder {

        /**
         * Modifier settings class for world modifiers.
         */
        public static class WorldTarget {

            World world;
            private double globalMultiplier = 1.0;

            WorldTarget(World w) {
                this.world = w;
            }

            public WorldTarget multiplier(double globalMultiplier) {
                this.globalMultiplier = globalMultiplier;
                return this;
            }

            public Modifier build() {
                WorldModifier wm = new WorldModifier(world);
                wm.globalMultiplier = globalMultiplier;
                return wm;
            }
        }

        /**
         * Modifier settings class for generator modifiers.
         */
        public static class GeneratorTarget {

            private final Generator generator;
            private double productionMultiplier = 1.0;
            private long levelRequirement = 0;

            GeneratorTarget(Generator g) {
                generator = g;
            }

            public GeneratorTarget productionMultiplier(double multiplier) {
                productionMultiplier = multiplier;
                return this;
            }

            public GeneratorTarget levelRequirement(long lvl) {
                levelRequirement = lvl;
                return this;
            }

            public Modifier build() {
                GeneratorModifier gm = new GeneratorModifier(generator);
                gm.productionMultiplier = productionMultiplier;
                gm.levelRequirement = levelRequirement;
                return gm;
            }

        }

        /**
         * Create a settings class for a world modifier.
         * Example:
         * Modifier myNewWorldMod = Modifier.Builder.modify(myWorld)
         *                                          .speedBy(2.0)
         *                                          .build();
         * myNewWorldMod.enable();
         * myWorld.update(10.0); // The world has advanced 20 seconds instead of 10.
         */
        public final WorldTarget modify(World world) {
            return new WorldTarget(world);
        }

        /**
         * Create a settings class for a generator modifier.
         */
        public final GeneratorTarget modify(Generator generator) {
            return new GeneratorTarget(generator);
        }

    }

}
