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
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.AtomInteger.requireAtomInteger;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomIntegerTest {

    private static final Logger LOGGER = getLogger(AtomIntegerTest.class);

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
    public AtomIntegerTest() {
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
     * Test the Constructor, of class AtomInteger.
     */
    @Test
    public void testAtomInteger_1() {
        LOGGER.info("testAtomInteger_1");
        final AtomInteger actual = new AtomInteger();
        final AtomInteger expected = new AtomInteger();
        assertEquals(expected, actual);
    }

    /**
     * Test the Constructor, of class AtomInteger.
     */
    @Test
    public void testAtomInteger_2() {
        LOGGER.info("testAtomInteger_2");
        final Atom actual = new AtomInteger();
        final AtomInteger expected = new AtomInteger();
        assertEquals(expected, actual);
    }

    /**
     * Test the Constructor, of class AtomInteger.
     */
    @Test
    public void testAtomInteger_3() {
        LOGGER.info("testAtomInteger_3");
        final AtomInteger actual = new AtomInteger(1);
        final AtomInteger expected = new AtomInteger(1);
        assertEquals(expected, actual);
    }

    /**
     * Test the Constructor, of class AtomInteger.
     */
    @Test
    public void testAtomInteger_4() {
        LOGGER.info("testAtomInteger_4");
        final AtomInteger actual = new AtomInteger(1);
        final AtomInteger expected = new AtomInteger(2);
        assertNotEquals(expected, actual);
    }

    /**
     * Test the Constructor, of class AtomInteger.
     */
    @Test
    public void testAtomInteger_5() {
        LOGGER.info("testAtomInteger_5");
        final AtomInteger one = new AtomInteger(1);
        final AtomInteger actual = new AtomInteger(one);
        final AtomInteger expected = new AtomInteger(1);
        assertEquals(expected, actual);
    }

    /**
     * Test the Constructor, of class AtomInteger.
     */
    @Test
    public void testAtomInteger_6() {
        LOGGER.info("testAtomInteger_5");
        AtomInteger actual = null;
        final AtomInteger expected = null;
        boolean caught = false;
        try {
            actual = new AtomInteger(null);
        } catch (final NullPointerException ex) {
            caught = true;
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of bLength method, of class AtomInteger.
     */
    @Test
    public void testBLength() {
        LOGGER.info("testBLength");
        final int actual = new AtomInteger().bLength();
        final int expected = 3;
        assertEquals(expected, actual);
    }

    /**
     * Test of encode method, of class AtomInteger.
     */
    @Test
    public void testEncode() {
        LOGGER.info("testEncode");
        final String actual = new AtomInteger().encode();
        final String expected = "i0e";
        assertEquals(expected, actual);
    }

    /**
     * Test of encodeAsBytes method, of class AtomInteger.
     */
    @Test
    public void testEncodeAsBytes() {
        LOGGER.info("encodeAsBytes");
        final AtomInteger atomInteger = new AtomInteger();
        final byte[] actual = atomInteger.encodeAsBytes();
        final byte[] expected = {0x69, 0x30, 0x65};
        assertArrayEquals(expected, actual);
    }

    /**
     * Test of byteValue method, of class AtomInteger.
     */
    @Test
    public void testByteValue() {
        LOGGER.info("testByteValue");
        final byte actual = new AtomInteger().byteValue();
        final byte expected = 0;
        assertEquals(expected, actual);
    }

    /**
     * Test of shortValue method, of class AtomInteger.
     */
    @Test
    public void testShortValue() {
        LOGGER.info("testShortValue");
        final short actual = new AtomInteger().shortValue();
        final short expected = 0;
        assertEquals(expected, actual);
    }

    /**
     * Test of intValue method, of class AtomInteger.
     */
    @Test
    public void testIntValue() {
        LOGGER.info("testIntValue");
        final int actual = new AtomInteger().intValue();
        final int expected = 0;
        assertEquals(expected, actual);
    }

    /**
     * Test of longValue method, of class AtomInteger.
     */
    @Test
    public void testLongValue() {
        LOGGER.info("testLongValue");
        final long actual = new AtomInteger().longValue();
        final long expected = 0;
        assertEquals(expected, actual);
    }

    /**
     * Test of floatValue method, of class AtomInteger.
     */
    @Test
    public void testFloatValue() {
        LOGGER.info("testFloatValue");
        final float actual = new AtomInteger().floatValue();
        final float expected = 0;
        final double delta = 0;
        assertEquals(expected, actual, delta);
    }

    /**
     * Test of doubleValue method, of class AtomInteger.
     */
    @Test
    public void testDoubleValue() {
        LOGGER.info("testDoubleValue");
        final double actual = new AtomInteger().doubleValue();
        final double expected = 0;
        final double delta = 0;
        assertEquals(expected, actual, delta);
    }

    /**
     * Test of compareTo method, of class AtomInteger.
     */
    @Test
    public void testCompareTo_1() {
        LOGGER.info("testCompareTo_1");
        final AtomInteger atomInteger1 = new AtomInteger();
        final AtomInteger atomInteger2 = new AtomInteger();
        final int actual = atomInteger1.compareTo(atomInteger2);
        final int expected = 0;
        assertEquals(expected, actual);
    }

    /**
     * Test of compareTo method, of class AtomInteger.
     */
    @Test
    public void testCompareTo_2() {
        LOGGER.info("testCompareTo_2");
        final AtomInteger atomInteger1 = new AtomInteger();
        final AtomInteger atomInteger2 = new AtomInteger(10);
        final int actual = atomInteger1.compareTo(atomInteger2);
        final int expected = -1;
        assertEquals(expected, actual);
    }

    /**
     * Test of compareTo method, of class AtomInteger.
     */
    @Test
    public void testCompareTo_3() {
        LOGGER.info("testCompareTo_3");
        final AtomInteger atomInteger1 = new AtomInteger();
        final AtomInteger atomInteger2 = new AtomInteger(-10);
        final int actual = atomInteger1.compareTo(atomInteger2);
        final int expected = 1;
        assertEquals(expected, actual);
    }

    /**
     * Test of compareTo method, of class AtomInteger.
     */
    @Test
    public void testCompareTo_4() {
        LOGGER.info("testCompareTo_4");
        final AtomInteger atomInteger1 = new AtomInteger();
        Integer actual = null;
        final AtomInteger expected = null;
        boolean caught = false;
        try {
            actual = atomInteger1.compareTo(null);
        } catch (final NullPointerException ex) {
            caught = true;
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of hashCode method, of class AtomInteger.
     */
    @Test
    public void testHashCode() {
        LOGGER.info("testHashCode");
        final long value = 10;
        final int actual = new AtomInteger(value).hashCode();
        final int expected = 175 + Long.hashCode(value);
        assertEquals(expected, actual);
    }

    /**
     * Test of equals method, of class AtomInteger.
     */
    @Test
    public void testEquals_1() {
        LOGGER.info("testEquals_1");
        final AtomInteger atomInteger = new AtomInteger(-10);
        final Atom atom = new AtomInteger(-10);
        final boolean actual = atomInteger.equals(atom);
        assertTrue(actual);
    }

    /**
     * Test of equals method, of class AtomInteger.
     */
    @Test
    public void testEquals_2() {
        LOGGER.info("testEquals_2");
        final AtomInteger atomInteger = new AtomInteger(-1);
        final Atom atom = new AtomInteger(-10);
        final boolean actual = atomInteger.equals(atom);
        assertFalse(actual);
    }

    /**
     * Test of equals method, of class AtomInteger.
     */
    @Test
    public void testEquals_3() {
        LOGGER.info("testEquals_3");
        final AtomInteger atomInteger = new AtomInteger(-1);
        final Atom atom = null;
        final boolean actual = atomInteger.equals(atom);
        assertFalse(actual);
    }

    /**
     * Test of toString method, of class AtomInteger.
     */
    @Test
    public void testToString() {
        LOGGER.info("testToString");
        final String actual = new AtomInteger().toString();
        final String expected = "0";
        assertEquals(expected, actual);
    }

    /**
     * Test of copy method, of class AtomInteger.
     */
    @Test
    public void testCopy_1() {
        LOGGER.info("testCopy_1");
        final AtomInteger actual = new AtomInteger(2);
        final AtomInteger expected = actual.copy();
        assertEquals(expected, actual);
    }

    /**
     * Test of copy method, of class AtomInteger.
     */
    @Test
    public void testCopy_2() {
        LOGGER.info("testCopy_2");
        final int value = 2;
        final AtomInteger atomInteger1 = new AtomInteger(value);
        final AtomInteger atomInteger2 = atomInteger1.copy();
        assertTrue(atomInteger1 != atomInteger2);
        assertEquals(atomInteger1, atomInteger2);
        final int actual = atomInteger2.intValue();
        final int expected = value;
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomInteger method, of class AtomInteger.
     */
    @Test
    public void testRequireAtomInteger_GenericType() {
        LOGGER.info("requireAtomInteger");
        Atom actual = null;
        final Atom expected = null;
        boolean caught = false;
        try {
            actual = requireAtomInteger(null);
        } catch (final ClassCastException ex) {
            caught = true;
            assertEquals("", ex.getMessage());
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomInteger method, of class AtomInteger.
     */
    @Test
    public void testRequireAtomInteger_GenericType_1() {
        LOGGER.info("requireAtomInteger_1");
        final AtomInteger actual = new AtomInteger();
        final AtomInteger expected = requireAtomInteger(actual);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomInteger method, of class AtomInteger.
     */
    @Test
    public void testRequireAtomInteger_GenericType_String_1() {
        LOGGER.info("requireAtomInteger with message");
        Atom actual = null;
        final Atom expected = null;
        boolean caught = false;
        try {
            actual = requireAtomInteger(null, "Message");
        } catch (final ClassCastException ex) {
            caught = true;
            assertEquals("Message", ex.getMessage());
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomInteger method, of class AtomInteger.
     */
    @Test
    public void testRequireAtomInteger_GenericType_String_2() {
        LOGGER.info("requireAtomInteger with message");
        final AtomInteger actual = new AtomInteger();
        final AtomInteger expected = requireAtomInteger(actual, "Message");
        assertEquals(expected, actual);
    }

}
