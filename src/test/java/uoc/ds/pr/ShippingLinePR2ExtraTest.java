package uoc.ds.pr;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.exceptions.NoRouteException;

import static org.junit.Assert.assertThrows;

public class ShippingLinePR2ExtraTest extends ShippingLinePR2PlusTest {

    @Before
    public void setUp() throws Exception {
        super.setUp();
        super.theShippingLine = FactoryShippingLine.getShippingLine();
        Assert.assertEquals(23, this.theShippingLine.numPorts());
    }

    @Test
    public void givenNoVoyagesWhenGetMostTraveledRouteThenException() {
        super.theShippingLine = new ShippingLinePR2Impl();
        assertThrows(NoRouteException.class, () -> theShippingLine.getMostTraveledRoute());
    }

    @Test
    public void givenNoPossibleRouteWhenGetBestKmsRouteThenException() {
        assertThrows(NoRouteException.class, () -> theShippingLine.getBestKmsRoute("PAL", "ROME"));
    }

    @Test
    public void givenNoPossibleRouteWhenGetBestPortsRouteThenException() {
        assertThrows(NoRouteException.class, () -> theShippingLine.getBestPortsRoute("PAL", "ROME"));
    }
}
