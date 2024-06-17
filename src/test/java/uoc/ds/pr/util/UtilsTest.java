package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import junit.framework.TestCase;
import uoc.ds.pr.model.Client;
import uoc.ds.pr.model.Ship;

import java.util.Objects;

public class UtilsTest extends TestCase {

    public void testAnyMatch() {
        List<Ship> ships = new LinkedList<>();
        ships.insertEnd(new Ship("id", "name", 0, 0, 0, 200, 5));

        assertFalse(Utils.anyMatch(ships.values(), s -> s.getName().equals("invalid name")));
        assertTrue(Utils.anyMatch(ships.values(), s -> s.getName().equals("name")));
        assertTrue(Utils.anyMatch(ships.values(), s -> s.getUnLoadTimeInMinutes() > 3));
        assertFalse(Utils.anyMatch(new LinkedList<>().values(), Objects::nonNull));
    }
}