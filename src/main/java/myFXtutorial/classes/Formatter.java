package main.java.myFXtutorial.classes;

public class Formatter {

    /**
     * Format a currency to a string representation.
     */
    public static class CurrencyFormatter extends Formatter {

        private final Currency currency;

        private CurrencyFormatter(Builder builder, Currency currency) {
            super(builder);
            this.currency = currency;
        }

        @Override
        public String toString() {
            setRawString(currency.getValue().toString());
            return super.toString();
        }
    }

    /**
     * Format an purchasable item's cost to a string representation.
     */
    public static class CostFormatter extends Formatter {

        private final Purchasable purchasable;

        private CostFormatter(Builder builder, Purchasable purchasable) {
            super(builder);
            this.purchasable = purchasable;
        }

        @Override
        public String toString() {
            setRawString(purchasable.getCurrentCost().toString());
            return super.toString();
        }
    }

    protected final boolean groupDigits;
    protected final String thousandSeparator;
    protected final boolean showDecimals;
    protected final int decimals;
    protected final String decimalSeparator;
    protected final boolean cutAtHighest;
    protected final String[] abbreviations;

    protected Formatter(Builder builder) {
        groupDigits = builder.groupDigits;
        thousandSeparator = builder.thousandSeparator;
        showDecimals = builder.showDecimals;
        decimals = builder.decimals;
        decimalSeparator = builder.decimalSeparator;
        cutAtHighest = builder.cutAtHighest;
        abbreviations = builder.abbreviations;
    }

    public static class ForCost extends Builder {

        private Purchasable purchasable;

        public ForCost(Purchasable purchasable) {
            this.purchasable = purchasable;
        }

        public CostFormatter build() {
            return new CostFormatter(this, purchasable);
        }
    }

    public static class ForCurrency extends Builder {

        private Currency currency;

        public ForCurrency(Currency currency) {
            this.currency = currency;
        }

        public CurrencyFormatter build() {
            return new CurrencyFormatter(this, currency);
        }
    }

    public static abstract class Builder {

        private boolean groupDigits = true;
        private String thousandSeparator = ",";
        private boolean showDecimals = false;
        private int decimals = 2;
        private String decimalSeparator;
        private boolean cutAtHighest = true;
        private String[] abbreviations = null;


        public Builder showHighestThousand() {
            cutAtHighest = true;
            return this;
        }

        public Builder showFully() {
            cutAtHighest = false;
            return this;
        }

        public Builder groupDigits() {
            return groupDigits(",");
        }

        public Builder groupDigits(String separator) {
            groupDigits = true;
            thousandSeparator = separator;
            return this;
        }

        public Builder dontGroupDigits() {
            groupDigits = false;
            thousandSeparator = null;
            return this;
        }

        public Builder showDecimals(int count) {
            return showDecimals(count, ".");
        }

        public Builder showDecimals(String separator) {
            return showDecimals(2, separator);
        }

        public Builder showDecimals(int count, String separator) {
            showDecimals = true;
            decimals = count;
            decimalSeparator = separator;
            return this;
        }

        public Builder dontShowDecimals() {
            showDecimals = false;
            decimals = 0;
            decimalSeparator = null;
            return this;
        }

        public Builder useAbbreviations(String[] abbreviations) {
            this.abbreviations = abbreviations;
            return this;
        }

        public abstract Formatter build();

    }

    private String rawString = "";

    public void setRawString(String raw) {
        rawString = raw;
        if (rawString == null) {
            rawString = "";
        }
    }

    @Override
    public String toString() {
        String raw = rawString;

        if (cutAtHighest) {

            int length = raw.length();
            if (length < 4) {
                return raw;
            }
            int remainder = length % 3;
            remainder = remainder == 0 ? 3 : remainder;
            String top = raw.substring(0, remainder);

            if (showDecimals) {
                top += decimalSeparator;
                int decimals = Math.min(this.decimals, length - remainder);
                top += raw.substring(remainder, remainder + decimals);
            }

            if (abbreviations != null) {
                int tri = (raw.length() - 1) / 3;
                if (tri > 0 && tri <= abbreviations.length) {
                    top += abbreviations[tri - 1];
                }
            }
            return top;
        } else {
            if (groupDigits) {
                int len = raw.length() - 3;
                for (int i = len; i > 0; --i) {
                    if ((len - i) % 3 == 0) {
                        raw = raw.substring(0, i) + thousandSeparator + raw.substring(i);
                    }
                }
            }
            return raw;
        }
    }
}
