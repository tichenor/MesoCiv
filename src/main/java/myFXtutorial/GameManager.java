package main.java.myFXtutorial;

import main.java.myFXtutorial.classes.Automator;
import main.java.myFXtutorial.classes.Currency;
import main.java.myFXtutorial.classes.Generator;
import main.java.myFXtutorial.classes.World;
import main.java.myFXtutorial.utils.Constants;

import java.math.BigInteger;

public class GameManager {

    private World world;
    private Currency coins;
    private Generator g1, g2, g3, g4, g5, g6, g7, g8, g9, g10, g11;

    private double testTimer = 0.0;

    public GameManager() {
        world = new World();
    }

    public void initialize() {

        coins = new Currency.Builder(world)
                .name("Coins")
                .build();

        g1 = new Generator.Builder(world)
                .baseCost(10)
                .baseValue(1)
                .costMultiplier(1.18)
                .generate(coins)
                .valueMultiplier(1.15)
                .build();

        Automator a1 = new Automator.Builder(world)
                .automate(g1)
                .tickRate(1.0)
                .build();

        g1.levelUp(); // Set this generator's level to 1 to test if it works.
        a1.levelUp(); // Set this automator's level to 1 to test if it works.

    }

    public void update(double deltaTime) {
        world.update(deltaTime);
    }

    public void increment(int n) {
        coins.add(BigInteger.valueOf(n));
    }

    public String getCoins() {
        return coins.getAmountAsString();
    }
}
