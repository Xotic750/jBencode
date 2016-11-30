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
import java.util.Objects;
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
public final class AtomString extends Atom implements Serializable, Comparable<AtomString> {

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
    }

    /**
     * Initialises a newly created {@code AtomString} object so that it
     * represents the same sequence of characters as the argument
     * {@code AtomString}; in other words, the newly created {@code AtomString}
     * is a copy of the argument {@code AtomString}.
     *
     * @param anotherAtomstring A {@code AtomString}
     */
    public AtomString(final AtomString anotherAtomstring) {
        this(anotherAtomstring.toString());
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
     *
     * @return
     */
    @Override
    public int bLength() {
        final int len = value.length();
        return Integer.toString(len).length() + 1 + len;
    }

    /**
     *
     * @return
     */
    @Override
    public String encode() {
        return value.length() + ":" + value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.value);
        return hash;
    }

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

    @Override
    public int compareTo(final AtomString atomString) {
        return value.compareTo(atomString.toString());
    }

    @Override
    public AtomString copy() {
        return new AtomString(value);
    }

}
