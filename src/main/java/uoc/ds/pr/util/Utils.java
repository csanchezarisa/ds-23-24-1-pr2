package uoc.ds.pr.util;

import edu.uoc.ds.traversal.Iterator;

import java.util.Optional;
import java.util.function.Predicate;

public final class Utils {

    private Utils() {
        throw new UnsupportedOperationException("This is a utility class and must not be initialized");
    }

    /**
     * Loops an iterator evaluating the predicate. If there is any match with it, true is returned. Otherwise, false.
     * @param it iterator to loop
     * @param predicate predicate to evaluate each element in the iterator
     * @return true is there is any match with the predicate, else false
     * @param <E> class type of the elements contained in the iterator
     */
    public static <E> boolean anyMatch(Iterator<E> it, Predicate<E> predicate) {
        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                return true;
            }
        }
        return false;
    }

    public static <E> Optional<E> find(Iterator<E> it, Predicate<E> predicate) {
        while (it.hasNext()) {
            E e = it.next();
            if (predicate.test(e)) {
                return Optional.of(e);
            }
        }
        return Optional.empty();
    }
}
