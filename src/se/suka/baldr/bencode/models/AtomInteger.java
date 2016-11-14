package se.suka.baldr.bencode.models;

import java.util.Objects;

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
public final class AtomInteger extends Atom<Integer> {

    public AtomInteger() {
        this(0);
    }

    public AtomInteger(AtomInteger atomInteger) {
        this(atomInteger.intValue());
    }

    public AtomInteger(Integer value) {
        super(value);
    }

    @Override
    public int bLength() {
        return toString().length() + 2;
    }

    @Override
    public String encode() {
        return "i" + this + "e";
    }

    /**
     * Returns the value of the specified number as an {@code int}, which may
     * involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion to
     * type {@code int}.
     */
    public int intValue() {
        return getValue();
    }

    /**
     * Returns the value of the specified number as a {@code long}, which may
     * involve rounding or truncation.
     *
     * @return the numeric value represented by this object after conversion to
     * type {@code long}.
     */
    public long longValue() {
        return (long) intValue();
    }

    /**
     * Returns the value of the specified number as a {@code float}, which may
     * involve rounding.
     *
     * @return the numeric value represented by this object after conversion to
     * type {@code float}.
     */
    public float floatValue() {
        return (float) intValue();
    }

    /**
     * Returns the value of the specified number as a {@code double}, which may
     * involve rounding.
     *
     * @return the numeric value represented by this object after conversion to
     * type {@code double}.
     */
    public double doubleValue() {
        return (double) intValue();
    }

    /**
     * Returns the value of the specified number as a {@code byte}, which may
     * involve rounding or truncation.
     *
     * <p>
     * This implementation returns the result of {@link #intValue} cast to a
     * {@code byte}.
     *
     * @return the numeric value represented by this object after conversion to
     * type {@code byte}.
     */
    public byte byteValue() {
        return (byte) intValue();
    }

    /**
     * Returns the value of the specified number as a {@code short}, which may
     * involve rounding or truncation.
     *
     * <p>
     * This implementation returns the result of {@link #intValue} cast to a
     * {@code short}.
     *
     * @return the numeric value represented by this object after conversion to
     * type {@code short}.
     */
    public short shortValue() {
        return (short) intValue();
    }

    /**
     * Compares two {@code AtomInteger} objects numerically.
     *
     * @param anotherAtomInteger the {@code AtomInteger} to be compared.
     * @throws NullPointerException if the specified anotherAtomInteger is null
     * @return the value {@code 0} if this {@code AtomInteger} is equal to the
     * argument {@code AtomInteger}; a value less than {@code 0} if this
     * {@code AtomInteger} is numerically less than the argument
     * {@code AtomInteger}; and a value greater than {@code 0} if this
     * {@code AtomInteger} is numerically greater than the argument
     * {@code AtomInteger} (signed comparison).
     */
    public int compareTo(AtomInteger anotherAtomInteger) {
        Objects.requireNonNull(anotherAtomInteger);
        return getValue().compareTo(anotherAtomInteger.intValue());
    }

}
