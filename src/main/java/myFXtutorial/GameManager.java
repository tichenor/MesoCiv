package main.java.myFXtutorial;

import main.java.myFXtutorial.classes.*;
import main.java.myFXtutorial.classes.Currency;
import main.java.myFXtutorial.utils.Assets;

import java.math.BigInteger;
import java.util.*;

public class GameManager {

    private final World world;

    private Currency coins;

    private int clickValue = 1;

    private final ProgressManager progressManager;

    private Stack<ProgressManager.Achievement> achievementStack;

    // ALL AUTOMATORS TICK ONCE A SECOND; THIS GAME DOESN'T USE AUTOMATION AS A MECHANIC.

    /**
     * Dictionary of the generators and the possible modifiers (upgrades) for them.
     */
    private final Map<Generator, List<Modifier>> generatorUpgrades = new HashMap<>();

    /**
     * Dictionary of global modifiers (global upgrades) and the name of the achievement that unlocks them.
     * The progress manager handles achievements and properties internally.
     */
    private final Map<Modifier, String> globalUpgrades = new HashMap<>();


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

            a.levelUp(); // Level it up to 1 so it has effect (starts at 0).
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
                mod.setType(upgradeData.getType());
                mod.setBaseCost(upgradeData.getCost());
                generatorUpgrades.get(target).add(mod);
            }
        }

        // Initialize global/other upgrades
        for (Assets.GlobalUpgradeData globalUpgradeData : Assets.globalUpgrades) {

            // Generate the global modifier
            Modifier mod = new Modifier.Builder()
                    .modify(world)
                    .multiplier(globalUpgradeData.getMultiplier())
                    .build();
            mod.setName(globalUpgradeData.getName());
            mod.setDescription(globalUpgradeData.getDescription());
            mod.setType(globalUpgradeData.getType());
            mod.setBaseCost(globalUpgradeData.getCost());

            // Create the property and achievement corresponding to the unlock requirement for the global upgrade

            // Tag name is of the form "totalBuildings", "totalClicks", etc. Property will have only this tag.
            String propertyTag = globalUpgradeData.getUnlockRequirement();
            // Property name is <tag><integer unlock value>, e.g. "totalBuildings50".
            String propertyName = globalUpgradeData.getUnlockRequirement() + globalUpgradeData.getUnlockValue();
            progressManager.defineProperty(propertyName,
                    0,
                    ProgressManager.ActivationRule.GREATER_THAN,
                    globalUpgradeData.getUnlockValue(),
                    Collections.singletonList(propertyTag));
            String achievementName = mod.getName() + " unlock";
            // The achievement consists of just the one property corresponding to the unlock requirement of the upgrade
            progressManager.defineAchievement(achievementName, Collections.singletonList(propertyName));

            globalUpgrades.put(mod, achievementName);

        }

        // Initialize achievements
        for (Assets.AchievementData achievementData : Assets.achievements.values()) {
            // Currently only single-property achievements
            String propertyName = achievementData.getUnlockRequirement() + achievementData.getUnlockValue();
            String propertyTag = achievementData.getUnlockRequirement();
            progressManager.defineProperty(propertyName,
                    0,
                    ProgressManager.ActivationRule.GREATER_THAN,
                    achievementData.getUnlockValue(),
                    Collections.singletonList(propertyTag));
            String achievementName = achievementData.getName();
            progressManager.defineAchievement(achievementName, Collections.singletonList(propertyName));
        }
    }

    double seconds = 0;

    /**
     * Updates and progresses the world by a time step. Checks if any new achievements have been unlocked.
     * @param deltaTime Time in milliseconds.
     */
    public void update(double deltaTime) {
        seconds += deltaTime;
        while (seconds > 1) {
            seconds -= 1;
            progressManager.setValueTagged(Collections.singletonList("perSecond"), getPerSecond().intValue());
        }
        world.update(deltaTime);
        List<ProgressManager.Achievement> unlockedAchievements = progressManager.checkAllAchievements();
        for (ProgressManager.Achievement a : unlockedAchievements) {
            achievementStack.push(a);
            onUnlock(a);
        }
    }

    private void onUnlock(ProgressManager.Achievement a) {
        Assets.AchievementData ad = Assets.achievements.get(a.getName());
        switch (ad.getType()) {
            case "bonusClick" -> clickValue += (int) ad.getBonusValue();
            case "bonusGlobalMultiplier" -> {
                Modifier m = new Modifier.Builder()
                        .modify(world)
                        .multiplier(ad.getBonusValue())
                        .build();
                m.setName(ad.getName() + " bonus");
                m.enable();
            }
            default -> {}
        }
    }

    public void onClick() {
        coins.add(BigInteger.valueOf(clickValue));
        progressManager.addValueTagged(Collections.singletonList("clicking"), 1);
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

    /**
     * Return a list of all global upgrades whose 'unlocking achievement' has been unlocked.
     */
    public List<Modifier> getAvailableGlobalUpgrades() {
        List<Modifier> result = new ArrayList<>();
        for (Map.Entry<Modifier, String> entry : globalUpgrades.entrySet()) {
            if (progressManager.isUnlocked(entry.getValue())) {
                result.add(entry.getKey());
            }
        }
        return result;
    }

    public String getNameOf(int tier) {
        return world.getGenerators().get(tier - 1).getName();
    }

    public List<ProgressManager.Achievement> getUnlockedAchievements() {
        List<ProgressManager.Achievement> result = new ArrayList<>();
        while (!achievementStack.empty()) {
            result.add(achievementStack.pop());
        }
        return result;
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
                    progressManager.addValueTagged(Collections.singletonList("totalBuildings"), 1);
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
