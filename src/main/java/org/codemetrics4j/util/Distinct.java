package org.codemetrics4j.util;

/**
 * A lot of the JavaParser node types have their own equals and hashCode methods which use the EqualsVisitor to determine if they are equal,
 * but often they only include certain aspects of the element in the equality consideration.  For example, if you have two distinct method calls
 * to the same method they will both be instances of a MethodCallExpr, but they will have the same scope, the same name, the same arguments, and
 * the same type arguments and thus be considered equal and having the same hashCode.  This is a problem when using the elements as keys or values
 * in hashes or graphs or other collection types because the distinctiveness of the elements is lost.
 *
 * This class essentially un-equals two different nodes that would otherwise be considered equal according to the EqualsVisitor.  Equals now requires
 * the objects actually be identical instances, and hashCode uses System.identityHashCode
 *
 * @param <T>
 */
public class Distinct<T> {
    private final T wrapped;

    private Distinct(T wrapped) {
        this.wrapped = wrapped;
    }

    public static <T> Distinct<T> of(T toWrap) {
        return new Distinct<>(toWrap);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Distinct)) return false;
        else {
            Distinct other = (Distinct) o;
            return other.wrapped == this.wrapped;
        }
    }

    @Override
    public int hashCode() {
        return System.identityHashCode(wrapped);
    }

    public T get() {
        return wrapped;
    }
}
