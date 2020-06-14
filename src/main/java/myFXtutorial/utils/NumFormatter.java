package main.java.myFXtutorial.utils;

import java.math.BigInteger;

public class NumFormatter {

    private final boolean groupDigits;
    private final String thousandSeparator;
    private final boolean showDecimals;
    private final int decimals;
    private final String decimalSeparator;
    private final boolean cutAtHighest;
    private final String[] abbreviations;

    public String format(BigInteger num) {
        String rawStr = num.toString();

        if (cutAtHighest) {

            int length = rawStr.length();
            if (length < 4) {
                return rawStr;
            }
            int remainder = length % 3;
            remainder = remainder == 0 ? 3 : remainder;
            String top = rawStr.substring(0, remainder);

            if (showDecimals) {
                top += decimalSeparator;
                int decimals = Math.min(this.decimals, length - remainder);
                top += rawStr.substring(remainder, remainder + decimals);
            }

            if (abbreviations != null) {
                int tri = (rawStr.length() - 1) / 3;
                if (tri > 0 && tri <= abbreviations.length) {
                    top += abbreviations[tri - 1];
                }
            }
            return top;
        } else {
            if (groupDigits) {
                int len = rawStr.length() - 3;
                for (int i = len; i > 0; --i) {
                    if ((len - i) % 3 == 0) {
                        rawStr = rawStr.substring(0, i) + thousandSeparator + rawStr.substring(i);
                    }
                }
            }
            return rawStr;
        }
    }

    public NumFormatter(Builder builder) {
        groupDigits = builder.groupDigits;
        thousandSeparator = builder.thousandSeparator;
        showDecimals = builder.showDecimals;
        decimals = builder.decimals;
        decimalSeparator = builder.decimalSeparator;
        cutAtHighest = builder.cutAtHighest;
        abbreviations = builder.abbreviations;
    }

    public static class Builder {

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

        public NumFormatter build() {
            return new NumFormatter(this);
        }

    }

}
