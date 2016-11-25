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
import java.util.logging.Level;
import java.util.logging.Logger;

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
public abstract class Bencode {

    private static final Logger LOGGER = Logger.getLogger(Bencode.class.getSimpleName());

    /**
     *
     * @param x
     * @return
     */
    public static final Atom decode(final String x) {
        return decode(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final Atom decode(final String x, final int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decode")) {
            return null;
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
                if (Character.isDigit(c)) {
                    return decodeStr(x, uiStart);
                }
        }
        LOGGER.log(Level.WARNING, "decode: found unexpected character '{0}' at {1}, halting decode", new Object[]{c, uiStart});
        return null;
    }

    /**
     *
     * @param x
     * @return
     */
    public static final AtomDictionary decodeDict(final String x) {
        return decodeDict(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final AtomDictionary decodeDict(final String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeDict")) {
            return null;
        }
        if (x.charAt(uiStart) != 'd') {
            charNotFound("decodeDict", "d");
            return null;
        }
        uiStart++;
        final AtomDictionary dict = new AtomDictionary();
        final int length = x.length();
        while (isMore(x, uiStart, length)) {
            final Atom key = decode(x, uiStart);
            if (!(key instanceof AtomString)) {
                itemError("decodeDict", "key");
                return null;
            }
            uiStart += key.bLength();
            final Atom value = decode(x, uiStart);
            if (!(key instanceof Atom)) {
                itemError("decodeDict", "value");
                return null;
            }
            uiStart += value.bLength();
            dict.put(key.toString(), value);
        }
        if (isComplete(x, uiStart, length)) {
            return dict;
        }
        charNotFound("decodeDict", "e");
        return null;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static final Atom decodeFile(final String fileName) {
        final String s = Utilities.readFileBytesToString(fileName, "windows-1252");
        return Bencode.decode(s);
    }

    /**
     *
     * @param x
     * @return
     */
    public static final AtomInteger decodeInt(final String x) {
        return decodeInt(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final AtomInteger decodeInt(final String x, final int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeInt")) {
            return null;
        }
        if (x.charAt(uiStart) != 'i') {
            charNotFound("decodeInt", "i");
            return null;
        }
        final int uiEnd = x.indexOf('e', uiStart);
        if (uiEnd == -1) {
            charNotFound("decodeInt", "c");
            return null;
        }
        final String s = x.substring(uiStart + 1, uiEnd);
        if (s.equals("-0")) {
            invalidInt("decodeInt", s);
            return null;
        }
        try {
            return new AtomInteger(Integer.parseInt(s, 10));
        } catch (final NumberFormatException nfe) {
            invalidInt("decodeInt", s);
            return null;
        }
    }

    /**
     *
     * @param x
     * @return
     */
    public static final AtomList decodeList(final String x) {
        return decodeList(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final AtomList decodeList(final String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeList")) {
            return null;
        }
        if (x.charAt(uiStart) != 'l') {
            charNotFound("decodeList", "d");
            return null;
        }
        uiStart++;
        final AtomList list = new AtomList();
        final int length = x.length();
        while (isMore(x, uiStart, length)) {
            final Atom value = decode(x, uiStart);
            if (Objects.isNull(value)) {
                itemError("decodeList", "value");
                return null;
            }
            uiStart += value.bLength();
            list.add(value);
        }
        if (isComplete(x, uiStart, length)) {
            return list;
        }
        charNotFound("decodeList", "e");
        return null;
    }

    /**
     *
     * @param x
     * @return
     */
    public static final AtomString decodeStr(final String x) {
        return decodeStr(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static final AtomString decodeStr(final String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeStr")) {
            return null;
        }
        if (!Character.isDigit(x.charAt(uiStart))) {
            charNotFound("decodeStr", "1234567890");
            return null;
        }
        final int uiSplit = Utilities.findFirstNotOf(x, "1234567890", uiStart);
        if (uiSplit == -1 || x.charAt(uiSplit) != ':') {
            charNotFound("decodeString", ":");
            return null;
        }
        final String length = x.substring(uiStart, uiSplit);
        final int uiLength;
        try {
            uiLength = Integer.parseInt(length, 10);
        } catch (NumberFormatException nfe) {
            invalidInt("decodeStr", length);
            return null;
        }
        String contents = x.substring(uiSplit + 1, uiSplit + uiLength + 1);
        return new AtomString(contents);
    }

    /**
     *
     * @param atom
     * @return
     */
    public static final String encode(final Atom atom) {
        if (Objects.nonNull(atom)) {
            return atom.encode();
        }
        LOGGER.log(Level.WARNING, "encode: error encoding, halting encode");
        return null;
    }

    private static void charNotFound(final Object... params) {
        LOGGER.log(Level.WARNING, "{0}: did not find \"{1}\", halting decode", params);
    }

    private static void invalidInt(final Object... params) {
        LOGGER.log(Level.WARNING, "{0}: invalid integer \"{1}\", halting decode", params);
    }

    private static boolean isArgCheckOk(final String x, final int uiStart, final String methodName) {
        if (Objects.isNull(x)) {
            LOGGER.log(Level.WARNING, "{0}: null string, halting decode", methodName);
            return false;
        }
        if (x.isEmpty() || x.substring(uiStart + 1).isEmpty()) {
            LOGGER.log(Level.WARNING, "{0}: empty string, halting decode", methodName);
            return false;
        }
        if (uiStart < 0 || uiStart >= x.length()) {
            LOGGER.log(Level.WARNING, "{0}: out of range, halting decode", methodName);
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
        LOGGER.log(Level.WARNING, "{0}: error decoding \"{1}\", halting decode", params);
    }

    private Bencode() {
    }

}
