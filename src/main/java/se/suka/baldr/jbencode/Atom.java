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

import static java.util.Objects.requireNonNull;

/**
 * The smallest abstract class that is extended by all other types Bencoded atom
 * Object.
 *
 * @author Graham Fairweather
 */
public abstract class Atom {

    /**
     *
     * @param o
     * @return
     */
    public static final boolean isAtom(Object o) {
        return o instanceof Atom;
    }

    /**
     *
     * @param o
     * @return
     */
    public static final Atom requireAtom(Object o) {
        if (!isAtom(requireNonNull(o))) {
            throw new IllegalArgumentException();
        }
        return (Atom) o;
    }

    /**
     *
     * @param o
     * @param message
     * @return
     */
    public static final Atom requireAtom(Object o, String message) {
        if (!isAtom(requireNonNull(o))) {
            throw new IllegalArgumentException(message);
        }
        return (Atom) o;
    }

    /**
     * Returns the length of the Bencoded string of this {@link Atom}. This
     * method is faster than performing an <code>encode().length()</code>.
     *
     * @return The length of the Becoded string
     */
    public abstract int bLength();

    /**
     * Returns a deep copy of this {@link Atom}.
     *
     * @param <T>
     * @return a copy of this {@link Atom}
     */
    public abstract <T> Atom copy();

    /**
     * Returns the Bencoded string of this {@link Atom}.
     *
     * @return The Benoded string
     */
    public abstract String encode();

}
