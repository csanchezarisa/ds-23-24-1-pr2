package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Test;
import uoc.ds.pr.ShippingLinePR2;

public class LoyaltyHelperTest {
    @Test
    public void loyaltyTest() {
        Assert.assertEquals(ShippingLinePR2.LoyaltyLevel.DIAMOND, LoyaltyLevel.getLevel(40));
        Assert.assertEquals(ShippingLinePR2.LoyaltyLevel.GOLD, LoyaltyLevel.getLevel(12));
        Assert.assertEquals(ShippingLinePR2.LoyaltyLevel.SILVER, LoyaltyLevel.getLevel(7));
        Assert.assertEquals(ShippingLinePR2.LoyaltyLevel.BRONZE, LoyaltyLevel.getLevel(3));
    }

}
