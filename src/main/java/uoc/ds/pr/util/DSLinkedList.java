package uoc.ds.pr.util;

import edu.uoc.ds.adt.sequential.LinkedList;
import edu.uoc.ds.traversal.Iterator;
import uoc.ds.pr.model.Client;

import java.util.Comparator;

public class DSLinkedList<E> extends LinkedList<E> {
    Comparator<E> comparator;

    public DSLinkedList(Comparator<E> comparator) {
        this.comparator = comparator;
    }

    public E get(E pElem) {
        Iterator<E> it = super.values();
        boolean found = false;
        E elem = null;
        while (!found && it.hasNext()) {
            elem = it.next();
            found = comparator.compare(elem, pElem) == 0;
        }
        return (found?elem: null);
    }
}
