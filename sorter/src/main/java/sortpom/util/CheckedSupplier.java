package sortpom.util;

/**
 * Added supplier that can return a checked exception
 */
@FunctionalInterface
public interface CheckedSupplier<T, E extends Exception> {
    public T get() throws E;
}