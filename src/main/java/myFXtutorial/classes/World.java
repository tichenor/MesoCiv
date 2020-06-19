package main.java.myFXtutorial.classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class World implements Serializable {

    private ArrayList<Generator> generators = new ArrayList<>();
    private ArrayList<Currency> currencies = new ArrayList<>();
    private ArrayList<Automator> automators = new ArrayList<>();
    private ArrayList<Modifier> modifiers = new ArrayList<>();

    /**
     * Affects the production of all generators in this world by multiplication with this factor.
     * The value is queried by a generator each time it is calculating its processed amount. It may be changed
     * by a WorldModifier.
     */
    private double globalMultiplier = 1.0;

    private boolean updateAutomators = true;

    private double elapsedTime = 0;

    public void update(double seconds) {
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

    public double getGlobalMultiplier() {
        return globalMultiplier;
    }

    public void setGlobalMultiplier(double factor) {
        if (factor > 0) {
            globalMultiplier = factor;
        }
    }

    public boolean isAutomationEnabled() {
        return updateAutomators;
    }

    public double getElapsedTime() {
        return elapsedTime;
    }

    public List<Generator> getGenerators() {
        return generators;
    }

}
