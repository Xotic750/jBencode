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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.Bencode.decode;
import static se.suka.baldr.jbencode.Bencode.decodeDict;
import static se.suka.baldr.jbencode.Bencode.decodeFile;
import static se.suka.baldr.jbencode.Bencode.decodeInt;
import static se.suka.baldr.jbencode.Bencode.decodeList;
import static se.suka.baldr.jbencode.Bencode.decodeStr;
import static se.suka.baldr.jbencode.Bencode.encode;
import static se.suka.baldr.jbencode.Bencode.encodeAsBytes;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class BencodeTest {

    private static final Logger LOGGER = getLogger(BencodeTest.class);

    /**
     * Sometimes several tests need to share computationally expensive setup
     * (like logging into a database). While this can compromise the
     * independence of tests, sometimes it is a necessary optimization.
     * Annotating a public static void no-arg method with @BeforeClass causes it
     * to be run once before any of the test methods in the class. The
     *
     * @BeforeClass methods of superclasses will be run before those of the
     * current class, unless they are shadowed in the current class.
     */
    @BeforeClass
    public static void setUpClass() {
    }

    /**
     * If you allocate expensive external resources in a BeforeClass method you
     * need to release them after all the tests in the class have run.
     * Annotating a public static void method with @AfterClass causes that
     * method to be run after all the tests in the class have been run. All
     *
     * @AfterClass methods are guaranteed to run even if a BeforeClass method
     * throws an exception. The @AfterClass methods declared in superclasses
     * will be run after those of the current class, unless they are shadowed in
     * the current class.
     */
    @AfterClass
    public static void tearDownClass() {
    }

    /**
     * Test constructor.
     */
    public BencodeTest() {
    }

    /**
     * When writing tests, it is common to find that several tests need similar
     * objects created before they can run. Annotating a public void method with
     *
     * @Before causes that method to be run before the Test method. The @Before
     * methods of superclasses will be run before those of the current class,
     * unless they are overridden in the current class. No other ordering is
     * defined.
     */
    @Before
    public void setUp() {
    }

    /**
     * If you allocate external resources in a Before method you need to release
     * them after the test runs. Annotating a public void method with @After
     * causes that method to be run after the Test method. All @After methods
     * are guaranteed to run even if a Before or Test method throws an
     * exception. The @After methods declared in superclasses will be run after
     * those of the current class, unless they are overridden in the current
     * class.
     */
    @After
    public void tearDown() {
    }

    /**
     * Test exception thrown when constructor called.
     */
    @Test
    public void testBencode() {
        LOGGER.info("testBencode");
        Bencode actual = null;
        final Bencode expected = null;
        boolean caught = false;
        try {
            actual = new Bencode();
        } catch (final UnsupportedOperationException ex) {
            caught = true;
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of decode method, of class Bencode.
     */
    @Test
    public void testDecode_String_1() {
        LOGGER.info("decode");
        final String x = "d1:a11:Hello World1:ble1:ci0ee";
        final AtomDictionary expected = new AtomDictionary();
        expected.put("a", new AtomString("Hello World"));
        expected.put("b", new AtomList());
        expected.put("c", new AtomInteger());
        final Atom actual = decode(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decode method, of class Bencode.
     */
    @Test
    public void testDecode_String_2() {
        LOGGER.info("decode");
        final String x = null;
        final Atom expected = null;
        final Atom actual = decode(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decode method, of class Bencode.
     */
    @Test
    public void testDecode_String_3() {
        LOGGER.info("decode");
        final String x = "";
        final Atom expected = null;
        final Atom actual = decode(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decode method, of class Bencode.
     */
    @Test
    public void testDecode_String_4() {
        LOGGER.info("decode");
        final String x = "ae";
        final Atom expected = null;
        final Atom actual = decode(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decode method, of class Bencode.
     */
    @Test
    public void testDecode_Byte_1() {
        LOGGER.info("decode");
        final String s = "d1:a11:Hello World1:ble1:ci0ee";
        final byte[] x = s.getBytes(StandardCharsets.US_ASCII);
        final AtomDictionary expected = new AtomDictionary();
        expected.put("a", new AtomString("Hello World"));
        expected.put("b", new AtomList());
        expected.put("c", new AtomInteger());
        final Atom actual = decode(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decode method, of class Bencode.
     */
    @Test
    public void testDecode_String_int_1() {
        LOGGER.info("decode at start index");
        final int uiStart = 4;
        final String x = "d1:a11:Hello Worlde";
        final Atom expected = new AtomString("Hello World");
        final Atom actual = decode(x, uiStart);
        assertEquals(expected, actual);
    }

    /**
     * Test of decode method, of class Bencode.
     */
    @Test
    public void testDecode_String_int_2() {
        LOGGER.info("decode at start index");
        final int uiStart = 40;
        final String x = "d1:a11:Hello Worlde";
        final Atom expected = null;
        final Atom actual = decode(x, uiStart);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeDict method, of class Bencode.
     */
    @Test
    public void testDecodeDict_String_1() {
        LOGGER.info("decodeDict");
        final String x = "d1:a11:Hello Worlde";
        final AtomDictionary expected = new AtomDictionary();
        expected.put("a", new AtomString("Hello World"));
        final Atom actual = decodeDict(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeDict method, of class Bencode.
     */
    @Test
    public void testDecodeDict_String_2() {
        LOGGER.info("decodeDict");
        final String x = null;
        final AtomDictionary expected = null;
        final Atom actual = decodeDict(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeDict method, of class Bencode.
     */
    @Test
    public void testDecodeDict_String_3() {
        LOGGER.info("decodeDict");
        final String x = "1:a11:Hello Worlde";
        final AtomDictionary expected = null;
        final Atom actual = decodeDict(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeDict method, of class Bencode.
     */
    @Test
    public void testDecodeDict_String_4() {
        LOGGER.info("decodeDict");
        final String x = "d1:a11:Hello World";
        final AtomDictionary expected = null;
        final Atom actual = decodeDict(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeDict method, of class Bencode.
     */
    @Test
    public void testDecodeDict_String_5() {
        LOGGER.info("decodeDict");
        final String x = "di0e11:Hello Worlde";
        final AtomDictionary expected = null;
        final Atom actual = decodeDict(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeDict method, of class Bencode.
     */
    @Test
    public void testDecodeDict_String_6() {
        LOGGER.info("decodeDict");
        final String x = "d1:a11:Helloe";
        final AtomDictionary expected = null;
        final Atom actual = decodeDict(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeDict method, of class Bencode.
     */
    @Test
    public void testDecodeDict_Byte_1() {
        LOGGER.info("decodeDict");
        final String s = "d1:a11:Hello Worlde";
        final byte[] x = s.getBytes(StandardCharsets.US_ASCII);
        final AtomDictionary expected = new AtomDictionary();
        expected.put("a", new AtomString("Hello World"));
        final Atom actual = decodeDict(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeDict method, of class Bencode.
     */
    @Test
    public void testDecodeDict_String_int() {
        LOGGER.info("decodeDict at start index");
        final int uiStart = 5;
        final String x = "li42ed1:a11:Hello Worldee";
        final AtomDictionary expected = new AtomDictionary();
        expected.put("a", new AtomString("Hello World"));
        final Atom actual = decodeDict(x, uiStart);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeFile method, of class Bencode.
     */
    @Test
    public void testDecodeFile() {
        LOGGER.info("decodeFile");
        final Atom actual = decodeFile("samples/sample1.torrent");
        final AtomDictionary expected = new AtomDictionary();
        expected.put("announce", new AtomString("udp://tracker.openbittorrent.com:80"));
        expected.put("creation date", new AtomInteger(1327049827));
        final AtomDictionary info = new AtomDictionary();
        info.put("length", new AtomInteger(20));
        info.put("name", new AtomString("sample.txt"));
        info.put("piece length", new AtomInteger(65536));
        info.put("pieces", new AtomString(""));
        info.put("private", new AtomInteger(1));
        expected.put("info", info);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeInt method, of class Bencode.
     */
    @Test
    public void testDecodeInt_String_1() {
        LOGGER.info("decodeInt");
        final String x = "i42e";
        final Atom expected = new AtomInteger(42);
        final Atom actual = decodeInt(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeInt method, of class Bencode.
     */
    @Test
    public void testDecodeInt_String_2() {
        LOGGER.info("decodeInt");
        final String x = "";
        final Atom expected = null;
        final Atom actual = decodeInt(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeInt method, of class Bencode.
     */
    @Test
    public void testDecodeInt_String_3() {
        LOGGER.info("decodeInt");
        final String x = "42e";
        final Atom expected = null;
        final Atom actual = decodeInt(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeInt method, of class Bencode.
     */
    @Test
    public void testDecodeInt_String_4() {
        LOGGER.info("decodeInt");
        final String x = "i42x";
        final Atom expected = null;
        final Atom actual = decodeInt(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeInt method, of class Bencode.
     */
    @Test
    public void testDecodeInt_String_5() {
        LOGGER.info("decodeInt");
        final String x = "i-0e";
        final Atom expected = null;
        final Atom actual = decodeInt(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeInt method, of class Bencode.
     */
    @Test
    public void testDecodeInt_String_6() {
        LOGGER.info("decodeInt");
        final String x = "ixe";
        final Atom expected = null;
        final Atom actual = decodeInt(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeInt method, of class Bencode.
     */
    @Test
    public void testDecodeInt_Byte_1() {
        LOGGER.info("decodeInt");
        final String s = "i42e";
        final byte[] x = s.getBytes(StandardCharsets.US_ASCII);
        final Atom expected = new AtomInteger(42);
        final Atom actual = decodeInt(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeInt method, of class Bencode.
     */
    @Test
    public void testDecodeInt_String_int() {
        LOGGER.info("decodeInt at start index");
        final int uiStart = 1;
        final String x = "li42ee";
        final Atom expected = new AtomInteger(42);
        final Atom actual = decodeInt(x, uiStart);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeList method, of class Bencode.
     */
    @Test
    public void testDecodeList_String_1() {
        LOGGER.info("decodeList");
        final String x = "li42ed1:a11:Hello Worldee";
        final AtomList expected = new AtomList();
        final AtomDictionary dict = new AtomDictionary();
        dict.put("a", new AtomString("Hello World"));
        expected.add(new AtomInteger(42));
        expected.add(dict);
        final Atom actual = decodeList(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeList method, of class Bencode.
     */
    @Test
    public void testDecodeList_String_2() {
        LOGGER.info("decodeList");
        final String x = "";
        final AtomList expected = null;
        final Atom actual = decodeList(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeList method, of class Bencode.
     */
    @Test
    public void testDecodeList_String_3() {
        LOGGER.info("decodeList");
        final String x = "i42ed1:a11:Hello Worldee";
        final AtomList expected = null;
        final Atom actual = decodeList(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeList method, of class Bencode.
     */
    @Test
    public void testDecodeList_String_4() {
        LOGGER.info("decodeList");
        final String x = "li42ed1:a11:Hello Worlde";
        final AtomList expected = null;
        final Atom actual = decodeList(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeList method, of class Bencode.
     */
    @Test
    public void testDecodeList_String_5() {
        LOGGER.info("decodeList");
        final String x = "li42ed1:a11:Helloe";
        final AtomList expected = null;
        final Atom actual = decodeList(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeList method, of class Bencode.
     */
    @Test
    public void testDecodeList_Byte_1() {
        LOGGER.info("decodeList");
        final String s = "li42ed1:a11:Hello Worldee";
        final byte[] x = s.getBytes(StandardCharsets.US_ASCII);
        final AtomList expected = new AtomList();
        final AtomDictionary dict = new AtomDictionary();
        dict.put("a", new AtomString("Hello World"));
        expected.add(new AtomInteger(42));
        expected.add(dict);
        final Atom actual = decodeList(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeList method, of class Bencode.
     */
    @Test
    public void testDecodeList_String_int() {
        LOGGER.info("decodeList at start index");
        final int uiStart = 4;
        final String x = "d1:bli42ed1:a11:Hello Worldeee";
        final AtomList expected = new AtomList();
        final AtomDictionary dict = new AtomDictionary();
        dict.put("a", new AtomString("Hello World"));
        expected.add(new AtomInteger(42));
        expected.add(dict);
        final Atom actual = decodeList(x, uiStart);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeStr method, of class Bencode.
     */
    @Test
    public void testDecodeStr_String_1() {
        LOGGER.info("decodeStr");
        final String x = "11:Hello World";
        final Atom expected = new AtomString("Hello World");
        final Atom actual = decodeStr(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeStr method, of class Bencode.
     */
    @Test
    public void testDecodeStr_String_2() {
        LOGGER.info("decodeStr");
        final String x = "";
        final Atom expected = null;
        final Atom actual = decodeStr(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeStr method, of class Bencode.
     */
    @Test
    public void testDecodeStr_String_3() {
        LOGGER.info("decodeStr");
        final String x = "11:Hello";
        final Atom expected = null;
        final Atom actual = decodeStr(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeStr method, of class Bencode.
     */
    @Test
    public void testDecodeStr_String_4() {
        LOGGER.info("decodeStr");
        final String x = "xx:Hello World";
        final Atom expected = null;
        final Atom actual = decodeStr(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeStr method, of class Bencode.
     */
    @Test
    public void testDecodeStr_String_5() {
        LOGGER.info("decodeStr");
        final String x = "11xHello World";
        final Atom expected = null;
        final Atom actual = decodeStr(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeStr method, of class Bencode.
     */
    @Test
    public void testDecodeStr_Byte_1() {
        LOGGER.info("decodeStr");
        final String s = "11:Hello World";
        final byte[] x = s.getBytes(StandardCharsets.US_ASCII);
        final Atom expected = new AtomString("Hello World");
        final Atom actual = decodeStr(x);
        assertEquals(expected, actual);
    }

    /**
     * Test of decodeStr method, of class Bencode.
     */
    @Test
    public void testDecodeStr_String_int() {
        LOGGER.info("decodeStr at start index");
        final int uiStart = 4;
        final String x = "d1:a11:Hello Worlde";
        final Atom expected = new AtomString("Hello World");
        final Atom actual = decodeStr(x, uiStart);
        assertEquals(expected, actual);
    }

    /**
     * Test of encode method, of class Bencode.
     */
    @Test
    public void testEncode_1() {
        LOGGER.info("encode");
        final String expected = "li42ed1:a11:Hello Worldee";
        final AtomList list = new AtomList();
        final AtomDictionary dict = new AtomDictionary();
        dict.put("a", new AtomString("Hello World"));
        list.add(new AtomInteger(42));
        list.add(dict);
        final String actual = encode(list);
        assertEquals(expected, actual);
    }

    /**
     * Test of encode method, of class Bencode.
     */
    @Test
    public void testEncode_2() {
        LOGGER.info("encode");
        final String expected = null;
        final AtomList list = null;
        final String actual = encode(list);
        assertEquals(expected, actual);
    }

    /**
     * Test of encodeAsBytes method, of class Bencode.
     */
    @Test
    public void testEncodeAsBytes_1() {
        LOGGER.info("encodeAsBytes");
        final String s = "li42ed1:a11:Hello Worldee";
        final byte[] expected = s.getBytes(StandardCharsets.US_ASCII);
        final AtomList list = new AtomList();
        final AtomDictionary dict = new AtomDictionary();
        dict.put("a", new AtomString("Hello World"));
        list.add(new AtomInteger(42));
        list.add(dict);
        final byte[] actual = encodeAsBytes(list);
        Assert.assertArrayEquals(expected, actual);
    }

    /**
     * Test of encodeAsBytes method, of class Bencode.
     */
    @Test
    public void testEncodeAsBytes_2() {
        LOGGER.info("encodeAsBytes");
        final byte[] expected = null;
        final AtomList list = null;
        final byte[] actual = encodeAsBytes(list);
        Assert.assertArrayEquals(expected, actual);
    }

}
