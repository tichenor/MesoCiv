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

    private Modifier(World world) {
        super(world);
    }

    protected abstract void onEnable();
    protected abstract void onDisable();

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

    /**
     * Modifier for worlds. Affects global things in the game, such as
     * how fast time elapses.
     */
    static class WorldModifier extends Modifier {

        private double speedMultiplier;

        WorldModifier(World w) {
            super(w);
        }

        @Override
        protected void onEnable() {
            // Apply the speed modifier to the world.
            if (speedMultiplier != 1.0) {
                double speedBefore = getWorld().getSpeedMultiplier();
                double speedAfter = speedMultiplier *= speedBefore;
                getWorld().setSpeedMultiplier(speedAfter);
            }
        }

        @Override
        protected void onDisable() {
            // Remove the speed modifier from the world.
            if (speedMultiplier != 1.0) {
                double d = getWorld().getSpeedMultiplier();
                d /= speedMultiplier;
                getWorld().setSpeedMultiplier(d);
            }
        }
    }

    /**
     * Modifier for generators. Modifies properties of a target generator.
     */
    static class GeneratorModifier extends Modifier {

        private final Generator generator;
        private double productionMultiplier = 1.0;

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

        double getProductionMultiplier() {
            return productionMultiplier;
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
            private double speedMultiplier = 1.0;

            WorldTarget(World w) {
                this.world = w;
            }

            public WorldTarget speedBy(double speedMultiplier) {
                this.speedMultiplier = speedMultiplier;
                return this;
            }

            public Modifier build() {
                WorldModifier wm = new WorldModifier(world);
                wm.speedMultiplier = speedMultiplier;
                return wm;
            }
        }

        /**
         * Modifier settings class for generator modifiers.
         */
        public static class GeneratorTarget {

            private Generator generator;
            private double productionMultiplier = 1.0;

            GeneratorTarget(Generator g) {
                generator = g;
            }

            public GeneratorTarget productionMultiplier(double multiplier) {
                productionMultiplier = multiplier;
                return this;
            }

            public Modifier build() {
                GeneratorModifier gm = new GeneratorModifier(generator);
                gm.productionMultiplier = productionMultiplier;
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