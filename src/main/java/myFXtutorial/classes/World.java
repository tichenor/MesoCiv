package main.java.myFXtutorial.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class World implements Serializable {

    private ArrayList<Generator> generators = new ArrayList<>();
    private ArrayList<Currency> currencies = new ArrayList<>();
    private ArrayList<Automator> automators = new ArrayList<>();
    private ArrayList<Modifier> modifiers = new ArrayList<>();

    private double speedMultiplier = 1.0;

    private boolean updateAutomators = true;

    private double elapsedTime = 0;

    public void update(double seconds) {
        seconds *= speedMultiplier;
        if (updateAutomators) {
            for (Automator a : automators) {
                a.update(seconds);
            }
        }
        elapsedTime += seconds;
    }

    void addGenerator(Generator g) {
        if (g != null && !generators.contains(g)) {
            generators.add(g);
        }
    }

    void removeGenerator(Generator g) {
        if (g != null) {
            generators.remove(g);
        }
    }

    void removeAllGenerators() {
        generators.clear();
    }

    void addCurrency(Currency c) {
        if (c != null && !currencies.contains(c)) {
            currencies.add(c);
        }
    }

    void removeCurrency(Currency c) {
        if (c != null) {
            currencies.remove(c);
        }
    }

    void removeAllCurrencies() {
        currencies.clear();
    }

    void addAutomator(Automator a) {
        if (a != null && !automators.contains(a)) {
            automators.add(a);
        }
    }

    void removeAutomator(Automator a) {
        if (a != null) {
            automators.remove(a);
        }
    }

    void enableAutomators() {
        updateAutomators = true;
    }

    void disableAutomators() {
        updateAutomators = false;
    }

    void addModifier(Modifier m) {
        if (m != null && !modifiers.contains(m)) {
            modifiers.add(m);
        }
    }

    void removeModifier(Modifier m) {
        if (m != null) {
            modifiers.remove(m);
        }
    }

    // getters & setters

    public int getGeneratorCount() {
        return generators.size();
    }

    public Currency getCurrencyAtIndex(int index) {
        return currencies.get(index);
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public List<Automator> getAutomators() {
        return automators;
    }

    public List<Modifier> getModifiers() {
        return modifiers;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(double factor) {
        if (factor > 0) {
            speedMultiplier = factor;
        }
    }

    public boolean isAutomationEnabled() {
        return updateAutomators;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

}
