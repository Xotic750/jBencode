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

import static java.lang.Double.compare;
import static java.util.Objects.requireNonNull;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.Utilities.stringToAsciiBytes;

/**
 * An integer is encoded as i&lt;integer encoded in base ten ASCII&gt;e. Leading
 * zeros are not allowed (although the number zero is still represented as "0").
 * Negative values are encoded by prefixing the number with a minus sign. The
 * number 42 would thus be encoded as i42e, 0 as i0e, and -42 as i-42e. Negative
 * zero is not permitted.
 *
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomInteger extends Atom implements Comparable<AtomInteger> {

    private static final long serialVersionUID = 8464216577156678961L;

    private static final Logger LOGGER = getLogger(AtomInteger.class);

    /**
     * Test if an object reference is an instance of {@code AtomInteger}.
     *
     * @param o the object reference to check is instance of {@code AtomInteger}
     * @return {@code true} if the object is an instance of {@code AtomInteger},
     * otherwise {@code false}
     */
    public static boolean isAtomInteger(Object o) {
        return o instanceof AtomInteger;
    }

    /**
     * Test if an object reference is an instance of {@code AtomInteger}, throw
     * a {@link ClassCastException} if it is not, otherwise return the reference
     * object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @throws ClassCastException if {@code obj} is not an instance of
     * {@code AtomInteger}
     * @return the object reference
     */
    public static final <T> T requireAtomInteger(T o) {
        return requireAtom(o, "");
    }

    /**
     * Test if an object reference is an instance of {@code AtomInteger}, throw
     * a {@link ClassCastException} if it is not, otherwise return the reference
     * object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @param message detail message to be used in the event that a
     * {@code ClassCastException} is thrown
     * @throws ClassCastException if {@code obj} is not an instance of
     * {@code AtomInteger}
     * @return the object reference
     */
    public static final <T> T requireAtomInteger(T o, String message) {
        if (!isAtomInteger(o)) {
            throw new ClassCastException(message);
        }
        return o;
    }

    /**
     * Backing {@link long}
     */
    private final long value;

    /**
     * Constructs a newly allocated {@code AtomInteger} object that represents
     * the {@code int} value of 0.
     */
    public AtomInteger() {
        this(0);
        LOGGER.info("AtomInteger constructed without an argument is assumed zero");
    }

    /**
     * Constructs a newly allocated {@code AtomInteger} object that represents
     * the specified {@code AtomInteger} value.
     *
     * @param anotherAtomInteger the value to be represented by the
     * {@code AtomInteger} object.
     * @throws NullPointerException If anotherAtomInteger is {@code null}
     */
    public AtomInteger(final AtomInteger anotherAtomInteger) {
        this(requireNonNull(anotherAtomInteger).longValue());
    }

    /**
     * Constructs a newly allocated {@code AtomInteger} object that represents
     * the specified {@code long} value.
     *
     * @param value the value to be represented by the {@code AtomInteger}
     * object.
     */
    public AtomInteger(final long value) {
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
        return toString().length() + 2;
    }

    /**
     * Returns the Bencoded string of this {@link Atom}.
     *
     * @return The Benoded string
     */
    @Override
    public String encode() {
        return "i" + value + "e";
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
     * Returns the value of this {@code AtomInteger} as a {@code byte} after a
     * narrowing primitive conversion.
     *
     * @return the byte value
     */
    public byte byteValue() {
        return (byte) value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code short} after a
     * narrowing primitive conversion.
     *
     * @return the short value
     */
    public short shortValue() {
        return (short) value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as an {@code int} after a
     * narrowing primitive conversion.
     *
     * @return the int value
     */
    public int intValue() {
        return (int) value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code long}.
     *
     * @return the long value
     */
    public long longValue() {
        return value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code float} after a
     * widening primitive conversion.
     *
     * @return the float value
     */
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code double} after a
     * widening primitive conversion.
     *
     * @return the double value
     */
    public double doubleValue() {
        return value;
    }

    /**
     * Compares two {@code AtomInteger} objects numerically.
     *
     * @param anotherAtomInteger the {@code AtomInteger} to be compared.
     * @throws NullPointerException if the specified {@code anotherAtomInteger}
     * is {@code null}
     * @return the value {@code 0} if this {@code AtomInteger} is equal to the
     * argument {@code AtomInteger}; a value less than {@code 0} if this
     * {@code AtomInteger} is numerically less than the argument
     * {@code AtomInteger}; and a value greater than {@code 0} if this
     * {@code AtomInteger} is numerically greater than the argument
     * {@code AtomInteger} (signed comparison).
     */
    @Override
    public int compareTo(final AtomInteger anotherAtomInteger) {
        return compare(value, requireNonNull(anotherAtomInteger).value);
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
        hash = 19 * hash + (int) (this.value ^ (this.value >>> 32));
        return hash;
    }

    /**
     * Compares this object to the specified object. The result is {@code true}
     * if and only if the argument is not {@code null} and is an
     * {@code AtomInteger} object that contains the same {@code long} value as
     * this object.
     *
     * @param obj the object to compare with.
     * @return {@code true} if the objects are the same; {@code false}
     * otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AtomInteger) {
            return value == ((AtomInteger) obj).longValue();
        }
        return false;
    }

    /**
     * Returns a {@code String} object representing this {@code AtomInteger}'s
     * value. The value is converted to signed decimal representation and
     * returned as a string, exactly as if the integer value were given as an
     * argument to the {@link java.lang.Long#toString(long)} method.
     *
     * @return a string representation of the value of this object in
     * base&nbsp;10.
     */
    @Override
    public String toString() {
        return Long.toString(value);
    }

    /**
     * Returns a deep copy of this {@link Atom}.
     *
     * @return a copy of this {@link Atom}
     */
    @Override
    public AtomInteger copy() {
        return new AtomInteger(value);
    }

}
