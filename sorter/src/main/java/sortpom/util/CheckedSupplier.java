package sortpom.util;

/** Added supplier that can return a checked exception */
@FunctionalInterface
interface CheckedSupplier<T, E extends Exception> {
  T get() throws E;
}
