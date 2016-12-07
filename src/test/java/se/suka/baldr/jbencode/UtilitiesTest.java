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

import static java.lang.System.out;
import static java.nio.charset.StandardCharsets.US_ASCII;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.Utilities.asciiBytesToString;
import static se.suka.baldr.jbencode.Utilities.clamp;
import static se.suka.baldr.jbencode.Utilities.findFirstNotOf;
import static se.suka.baldr.jbencode.Utilities.randInt;
import static se.suka.baldr.jbencode.Utilities.randIntClosed;
import static se.suka.baldr.jbencode.Utilities.readFileAsBytes;
import static se.suka.baldr.jbencode.Utilities.stringToAsciiBytes;

/**
 *
 * @author Graham Fairweather
 */
public class UtilitiesTest {

    private static final Logger LOGGER = getLogger(UtilitiesTest.class);

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
    public UtilitiesTest() {
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
    public void testUtilites() {
        LOGGER.info("testUtilites");
        Utilities actual = null;
        final Utilities expected = null;
        boolean caught = false;
        try {
            actual = new Utilities();
        } catch (final UnsupportedOperationException ex) {
            caught = true;
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test correct index is found when no startIndex is provided.
     */
    @Test
    public void testFindFirstNotOf_1() {
        LOGGER.info("testFindFirstNotOf_1");
        final String s = "abc123abc123";
        final int i = findFirstNotOf(s, "cba");
        assertEquals(i, 3);
    }

    /**
     * Test correct index is not found when no startIndex is provided.
     */
    @Test
    public void testFindFirstNotOf_2() {
        LOGGER.info("testFindFirstNotOf_2");
        final String s = "abc123abc123";
        final int i = findFirstNotOf(s, "a1b2c3");
        assertEquals(i, -1);
    }

    /**
     * Test correct index is found when startIndex is provided.
     */
    @Test
    public void testFindFirstNotOf_3() {
        LOGGER.info("testFindFirstNotOf_3");
        final String s = "abc123abc123";
        final int i = findFirstNotOf(s, "cba", 6);
        assertEquals(i, 9);
    }

    /**
     * Test correct index is not found when startIndex is provided.
     */
    @Test
    public void testFindFirstNotOf_4() {
        LOGGER.info("testFindFirstNotOf_4");
        final String s = "abc123abc123";
        final int i = findFirstNotOf(s, "a1b2c3", 6);
        assertEquals(i, -1);
    }

    /**
     * Test correct index is not found when no startIndex is provided and both
     * strings are empty.
     */
    @Test
    public void testFindFirstNotOf_5() {
        LOGGER.info("testFindFirstNotOf_5");
        final String s = "";
        final int i = findFirstNotOf(s, "");
        assertEquals(i, -1);
    }

    /**
     * Test correct exception is thrown with null sequence.
     */
    @Test
    public void testFindFirstNotOf_6() {
        LOGGER.info("testFindFirstNotOf_6");
        boolean caught = false;
        try {
            findFirstNotOf(null, "");
        } catch (final NullPointerException ex) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * Test correct exception is thrown with null matcher sequence.
     */
    @Test
    public void testFindFirstNotOf_7() {
        LOGGER.info("testFindFirstNotOf_7");
        boolean caught = false;
        try {
            findFirstNotOf("", null);
        } catch (final NullPointerException ex) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * Test correct exception is thrown with negative startIndex.
     */
    @Test
    public void testFindFirstNotOf_8() {
        LOGGER.info("testFindFirstNotOf_8");
        boolean caught = false;
        try {
            findFirstNotOf("", "", -1);
        } catch (final IndexOutOfBoundsException ex) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * Test correct exception is thrown with startIndex greater than length of
     * sequence.
     */
    @Test
    public void testFindFirstNotOf_9() {
        LOGGER.info("testFindFirstNotOf_9");
        boolean caught = false;
        try {
            findFirstNotOf("", "", 1);
        } catch (final IndexOutOfBoundsException ex) {
            caught = true;
        }
        assertTrue(caught);
    }

    /**
     * Should return null when no pathName is provided.
     */
    @Test
    public void testReadFileAsBytes_1() {
        LOGGER.info("testReadFileAsBytes_1");
        final byte[] b = readFileAsBytes("");
        assertNull(b);
    }

    /**
     * Should throw correct exception when null is provided as pathName.
     */
    @Test
    public void testReadFileAsBytes_2() {
        LOGGER.info("testReadFileAsBytes_2");
        boolean nullPointerEx = false;
        try {
            readFileAsBytes(null);
        } catch (final NullPointerException ex) {
            nullPointerEx = true;
        }
        assertTrue(nullPointerEx);
    }

    /**
     * Should return null when pathName is a directory.
     */
    @Test
    public void testReadFileAsBytes_3() {
        LOGGER.info("testReadFileAsBytes_3");
        final byte[] b = readFileAsBytes("samples");
        assertNull(b);
    }

    /**
     * Should return null when pathName is non existant file.
     */
    @Test
    public void testReadFileAsBytes_4() {
        LOGGER.info("testReadFileAsBytes_4");
        final byte[] b = readFileAsBytes("wibble");
        assertNull(b);
    }

    /**
     * Test a text only torrent file.
     */
    @Test
    public void testReadFileAsBytes_5() {
        LOGGER.info("testReadFileAsBytes_5");
        final byte[] f = readFileAsBytes("samples/sample1.torrent");
        final String s = "d8:announce35:udp://tracker.openbittorrent.com:8013:creation datei1327049827e4:infod6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1eee\r";
        final byte[] b = s.getBytes(US_ASCII);
        assertArrayEquals(f, b);
    }

    /**
     * Test a byte string torrent file.
     */
    @Test
    public void testReadFileAsBytes_6() {
        LOGGER.info("testReadFileAsBytes_6");
        final byte[] b = readFileAsBytes("samples/sample2.torrent");
        assertNotNull(b);
    }

    /**
     * Test of randInt method, of class Utilities is in bounds.
     */
    @Test
    public void testRandInt() {
        LOGGER.info("testRandInt");
        final int min = 1;
        final int max = 3;
        for (int count = 0; count < 100; count++) {
            final int result = randInt(min, max);
            assertTrue(result >= min && result < max);
        }
    }

    /**
     * Test of randIntClosed method, of class Utilities is in bounds.
     */
    @Test
    public void testRandIntClosed() {
        LOGGER.info("testRandIntClosed");
        final int min = 1;
        final int max = 3;
        for (int count = 0; count < 100; count++) {
            final int result = randIntClosed(min, max);
            assertTrue(result >= min && result <= max);
        }
    }

    /**
     * Test of clamp method, of class Utilities is in bounds.
     */
    @Test
    public void testClamp_1() {
        LOGGER.info("testClamp_1");
        final int value = 25;
        final int min = 0;
        final int max = 50;
        final int expResult = 25;
        final int result = clamp(value, min, max);
        assertEquals(expResult, result);
    }

    /**
     * Test of clamp method, of class Utilities is in bounds.
     */
    @Test
    public void testClamp_2() {
        LOGGER.info("testClamp_2");
        final int value = -10;
        final int min = 0;
        final int max = 50;
        final int expResult = 0;
        final int result = clamp(value, min, max);
        assertEquals(expResult, result);
    }

    /**
     * Test of clamp method, of class Utilities is in bounds.
     */
    @Test
    public void testClamp_3() {
        LOGGER.info("testClamp_3");
        final int value = 100;
        final int min = 0;
        final int max = 50;
        final int expResult = 50;
        final int result = clamp(value, min, max);
        assertEquals(expResult, result);
    }

    /**
     * Test of asciiBytesToString method, of class Utilities.
     */
    @Test
    public void testAsciiBytesToString() {
        out.println("asciiBytesToString");
        final byte[] x = {0x48, 0x65, 0x6c, 0x6c, 0x6f};
        final String expected = "Hello";
        final String actual = asciiBytesToString(x);

        assertEquals(expected, actual);
    }

    /**
     * Test of stringToAsciiBytes method, of class Utilities.
     */
    @Test
    public void testStringToAsciiBytes() {
        out.println("stringToAsciiBytes");
        final String x = "Hello";
        final byte[] expected = {0x48, 0x65, 0x6c, 0x6c, 0x6f};
        final byte[] actual = stringToAsciiBytes(x);
        assertArrayEquals(expected, actual);
    }

}
