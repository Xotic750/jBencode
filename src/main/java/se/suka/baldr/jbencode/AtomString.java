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

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.Utilities.asciiBytesToString;
import static se.suka.baldr.jbencode.Utilities.stringToAsciiBytes;

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
     * Test if an object reference is an instance of {@code AtomString}.
     *
     * @param o the object reference to check is instance of {@code AtomString}
     * @return {@code true} if the object is an instance of {@code AtomString},
     * otherwise {@code false}
     */
    public static boolean isAtomString(Object o) {
        return o instanceof AtomString;
    }

    /**
     * Test if an object reference is an instance of {@code AtomString}, throw a
     * {@link ClassCastException} if it is not, otherwise return the reference
     * object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @throws ClassCastException if {@code obj} is not an instance of
     * {@code AtomString}
     * @return the object reference
     */
    public static final <T> T requireAtomString(T o) {
        return requireAtom(o, "");
    }

    /**
     * Test if an object reference is an instance of {@code AtomString}, throw a
     * {@link ClassCastException} if it is not, otherwise return the reference
     * object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @param message detail message to be used in the event that a
     * {@code ClassCastException} is thrown
     * @throws ClassCastException if {@code obj} is not an instance of
     * {@code AtomString}
     * @return the object reference
     */
    public static final <T> T requireAtomString(T o, String message) {
        if (!isAtomString(o)) {
            throw new ClassCastException(message);
        }
        return o;
    }

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
     * Constructs a new {@code AtomString} by decoding the specified array of
     * bytes using the specified {@linkplain StandardCharsets#US_ASCII}. The
     * length of the new {@code AtomString} is a function of the charset, and
     * hence may not be equal to the length of the byte array.
     *
     * <p>
     * The behavior of this constructor when the given bytes are not valid in
     * the given charset is unspecified.
     *
     * @param value The bytes to be decoded into characters
     */
    public AtomString(final byte[] value) {
        this.value = asciiBytesToString(value);
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
     * Returns the Bencoded ASCII bytes of this {@link Atom}.
     *
     * @return The Benoded ASCII bytes
     */
    @Override
    public byte[] encodeAsBytes() {
        return stringToAsciiBytes(encode());
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
     * Encodes this {@code AtomString} into a sequence of bytes using the given
     * {@linkplain StandardCharsets#US_ASCII}, storing the result into a new
     * byte array.
     *
     * <p>
     * This method always replaces malformed-input and unmappable-character
     * sequences with this charset's default replacement byte array.
     *
     *
     * @return The resultant byte array
     */
    public byte[] getBytes() {
        return stringToAsciiBytes(value);
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
