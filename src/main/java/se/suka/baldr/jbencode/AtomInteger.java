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

import java.io.Serializable;
import static java.lang.Integer.compare;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;

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
public final class AtomInteger extends Atom implements Serializable, Comparable<AtomInteger> {

    private static final long serialVersionUID = 8464216577156678961L;

    private static final Logger LOGGER = getLogger(AtomInteger.class);

    /**
     * Backing {@link int}
     */
    private final int value;

    /**
     * Constructs a newly allocated {@code AtomInteger} object that represents
     * the {@code int} value of 0.
     */
    public AtomInteger() {
        this(0);
    }

    /**
     * Constructs a newly allocated {@code AtomInteger} object that represents
     * the specified {@code AtomInteger} value.
     *
     * @param anotherAtomInteger the value to be represented by the
     * {@code AtomInteger} object.
     */
    public AtomInteger(final AtomInteger anotherAtomInteger) {
        this(anotherAtomInteger.intValue());
    }

    /**
     * Constructs a newly allocated {@code AtomInteger} object that represents
     * the specified {@code int} value.
     *
     * @param value the value to be represented by the {@code Integer} object.
     */
    public AtomInteger(final int value) {
        this.value = value;
    }

    /**
     *
     * @return
     */
    @Override
    public int bLength() {
        return toString().length() + 2;
    }

    /**
     *
     * @return
     */
    @Override
    public String encode() {
        return "i" + value + "e";
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code byte} after a
     * narrowing primitive conversion.
     *
     * @return
     */
    public byte byteValue() {
        return (byte) value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code short} after a
     * narrowing primitive conversion.
     *
     * @return
     */
    public short shortValue() {
        return (short) value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as an {@code int}.
     *
     * @return
     */
    public int intValue() {
        return value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code long} after a
     * widening primitive conversion.
     *
     * @return
     * @see Integer#toUnsignedLong(int)
     */
    public long longValue() {
        return value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code float} after a
     * widening primitive conversion.
     *
     * @return
     */
    public float floatValue() {
        return value;
    }

    /**
     * Returns the value of this {@code AtomInteger} as a {@code double} after a
     * widening primitive conversion.
     *
     * @return
     */
    public double doubleValue() {
        return value;
    }

    /**
     * Compares two {@code AtomInteger} objects numerically.
     *
     * @param anotherAtomInteger
     * @throws NullPointerException if the specified anotherAtomInteger is null
     * @return the value {@code 0} if this {@code AtomInteger} is equal to the
     * argument {@code AtomInteger}; a value less than {@code 0} if this
     * {@code AtomInteger} is numerically less than the argument
     * {@code AtomInteger}; and a value greater than {@code 0} if this
     * {@code AtomInteger} is numerically greater than the argument
     * {@code AtomInteger} (signed comparison).
     */
    @Override
    public int compareTo(final AtomInteger anotherAtomInteger) {
        return compare(value, anotherAtomInteger.value);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AtomInteger) {
            return value == ((AtomInteger) obj).intValue();
        }
        return false;
    }

    /**
     * Returns a {@code String} object representing this {@code AtomInteger}'s
     * value. The value is converted to signed decimal representation and
     * returned as a string, exactly as if the integer value were given as an
     * argument to the {@link
     * java.lang.Integer#toString(int)} method.
     *
     * @return a string representation of the value of this object in
     * base&nbsp;10.
     */
    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public AtomInteger copy() {
        return new AtomInteger(value);
    }

}
