package uoc.ds.pr.util;

import edu.uoc.ds.adt.helpers.Position;
import edu.uoc.ds.adt.sequential.FiniteContainer;
import edu.uoc.ds.adt.sequential.LinkedList;

public class FiniteLinkedList<E> extends LinkedList<E> implements FiniteContainer<E> {

    int max;

    public FiniteLinkedList(int n) {
        this.max = n;
    }

    @Override
    public boolean isFull() {
        return super.size()>= max;
    }

    public int length() {
        return max;
    }

    public Position<E> insertEnd(E elem) {
        Position<E> position = null;
        if (!isFull()) {
            position = super.insertEnd(elem);
        }
        return position;
    }
}
