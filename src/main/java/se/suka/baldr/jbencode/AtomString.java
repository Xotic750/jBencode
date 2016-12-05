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

import java.util.Objects;
import static java.util.Objects.requireNonNull;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * A byte string (a sequence of bytes, not necessarily characters) is encoded as
 * &lt;length&gt;:&lt;contents&gt;. The length is encoded in base 10, like
 * integers, but must be non-negative (zero is allowed); the contents are just
 * the bytes that make up the string. The string "spam" would be encoded as
 * 4:spam. The specification does not deal with encoding of characters outside
 * the ASCII set; to mitigate this, some BitTorrent applications explicitly
 * communicate the encoding (most commonly UTF-8) in various non-standard ways.
 * This is identical to how
 * <a href="https://en.wikipedia.org/wiki/Netstring">netstrings</a> work, except
 * that netstrings additionally append a comma suffix after the byte sequence.
 *
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomString extends Atom implements Comparable<AtomString> {

    private static final long serialVersionUID = 1252496632535400969L;

    private static final Logger LOGGER = getLogger(AtomString.class);

    /**
     * Backing {@link String}
     */
    private final String value;

    /**
     * Initialises a newly created {@code AtomString} object so that it
     * represents an empty character sequence.
     */
    public AtomString() {
        this("");
        LOGGER.info("AtomInteger constructed without an argument is assumed an empty string");
    }

    /**
     * Initialises a newly created {@code AtomString} object so that it
     * represents the same sequence of characters as the argument
     * {@code AtomString}; in other words, the newly created {@code AtomString}
     * is a copy of the argument {@code AtomString}.
     *
     * @param anotherAtomstring A {@code AtomString}
     * @throws NullPointerException If anotherAtomstring is {@code null}
     */
    public AtomString(final AtomString anotherAtomstring) {
        this(requireNonNull(anotherAtomstring).toString());
    }

    /**
     * Initialises a newly created {@code AtomString} object so that it
     * represents the same sequence of characters as the argument; in other
     * words, the newly created {@code AtomString} is a copy of the argument
     * string.
     *
     * @param value A {@code String}
     */
    public AtomString(final String value) {
        this.value = value;
    }

    /**
     * Returns the length of the Bencoded string of this {@link Atom}. This
     * method is faster than performing an <code>encode().length()</code>.
     *
     * @return The length of the Becoded string
     */
    @Override
    public int bLength() {
        final int len = value.length();
        return Integer.toString(len).length() + 1 + len;
    }

    /**
     * Returns the Bencoded string of this {@link Atom}.
     *
     * @return The Benoded string
     */
    @Override
    public String encode() {
        return value.length() + ":" + value;
    }

    /**
     * Returns a hash code value for the object. This method is supported for
     * the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during an
     * execution of a Java application, the {@code hashCode} method must
     * consistently return the same integer, provided no information used in
     * {@code equals} comparisons on the object is modified. This integer need
     * not remain consistent from one execution of an application to another
     * execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     * method, then calling the {@code hashCode} method on each of the two
     * objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal according
     * to the {@link java.lang.Object#equals(java.lang.Object)} method, then
     * calling the {@code hashCode} method on each of the two objects must
     * produce distinct integer results. However, the programmer should be aware
     * that producing distinct integer results for unequal objects may improve
     * the performance of hash tables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by class
     * {@code Object} does return distinct integers for distinct objects. (This
     * is typically implemented by converting the internal address of the object
     * into an integer, but this implementation technique is not required by the
     * Java&trade; programming language.)
     *
     * @return a hash code value for this object.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.System#identityHashCode
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.value);
        return hash;
    }

    /**
     * Compares this {@code Atomstring} to the specified object. The result is
     * {@code true} if and only if the argument is not {@code null} and is a
     * {@code Atomstring} object that represents the same sequence of characters
     * as this object.
     *
     * @param obj The object to compare this {@code Atomstring} against
     *
     * @return {@code true} if the given object represents a {@code Atomstring}
     * equivalent to this string, {@code false} otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AtomString) {
            return value.equals(obj.toString());
        }
        return false;
    }

    /**
     * This object's value is itself returned.
     *
     * @return the string itself.
     */
    @Override
    public String toString() {
        return value;
    }

    /**
     * Compares two {@code AtomString} objects lexicographically.
     *
     * @param anotherAtomString the {@code AtomString} to be compared.
     * @throws NullPointerException if the specified {@code anotherAtomString}
     * is {@code null}
     * @return the value {@code 0} if the argument {@code AtomString} is equal
     * to this {@code AtomString}; a value less than {@code 0} if this
     * {@code AtomString} is lexicographically less than the {@code AtomString}
     * argument; and a value greater than {@code 0} if this {@code AtomString}
     * is lexicographically greater than the {@code AtomString} argument.
     */
    @Override
    public int compareTo(final AtomString anotherAtomString) {
        return value.compareTo(requireNonNull(anotherAtomString).toString());
    }

    /**
     * Returns a deep copy of this {@link Atom}.
     *
     * @return a copy of this {@link Atom}
     */
    @Override
    public AtomString copy() {
        return new AtomString(value);
    }

}
