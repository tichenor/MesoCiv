package main.java.myFXtutorial.classes;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Base class for any type of currency.
 */
public class Currency implements Serializable {

    private String name;

    /*
    The total value of this currency.
     */
    private BigInteger value = BigInteger.ZERO;

    private final World world;

    public static class Builder {

        private final World world;
        private String name = "coins";

        public Builder(World world) {
            this.world = world;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Currency build() {
            Currency c = new Currency(world, name);
            world.addCurrency(c);
            return c;
        }
    }

    private Currency(World world, String name) {
        this.world = world;
        this.name = name;
    }

    public void add(BigInteger number) {
        value = value.add(number);
    }

    public void sub(BigInteger number) {
        value = value.subtract(number);
    }

    public void multiply(double multiplier) {
        BigDecimal temp = new BigDecimal(value);
        temp = temp.multiply(new BigDecimal(multiplier));
        value = temp.toBigInteger();
    }

    void setValue(BigInteger newValue) {
        value = newValue;
    }

    public String getName() {
        return name;
    }

    public String getAmountAsString() {
        return value.toString();
    }

    @Override
    public String toString() {
        return name + ": " + getAmountAsString();
    }

    BigInteger getValue() {
        return value;
    }
}
