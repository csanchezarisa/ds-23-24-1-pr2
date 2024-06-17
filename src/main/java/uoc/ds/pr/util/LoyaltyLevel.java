package uoc.ds.pr.util;

import uoc.ds.pr.ShippingLinePR2;

public final class LoyaltyLevel {

    private static final int DIAMOND_THRESHOLD = 15;
    private static final int GOLD_THRESHOLD = 10;
    private static final int SILVER_THRESHOLD = 5;

    private LoyaltyLevel() {
        throw new UnsupportedOperationException("This is a utility class, it must not be initialized");
    }

    public static ShippingLinePR2.LoyaltyLevel getLevel(int points) {
        if (points >= DIAMOND_THRESHOLD) {
            return ShippingLinePR2.LoyaltyLevel.DIAMOND;
        }
        if (points >= GOLD_THRESHOLD) {
            return ShippingLinePR2.LoyaltyLevel.GOLD;
        }
        if (points >= SILVER_THRESHOLD) {
            return ShippingLinePR2.LoyaltyLevel.SILVER;
        }
        return ShippingLinePR2.LoyaltyLevel.BRONZE;
    }
}
