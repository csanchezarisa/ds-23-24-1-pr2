package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.adt.sequential.List;
import org.junit.Before;
import org.junit.Test;
import uoc.ds.pr.model.Ship;

import java.util.Objects;

import static org.junit.Assert.*;

public class UtilsTest {

    private List<Ship> ships;
    private Ship ship;

    @Before
    public void setUp() {
        ship = new Ship("id", "name", 0, 0, 0, 200, 5);
        ships = new LinkedList<>();
        ships.insertEnd(ship);
    }

    @Test
    public void testAnyMatch() {
        assertFalse(Utils.anyMatch(ships.values(), s -> s.getName().equals("invalid name")));
        assertTrue(Utils.anyMatch(ships.values(), s -> s.getName().equals("name")));
        assertTrue(Utils.anyMatch(ships.values(), s -> s.getUnLoadTimeInMinutes() > 3));
        assertFalse(Utils.anyMatch(new LinkedList<>().values(), Objects::nonNull));
    }

    @Test
    public void testFindUsingIterator() {
        var result = Utils.find(ships.values(), s -> s.getName().equals("name"));
        assertTrue(result.isPresent());
        assertEquals(ship, result.get());

        result = Utils.find(ships.values(), s -> s.getName().equals("invalid name"));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFindUsingTraversal() {
        var result = Utils.find(ships.positions(), s -> s.getName().equals("name"));
        assertTrue(result.isPresent());
        assertEquals(ship, result.get().getElem());

        result = Utils.find(ships.positions(), s -> s.getName().equals("invalid name"));
        assertTrue(result.isEmpty());
    }

    @Test
    public void testFilter() {
        ships.insertEnd(new Ship("newId", "newName", 0, 0, 0, 0, 0));

        var result = Utils.filter(ships.values(), s -> s.getUnLoadTimeInMinutes() > 0);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(ship, result.values().next());
    }

    @Test
    public void testCount() {
        ships.insertEnd(new Ship("newId", "newName", 0, 0, 0, 0, 0));

        var result = Utils.count(ships.values(), s -> s.getUnLoadTimeInMinutes() > 0);
        assertEquals(1, result);
    }

    @Test
    public void testToList() {
        var result = Utils.toList(ships);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(ship, result.values().next());
    }

    @Test
    public void testRemoveIf() {
        ships.insertEnd(new Ship("newId", "newName", 0, 0, 0, 0, 0));
        Utils.removeIf(ships, s -> s.getUnLoadTimeInMinutes() == 0);
        assertEquals(1, ships.size());
        assertEquals(ship, ships.values().next());
    }

    @Test
    public void testContains() {
        assertTrue(Utils.contains(ships, ship));
        assertFalse(Utils.contains(ships, new Ship("newId", "newName", 0, 0, 0, 0, 0)));
    }

    @Test
    public void testRemoveAll() {
        Ship newShip = new Ship("newId", "newName", 0, 0, 0, 0, 0);
        ships.insertEnd(newShip);

        List<Ship> shipsToRemove = new LinkedList<>();
        shipsToRemove.insertEnd(newShip);

        assertEquals(2, ships.size());

        Utils.removeAll(ships, shipsToRemove);
        assertEquals(1, ships.size());
        assertEquals(ship, ships.values().next());
    }
}