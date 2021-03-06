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
package se.suka.baldr.jbencode;

import java.util.Collection;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

/**
 * The smallest abstract class that is extended by all other types Bencoded atom
 * Object.
 *
 * @author Graham Fairweather
 */
public interface Atom {

    /**
     * Test if an object reference is an instance of {@code Atom}.
     *
     * @param o the object reference to check is instance of {@code Atom}
     * @return {@code true} if the object is an instance of {@code Atom},
     * otherwise {@code false}
     */
    public static boolean isAtom(Object o) {
        return o instanceof Atom;
    }

    /**
     * Test if an object reference is an instance of {@code Atom}, throw a
     * {@link ClassCastException} if it is not, otherwise return the reference
     * object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @throws ClassCastException if {@code o} is not an instance of
     * {@code Atom}
     * @throws NullPointerException if the specified element is null
     * @return the object reference
     */
    public static <T> T requireAtom(T o) {
        return requireAtom(o, "");
    }

    /**
     * Test if an object reference is an instance of {@code Atom}, throw a
     * {@link ClassCastException} if it is not, otherwise return the reference
     * object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @param message detail message to be used in the event that a
     * {@code ClassCastException} is thrown
     * @throws ClassCastException if {@code o} is not an instance of
     * {@code Atom}
     * @throws NullPointerException if the specified element is null
     * @return the object reference
     */
    public static <T> T requireAtom(T o, String message) {
        if (!isAtom(o)) {
            throw new ClassCastException(message);
        }
        return o;
    }

    /**
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @return the object reference
     * @throws ClassCastException if {@code o} is not an instance of
     * {@code Atom}
     * @throws NullPointerException if the specified element is null
     */
    public static <T> T requireNonNullAtom(T o) {
        return requireAtom(requireNonNull(o));
    }

    /**
     *
     * @param <T> the type of the value being boxed
     * @param c the object reference to check
     * @return the object reference
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this list
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     */
    public static <T> Collection<T> requireAtomCollection(final Collection<T> c) {
        return c.stream()
                .map(atom -> requireNonNullAtom(atom))
                .collect(toList());
    }

    /**
     * Returns the length of the Bencoded string of this {@link Atom}. This
     * method is faster than performing an <code>encode().length()</code>.
     *
     * @return The length of the Becoded string
     */
    public int bLength();

    /**
     * Returns a deep copy of this {@link Atom}.
     *
     * @return a copy of this {@link Atom}
     */
    public Atom copy();

    /**
     * Returns the Bencoded string of this {@link Atom}.
     *
     * @return The Benoded string
     */
    public String encode();

    /**
     * Returns the Bencoded ASCII bytes of this {@link Atom}.
     *
     * @return The Benoded ASCII bytes
     */
    public byte[] encodeAsBytes();

}
