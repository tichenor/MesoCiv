package main.java.myFXtutorial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressManager {

    /**
     * Dictionary of properties, using their names as keys.
     */
    private final Map<String, Property> properties;

    /**
     * Dictionary of achievements, using their names as keys.
     */
    private final Map<String, Achievement> achievements;

    public ProgressManager() {
        properties = new HashMap<>();
        achievements = new HashMap<>();
    }

    /**
     * Unlock any achievements for which all of its properties are active, and return a list of any new unlocked
     * achievements.
     * @return A list of achievements that were unlocked by this method,
     */
    public List<Achievement> checkAllAchievements() {
        List<Achievement> newUnlocked = new ArrayList<>();
        for (Achievement a : achievements.values()) {
            if (!a.isUnlocked()) {
                int activeProperties = 0;
                for (String propertyName : a.properties) {
                    if (properties.get(propertyName).isActive())
                        activeProperties++;
                }
                if (activeProperties == a.properties.size()) {
                    a.unlock();
                    newUnlocked.add(a);
                }
            }
        }
        return newUnlocked;
    }

    public void defineProperty(String name, int initialValue, ActivationRule activationRule, int activationValue,
                               List<String> tags) {
        properties.put(name, new Property(name, initialValue, activationRule, activationValue, tags));
    }

    public void defineAchievement(String name, List<String> propertyList) {
        for (String propertyName : propertyList) {
            checkPropertyExists(propertyName);
        }
        achievements.put(name, new Achievement(name, propertyList));
    }

    public int getValueOf(String propertyName) {
        checkPropertyExists(propertyName);
        return properties.get(propertyName).value;
    }

    public void addValue(String propertyName, int value) {
        setValue(propertyName, getValueOf(propertyName) + value);
    }

    public void addValues(List<Property> propertyList, int value) {
        for (Property p : propertyList) {
            addValue(p.name, value);
        }
    }

    public void setValue(String propertyName, int value) {
        // Constrain the value so that a property records the highest/lowest value obtained. Otherwise
        // achievements might be missed by recording a value, then another lower/higer value before a check
        // whether the achievement should be unlocked was made.
        switch (properties.get(propertyName).activationRule) {
            case GREATER_THAN -> value = Math.max(value, properties.get(propertyName).value);
            case LESS_THAN -> value = Math.min(value, properties.get(propertyName).value);
        }
        properties.get(propertyName).value = value;
    }
    
    public void setValues(List<Property> propertyList, int value) {
        for (Property p : propertyList) {
            setValue(p.name, value);
        }
    }

    public void resetProperties(List<String> matchingTags) {
        for (Property p : properties.values()) {
            if (matchingTags.isEmpty() || hasTag(p, matchingTags))
                p.reset();
        }
    }

    public void resetAllProperties() {
        for (Property p : properties.values()) {
            p.reset();
        }
    }

    private void checkPropertyExists(String propertyName) {
        if (properties.get(propertyName) == null) {
            throw new IllegalArgumentException("Unknown achievement property \"" + propertyName + "\".");
        }
    }

    private boolean hasTag(Property p, List<String> tags) {
        if (p.propertyTags == null || p.propertyTags.isEmpty()) {
            return false;
        }
        for (String tag : tags) {
            if (p.propertyTags.contains(tag))
                return true;
        }
        return false;
    }

    public String dumpPropertiesToString() {
        StringBuilder result = new StringBuilder();
        for (String name : properties.keySet()) {
            result.append(name).append(" = ").append(properties.get(name).value).append(", ");
        }
        return result.substring(0, result.length() - 2);
    }

    /**
     * An achievement is a (meta-)goal or challenge that is unlocked based on one or more properties or
     * conditions that must be satisfied.
     */
    public static class Achievement {

        private final String name;
        private final List<String> properties;
        private boolean unlocked;

        Achievement(String name, List<String> properties) {
            this.name = name;
            this.properties = properties;
            this.unlocked = false;
        }

        public String getName() {
            return name;
        }

        public boolean isUnlocked() {
            return unlocked;
        }

        void unlock() {
            unlocked = true;
        }
    }

    /**
     * Instances of this class track some in-game metric and are active or not depending on the value of that metric.
     * A property is more or less just a counter with some special attributes such as initial value and value
     * constraints.
     */
    private static class Property {

        private final String name;
        private int value;
        private final ActivationRule activationRule;
        private final int activationValue;
        private final int initialValue;
        private final List<String> propertyTags;

        Property(String name, int initialValue, ActivationRule activationRule, int activationValue, List<String> tags) {
            this.name = name;
            this.initialValue = initialValue;
            this.activationRule = activationRule;
            this.activationValue = activationValue;
            this.propertyTags = tags;
            reset();
        }

        void reset() {
            value = initialValue;
        }

        boolean isActive() {
            boolean result = false;
            switch (activationRule) {
                case GREATER_THAN: result = value > activationValue;
                case LESS_THAN: result = value < activationValue;
                case EQUAL_TO: result = value == activationValue;
            }
            return result;
        }
    }

    /**
     * Rule for when a property is active.
     */
    public enum ActivationRule {
        GREATER_THAN,
        LESS_THAN,
        EQUAL_TO
    }

}
