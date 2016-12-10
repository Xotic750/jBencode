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

import static java.lang.Character.isDigit;
import static java.text.MessageFormat.format;
import static java.util.Objects.isNull;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.OptionalLong;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.AtomString.isAtomString;
import static se.suka.baldr.jbencode.Utilities.asciiBytesToString;
import static se.suka.baldr.jbencode.Utilities.findFirstNotOf;
import static se.suka.baldr.jbencode.Utilities.readFileAsBytes;

/**
 * Bencode (pronounced like B encode) is the encoding used by the peer-to-peer
 * file sharing system BitTorrent for storing and transmitting loosely
 * structured data.
 *
 * It supports four different types of values:
 *
 * byte strings, integers, lists, and dictionaries (associative arrays).
 *
 * Bencoding is most commonly used in torrent files. These metadata files are
 * simply bencoded dictionaries.
 *
 * While less efficient than a pure binary encoding, bencoding is simple and
 * (because numbers are encoded as text in decimal notation) is unaffected by
 * endianness, which is important for a cross-platform application like
 * BitTorrent. It is also fairly flexible, as long as applications ignore
 * unexpected dictionary keys, so that new ones can be added without creating
 * incompatibilities.
 *
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public class Bencode {

    private static final Logger LOGGER = getLogger(Bencode.class);

    private static boolean isInteger(final String s) {
        return s.matches("^(0|-?[1-9]\\d*)$");
    }

    private static boolean isUInteger(final String s) {
        return s.matches("^(0|[1-9]\\d*)$");
    }

    static OptionalInt parseInt(final String s) {
        try {
            return OptionalInt.of(Integer.parseInt(s, 10));
        } catch (final NumberFormatException nfe) {
            return OptionalInt.empty();
        }
    }

    static OptionalLong parseLong(final String s) {
        try {
            return OptionalLong.of(Long.parseLong(s, 10));
        } catch (final NumberFormatException nfe) {
            return OptionalLong.empty();
        }
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<? extends Atom> decode(final byte[] x) {
        return decode(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<? extends Atom> decode(final byte[] x, final int uiStart) {
        return decode(asciiBytesToString(x), uiStart);
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<? extends Atom> decode(final String x) {
        return decode(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<? extends Atom> decode(final String x, final int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decode")) {
            return Optional.empty();
        }
        final char c = x.charAt(uiStart);
        switch (c) {
            case 'i':
                return decodeInt(x, uiStart);
            case 'l':
                return decodeList(x, uiStart);
            case 'd':
                return decodeDict(x, uiStart);
            default:
                if (isDigit(c)) {
                    return decodeStr(x, uiStart);
                }
        }
        LOGGER.warn(format("decode: found unexpected character '{0}' at {1}, halting decode", new Object[]{c, uiStart}));
        return Optional.empty();
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<AtomDictionary> decodeDict(final byte[] x) {
        return decodeDict(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<AtomDictionary> decodeDict(final byte[] x, final int uiStart) {
        return decodeDict(asciiBytesToString(x), uiStart);
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<AtomDictionary> decodeDict(final String x) {
        return decodeDict(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<AtomDictionary> decodeDict(final String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeDict")) {
            return Optional.empty();
        }
        if (x.charAt(uiStart) != 'd') {
            charNotFound("decodeDict", "d");
            return Optional.empty();
        }
        uiStart++;
        final AtomDictionary dict = new AtomDictionary();
        final int length = x.length();
        while (isMore(x, uiStart, length)) {
            final Optional<? extends Atom> key = decode(x, uiStart);
            if (!key.isPresent() || !isAtomString(key.get())) {
                itemError("decodeDict", "key");
                return Optional.empty();
            }
            uiStart += key.get().bLength();
            final Optional<? extends Atom> value = decode(x, uiStart);
            if (!value.isPresent()) {
                itemError("decodeDict", "value");
                return Optional.empty();
            }
            uiStart += value.get().bLength();
            dict.put((AtomString) key.get(), value.get());
        }
        if (isComplete(x, uiStart, length)) {
            return Optional.of(dict);
        }
        charNotFound("decodeDict", "e");
        return Optional.empty();
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static final Optional<? extends Atom> decodeFile(final String fileName) {
        final Optional<byte[]> b = readFileAsBytes(fileName);
        if (b.isPresent()) {
            return decode(b.get());
        }
        return Optional.empty();
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<AtomInteger> decodeInt(final byte[] x) {
        return decodeInt(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<AtomInteger> decodeInt(final byte[] x, final int uiStart) {
        return decodeInt(asciiBytesToString(x), uiStart);
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<AtomInteger> decodeInt(final String x) {
        return decodeInt(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<AtomInteger> decodeInt(final String x, final int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeInt")) {
            return Optional.empty();
        }
        if (x.charAt(uiStart) != 'i') {
            charNotFound("decodeInt", "i");
            return Optional.empty();
        }
        final int uiEnd = x.indexOf('e', uiStart);
        if (uiEnd == -1) {
            charNotFound("decodeInt", "e");
            return Optional.empty();
        }
        final String s = x.substring(uiStart + 1, uiEnd);
        if (!isInteger(s)) {
            invalidNumber("decodeInt", s);
            return Optional.empty();
        }
        final Long value = parseLong(s).orElseGet(null);
        if (isNull(value)) {
            invalidNumber("decodeInt", s);
            return Optional.empty();
        }
        return Optional.of(new AtomInteger(value));
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<AtomList> decodeList(final byte[] x) {
        return decodeList(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<AtomList> decodeList(final byte[] x, final int uiStart) {
        return decodeList(asciiBytesToString(x), uiStart);
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<AtomList> decodeList(final String x) {
        return decodeList(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<AtomList> decodeList(final String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeList")) {
            return Optional.empty();
        }
        if (x.charAt(uiStart) != 'l') {
            charNotFound("decodeList", "d");
            return Optional.empty();
        }
        uiStart++;
        final AtomList list = new AtomList();
        final int length = x.length();
        while (isMore(x, uiStart, length)) {
            final Optional<? extends Atom> value = decode(x, uiStart);
            if (!value.isPresent()) {
                itemError("decodeList", "value");
                return Optional.empty();
            }
            uiStart += value.get().bLength();
            list.add(value.get());
        }
        if (isComplete(x, uiStart, length)) {
            return Optional.of(list);
        }
        charNotFound("decodeList", "e");
        return Optional.empty();
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<AtomString> decodeStr(final byte[] x) {
        return decodeStr(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<AtomString> decodeStr(final byte[] x, final int uiStart) {
        return decodeStr(asciiBytesToString(x), uiStart);
    }

    /**
     *
     * @param x
     * @return
     */
    public static final Optional<AtomString> decodeStr(final String x) {
        return decodeStr(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Optional<AtomString> decodeStr(final String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeStr")) {
            return Optional.empty();
        }
        if (!isDigit(x.charAt(uiStart))) {
            charNotFound("decodeStr", "1234567890");
            return Optional.empty();
        }
        int uiSplit = findFirstNotOf(x, "1234567890", uiStart);
        if (uiSplit == -1 || x.charAt(uiSplit) != ':') {
            charNotFound("decodeStr", ":");
            return Optional.empty();
        }
        final String length = x.substring(uiStart, uiSplit);
        if (!isUInteger(length)) {
            invalidNumber("decodeStr", length);
            return Optional.empty();
        }
        final Integer uiLength = parseInt(length).orElseGet(null);
        if (isNull(uiLength) || uiLength < 0) {
            invalidNumber("decodeStr", uiLength);
            return Optional.empty();
        }
        if (++uiSplit > x.length() || uiSplit + uiLength > x.length()) {
            LOGGER.warn("decodeStr: out of bounds");
            return Optional.empty();
        }
        final String contents = x.substring(uiSplit, uiSplit + uiLength);
        return Optional.of(new AtomString(contents));
    }

    private static void charNotFound(final Object... params) {
        LOGGER.warn(format("{0}: did not find \"{1}\", halting decode", params));
    }

    private static void invalidNumber(final Object... params) {
        LOGGER.warn(format("{0}: invalid integer \"{1}\", halting decode", params));
    }

    private static boolean isArgCheckOk(final String x, final int uiStart, final String methodName) {
        if (isNull(x)) {
            LOGGER.warn(format("{0}: null string, halting decode", methodName));
            return false;
        }
        if (x.isEmpty()) {
            LOGGER.warn(format("{0}: empty string, halting decode", methodName));
            return false;
        }
        if (uiStart < 0 || uiStart > x.length()) {
            LOGGER.warn(format("{0}: out of range, halting decode", methodName));
            return false;
        }
        return true;
    }

    private static boolean isComplete(final String x, final int uiStart, final int length) {
        return uiStart < length && x.charAt(uiStart) == 'e';
    }

    private static boolean isMore(final String x, final int uiStart, final int length) {
        return uiStart < length && x.charAt(uiStart) != 'e';
    }

    private static void itemError(final Object... params) {
        LOGGER.warn(format("{0}: error decoding \"{1}\", halting decode", params));
    }

    /**
     * No construction
     */
    Bencode() {
        throw new UnsupportedOperationException();
    }

}
