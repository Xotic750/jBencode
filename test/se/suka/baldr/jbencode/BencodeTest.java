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

import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class BencodeTest {

    /**
     *
     */
    public BencodeTest() {
    }

    /**
     *
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     *
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }

    /**
     *
     */
    @After
    public void tearDown() {
    }

    /**
     *
     */
    @Test
    public void testDecodeDictIntStrLst() {
        AtomDictionary l = Bencode.decodeDict("d3:fooli5e5:Helloe3:barli50000000e11:Hello Worldee");
        AtomDictionary expected = new AtomDictionary();
        AtomList il = new AtomList();
        il.add(new AtomInteger(5));
        il.add(new AtomString("Hello"));
        AtomList im = new AtomList();
        im.add(new AtomInteger(50000000));
        im.add(new AtomString("Hello World"));
        expected.put("foo", il);
        expected.put("bar", im);
        assertEquals(l, expected);
    }

    /**
     *
     */
    @Test
    public void testDecodeEmptyInt() {
        Atom atom = Bencode.decode("ie");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeEmptyStr() {
        Atom atom = Bencode.decodeStr("0:");
        assertEquals(atom, new AtomString());
    }

    /**
     *
     */
    @Test
    public void testDecodeFileSample1() {
        Atom atom = Bencode.decodeFile("samples/sample1.torrent");
        assertNotNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeFileSample2() {
        Atom atom = Bencode.decodeFile("samples/sample2.torrent");
        assertNotNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeIntEmpty() {
        Atom atom = Bencode.decodeInt("");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeIntEmptyLE() {
        Atom atom = Bencode.decodeInt("ie");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeIntInvalidNegZero() {
        Atom atom = Bencode.decodeInt("i-0e");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeIntNeg() {
        Atom atom = Bencode.decodeInt("i-12e");
        assertEquals(atom, new AtomInteger(-12));
    }

    /**
     *
     */
    @Test
    public void testDecodeIntNull() {
        Atom atom = Bencode.decodeInt(null);
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeIntPos() {
        Atom atom = Bencode.decodeInt("i12e");
        assertEquals(atom, new AtomInteger(12));
    }

    /**
     *
     */
    @Test
    public void testDecodeIntPosZero() {
        Atom atom = Bencode.decodeInt("i0e");
        assertEquals(atom, new AtomInteger(0));
    }

    /**
     *
     */
    @Test
    public void testDecodeIntStrLst() {
        Atom l = Bencode.decode("d3:barli50000000e11:Hello Worlde3:fooli5e5:Helloee");
        AtomDictionary expected = new AtomDictionary();
        AtomList il = new AtomList();
        il.add(new AtomInteger(5));
        il.add(new AtomString("Hello"));
        AtomList im = new AtomList();
        im.add(new AtomInteger(50000000));
        im.add(new AtomString("Hello World"));
        expected.put("foo", il);
        expected.put("bar", im);
        assertEquals(l, expected);
    }

    /**
     *
     */
    @Test
    public void testDecodeInvalidNegZeroInt() {
        Atom atom = Bencode.decode("i-0e");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeListEmpty() {
        Atom atom = Bencode.decodeList("");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeListEmptyLE() {
        Atom atom = Bencode.decodeList("le");
        assertEquals(atom, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testDecodeListIntStr() {
        Atom l = Bencode.decodeList("li5e5:Helloe");
        AtomList expected = new AtomList();
        expected.add(new AtomInteger(5));
        expected.add(new AtomString("Hello"));
        assertEquals(l, expected);
    }

    /**
     *
     */
    @Test
    public void testDecodeListIntStrLst() {
        Atom l = Bencode.decodeList("lli5e5:Helloeli50000000e11:Hello Worldee");
        AtomList expected = new AtomList();
        AtomList il = new AtomList();
        il.add(new AtomInteger(5));
        il.add(new AtomString("Hello"));
        AtomList im = new AtomList();
        im.add(new AtomInteger(50000000));
        im.add(new AtomString("Hello World"));
        expected.add(il);
        expected.add(im);
        assertEquals(l, expected);
    }

    /**
     *
     */
    @Test
    public void testDecodeListNull() {
        Atom atom = Bencode.decodeList(null);
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeNegInt() {
        Atom atom = Bencode.decode("i-12e");
        assertEquals(atom, new AtomInteger(-12));
    }

    /**
     *
     */
    @Test
    public void testDecodeNull() {
        Atom atom = Bencode.decode(null);
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodePartialTorrent1() {
        String s = "d6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1ee";
        Atom atom = Bencode.decodeDict(s);
        assertNotNull(atom);
        assertEquals(s.length(), atom.bLength());
    }

    /**
     *
     */
    @Test
    public void testDecodePartialTorrent2() {
        String s = "d8:announce35:udp://tracker.openbittorrent.com:8013:creation datei1327049827e4:infod6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1eee";
        Atom atom = Bencode.decodeDict(s);
        assertNotNull(atom);
        assertEquals(s.length(), atom.bLength());
    }

    /**
     *
     */
    @Test
    public void testDecodePosInt() {
        Atom atom = Bencode.decode("i12e");
        assertEquals(atom, new AtomInteger(12));
    }

    /**
     *
     */
    @Test
    public void testDecodePosZeroInt() {
        Atom atom = Bencode.decode("i0e");
        assertEquals(atom, new AtomInteger(0));
    }

    /**
     *
     */
    @Test
    public void testDecodeSmallStr() {
        Atom atom = Bencode.decodeStr("5:Hello");
        assertEquals(atom, new AtomString("Hello"));
    }

    /**
     *
     */
    @Test
    public void testDecodeStr() {
        Atom atom = Bencode.decodeStr("5:Hello");
        assertEquals(atom, new AtomString("Hello"));
    }

    /**
     *
     */
    @Test
    public void testDecodeStrEmpty() {
        Atom atom = Bencode.decodeStr("");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeStrEmpty0() {
        Atom atom = Bencode.decodeStr("0:");
        assertEquals(atom, new AtomString(""));
    }

    /**
     *
     */
    @Test
    public void testDecodeStrInvalidChar() {
        Atom atom = Bencode.decodeStr("0a:");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeStrLarge() {
        Atom atom = Bencode.decodeStr("11:Hello World");
        assertEquals(atom, new AtomString("Hello World"));
    }

    /**
     *
     */
    @Test
    public void testDecodeStrLargeStr() {
        Atom atom = Bencode.decodeStr("11:Hello World");
        assertEquals(atom, new AtomString("Hello World"));
    }

    /**
     *
     */
    @Test
    public void testDecodeStrMissing() {
        Atom s = Bencode.decodeStr("0");
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testDecodeStrMissingNum() {
        Atom atom = Bencode.decodeStr("a");
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testDecodeStrNull() {
        Atom atom = Bencode.decodeStr(null);
        assertNull(atom);
    }

    /**
     *
     */
    @Test
    public void testEncodeAtom() {
        Atom atom = new AtomInteger(5);
        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "i5e");
    }

    /**
     *
     */
    @Test
    public void testEncodeDict() {
        AtomDictionary atom = new AtomDictionary();

        AtomList il = new AtomList();
        il.add(new AtomInteger(5));
        il.add(new AtomString("Hello"));
        atom.put("foo", il);

        AtomList im = new AtomList();
        im.add(new AtomInteger(50000000));
        im.add(new AtomString("Hello World"));
        atom.put("bar", im);

        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "d3:barli50000000e11:Hello Worlde3:fooli5e5:Helloee");
    }

    /**
     *
     */
    @Test
    public void testEncodeList() {
        AtomList atom = new AtomList();
        atom.add(new AtomInteger(5));
        atom.add(new AtomString("Hello"));
        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "li5e5:Helloe");
    }

    /**
     *
     */
    @Test
    public void testEncodeNull() {
        String encoded = Bencode.encode(null);
        assertNull(encoded);
    }

    /**
     *
     */
    @Test
    public void testEncondeInt() {
        Atom atom = new AtomInteger(1);
        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "i1e");
    }

    /**
     *
     */
    @Test
    public void testEncondeStr() {
        Atom atom = new AtomString("Hello");
        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "5:Hello");
    }

}
