package dev.berlitz.carname.shared;

import java.text.DecimalFormat;

public class PercentageUtil {
    public static String formatPercent(double done, int digits) {
        DecimalFormat percentFormat = new DecimalFormat("0.0%");
        percentFormat.setDecimalSeparatorAlwaysShown(false);
        percentFormat.setMinimumFractionDigits(digits);
        percentFormat.setMaximumFractionDigits(digits);
        return percentFormat.format(done);
    }
}
