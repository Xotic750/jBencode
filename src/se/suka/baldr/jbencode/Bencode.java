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

    /**
     *
     * @param x
     * @return
     */
    public static Atom decode(String x) {
        return decode(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static Atom decode(String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decode")) {
            return null;
        }
        char c = x.charAt(uiStart);
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
        System.err.println("decode: found unexpected character '" + c + "' at " + uiStart + ", halting decode");
        return null;
    }

    /**
     *
     * @param x
     * @return
     */
    public static AtomDictionary decodeDict(String x) {
        return decodeDict(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static AtomDictionary decodeDict(String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeDict")) {
            return null;
        }
        if (x.charAt(uiStart) != 'd') {
            charNotFound("d", "decodeDict");
            return null;
        }
        uiStart++;
        AtomDictionary dict = new AtomDictionary();
        int length = x.length();
        while (isMore(x, uiStart, length)) {
            Atom key = decode(x, uiStart);
            if (!(key instanceof AtomString)) {
                itemError("key", "decodeDict");
                return null;
            }
            uiStart += key.bLength();
            Atom value = decode(x, uiStart);
            if (!(key instanceof Atom)) {
                itemError("value", "decodeDict");
                return null;
            }
            uiStart += value.bLength();
            dict.put(key.toString(), value);
        }
        if (isComplete(x, uiStart, length)) {
            return dict;
        }
        charNotFound("e", "decodeDict");
        return null;
    }

    /**
     *
     * @param fileName
     * @return
     */
    public static Atom decodeFile(String fileName) {
        String s = Utils.readFileBytesToString(fileName, "windows-1252");
        Atom atom = Bencode.decode(s);
        return atom;
    }

    /**
     *
     * @param x
     * @return
     */
    public static AtomInteger decodeInt(String x) {
        return decodeInt(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static AtomInteger decodeInt(String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeInt")) {
            return null;
        }
        if (x.charAt(uiStart) != 'i') {
            charNotFound("i", "decodeInt");
            return null;
        }
        int uiEnd = x.indexOf('e', uiStart);
        if (uiEnd == -1) {
            charNotFound("c", "decodeInt");
            return null;
        }
        String s = x.substring(uiStart + 1, uiEnd);
        if (s.equals("-0")) {
            invalidInt(s, "decodeInt");
            return null;
        }
        try {
            return new AtomInteger(Integer.parseInt(s, 10));
        } catch (NumberFormatException nfe) {
            invalidInt(s, "decodeInt");
            return null;
        }
    }

    /**
     *
     * @param x
     * @return
     */
    public static AtomList decodeList(String x) {
        return decodeList(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static AtomList decodeList(String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeList")) {
            return null;
        }
        if (x.charAt(uiStart) != 'l') {
            charNotFound("d", "decodeList");
            return null;
        }
        uiStart++;
        AtomList list = new AtomList();
        int length = x.length();
        while (isMore(x, uiStart, length)) {
            Atom value = decode(x, uiStart);
            if (Objects.isNull(value)) {
                itemError("value", "decodeList");
                return null;
            }
            uiStart += value.bLength();
            list.add(value);
        }
        if (isComplete(x, uiStart, length)) {
            return list;
        }
        charNotFound("e", "decodeList");
        return null;
    }

    /**
     *
     * @param x
     * @return
     */
    public static AtomString decodeStr(String x) {
        return decodeStr(x, 0);
    }

    /**
     *
     * @param x
     * @param uiStart
     * @return
     */
    public static AtomString decodeStr(String x, int uiStart) {
        if (!isArgCheckOk(x, uiStart, "decodeStr")) {
            return null;
        }
        if (!Character.isDigit(x.charAt(uiStart))) {
            charNotFound("1234567890", "decodeStr");
            return null;
        }
        int uiSplit = Utils.findFirstNotOf(x, "1234567890", uiStart);
        if (uiSplit == -1 || x.charAt(uiSplit) != ':') {
            charNotFound(":", "decodeString");
            return null;
        }
        String length = x.substring(uiStart, uiSplit);
        int uiLength;
        try {
            uiLength = Integer.parseInt(length, 10);
        } catch (NumberFormatException nfe) {
            invalidInt(length, "decodeStr");
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
    public static String encode(Atom atom) {
        if (Objects.nonNull(atom)) {
            return atom.encode();
        }
        System.err.println("encode: error encoding, halting encode");
        return null;
    }

    private static void charNotFound(String string, String methodName) {
        System.err.println(methodName + ": did not find '" + string + "', halting decode");
    }

    private static void invalidInt(String s, String methodName) {
        System.err.println(methodName + ": invalid integer '" + s + "', halting decode");
    }

    private static boolean isArgCheckOk(String x, int uiStart, String methodName) {
        if (Objects.isNull(x)) {
            System.err.println(methodName + ": null string, halting decode");
            return false;
        }
        if (x.isEmpty() || x.substring(uiStart + 1).isEmpty()) {
            System.err.println(methodName + ": empty string, halting decode");
            return false;
        }
        if (uiStart < 0 || uiStart >= x.length()) {
            System.err.println(methodName + ": out of range, halting decode");
            return false;
        }
        return true;
    }

    private static boolean isComplete(String x, int uiStart, int length) {
        return uiStart < length && x.charAt(uiStart) == 'e';
    }

    private static boolean isMore(String x, int uiStart, int length) {
        return uiStart < length && x.charAt(uiStart) != 'e';
    }

    private static void itemError(String type, String methodName) {
        System.err.println(methodName + ": error decoding " + type + ", halting decode");
    }

    private Bencode() {
    }

}
