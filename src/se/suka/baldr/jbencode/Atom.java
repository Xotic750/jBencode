package se.suka.baldr.jbencode;

/*
 * The MIT License
 *
 * Copyright 2016 Graham Fairweather.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
import java.util.Objects;

/**
 * The smallest abstract class that is extended by all other types Bencoded atom
 * Object.
 *
 * The object uses Generics to allow a type or method to operate on objects of
 * various types while providing compile-time type safety.
 *
 * @author Graham Fairweather
 * @param <T> Generic
 * @see <a href="https://en.wikipedia.org/wiki/Generics_in_Java">Generics</a>
 */
public abstract class Atom<T> {

    static boolean isAtom(Object o) {
        return o instanceof Atom;
    }

    static int rangeCheck(int index, int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index(" + index + ") < 0 || >= size(" + size + ")");
        }
        return index;
    }

    static Object stringCheck(Object o) {
        if (o instanceof String) {
            return o;
        }
        throw new ClassCastException("object(" + o + ") is not a sub class of String");
    }

    static Object atomCheck(Object o) {
        if (isAtom(o)) {
            return o;
        }
        throw new ClassCastException("object(" + o + ") is not a sub class of Atom");
    }

    static void subListRangeCheck(int fromIndex, int toIndex, int size) {
        if (fromIndex < 0) {
            throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
        }
        if (toIndex > size) {
            throw new IndexOutOfBoundsException("toIndex = " + toIndex);
        }
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
    }

    private final T value;

    Atom(T obj) {
        Objects.requireNonNull(obj);
        value = obj;
    }

    /**
     *
     * @return
     */
    public abstract int bLength();

    /**
     *
     * @return
     */
    public abstract String encode();

    /**
     * Returns a string representation of the object. In general, the
     * {@code toString} method returns a string that "textually represents" this
     * object. The result should be a concise but informative representation
     * that is easy for a person to read. It is recommended that all subclasses
     * override this method.
     * <p>
     * The {@code toString} method for class {@code Object} returns a string
     * consisting of the name of the class of which the object is an instance,
     * the at-sign character `{@code @}', and the unsigned hexadecimal
     * representation of the hash code of the object. In other words, this
     * method returns a string equal to the value of:
     * <blockquote>
     * <pre>
     * getClass().getName() + '@' + Integer.toHexString(hashCode())
     * </pre></blockquote>
     *
     * @return a string representation of the object.
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Returns the hash code value for this list. The hash code of a list is
     * defined to be the result of the following calculation:
     * <pre>{@code
     *     int hashCode = 1;
     *     for (E e : list)
     *         hashCode = 31*hashCode + (e==null ? 0 : e.hashCode());
     * }</pre> This ensures that <tt>list1.equals(list2)</tt> implies that
     * <tt>list1.hashCode()==list2.hashCode()</tt> for any two lists,
     * <tt>list1</tt> and <tt>list2</tt>, as required by the general contract of
     * {@link Object#hashCode}.
     *
     * @return the hash code value for this list
     * @see Object#equals(Object)
     * @see #equals(Object)
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    /**
     * Compares the specified object with this list for equality. Returns
     * <tt>true</tt> if and only if the specified object is also a list, both
     * lists have the same size, and all corresponding pairs of elements in the
     * two lists are <i>equal</i>. (Two elements <tt>e1</tt> and
     * <tt>e2</tt> are <i>equal</i> if <tt>(e1==null ? e2==null :
     * e1.equals(e2))</tt>.) In other words, two lists are defined to be equal
     * if they contain the same elements in the same order. This definition
     * ensures that the equals method works properly across different
     * implementations of the <tt>List</tt> interface.
     *
     * @param obj the object to be compared for equality with this list
     * @return <tt>true</tt> if the specified object is equal to this list
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Atom<?> other = (Atom<?>) obj;
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }

    T getValue() {
        return value;
    }

}
