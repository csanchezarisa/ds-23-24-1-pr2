package uoc.ds.pr.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FiniteLinkedListTest {
    FiniteLinkedList<String> finiteLinkedList;

    @Before
    public void setUp() {
        finiteLinkedList = new FiniteLinkedList<>(5);
        finiteLinkedList.insertEnd("A");
        finiteLinkedList.insertEnd("B");
        finiteLinkedList.insertEnd("C");
        Assert.assertFalse(finiteLinkedList.isFull());
    }

    @Test
    public void insertEndTest() {
        Assert.assertEquals(5, finiteLinkedList.length());
        Assert.assertEquals(3, finiteLinkedList.size());
        finiteLinkedList.insertEnd("D");
        finiteLinkedList.insertEnd("E");
        Assert.assertEquals(5, finiteLinkedList.size());
        Assert.assertTrue(finiteLinkedList.isFull());
        Assert.assertEquals(5, finiteLinkedList.length());
        finiteLinkedList.insertEnd("F");
        finiteLinkedList.insertEnd("G");
        Assert.assertEquals(5, finiteLinkedList.size());
        Assert.assertTrue(finiteLinkedList.isFull());
        Assert.assertEquals(5, finiteLinkedList.length());
    }
}
