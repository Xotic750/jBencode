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

import static java.nio.charset.StandardCharsets.US_ASCII;
import java.util.Objects;
import static java.util.stream.Collectors.joining;
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
import static se.suka.baldr.jbencode.AtomString.requireAtomString;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomStringTest {

    private static final Logger LOGGER = getLogger(AtomStringTest.class);

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
    public AtomStringTest() {
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
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_1() {
        LOGGER.info("testAtomString_1");
        AtomString atomString1 = new AtomString();
        AtomString atomString2 = new AtomString();
        assertEquals(atomString1, atomString2);
    }

    /**
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_2() {
        LOGGER.info("testAtomString_2");
        AtomString atomString1 = new AtomString();
        Atom atom = new AtomString();
        assertEquals(atomString1, atom);
    }

    /**
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_3() {
        LOGGER.info("testAtomString_3");
        AtomString atomString1 = new AtomString("Hello");
        AtomString atomString2 = new AtomString("Hello");
        assertEquals(atomString1, atomString2);
    }

    /**
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_4() {
        LOGGER.info("testAtomString_4");
        AtomString atomString1 = new AtomString("Hello");
        AtomString atomString2 = new AtomString("Hej");
        assertNotEquals(atomString1, atomString2);
    }

    /**
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_5() {
        LOGGER.info("testAtomString_5");
        final AtomString hej = new AtomString("Hej");
        final AtomString actual = new AtomString(hej);
        final AtomString expected = new AtomString("Hej");
        assertEquals(expected, actual);
    }

    /**
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_6() {
        LOGGER.info("testAtomString_6");
        AtomString actual = null;
        final AtomString expected = null;
        boolean caught = false;
        try {
            actual = new AtomString(expected);
        } catch (final NullPointerException ex) {
            caught = true;
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_7() {
        LOGGER.info("testAtomString_7");
        final String s = "Hello";
        final byte[] b = s.getBytes(US_ASCII);
        AtomString atomString1 = new AtomString(b);
        AtomString atomString2 = new AtomString(s);
        assertEquals(atomString1, atomString2);
    }

    /**
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_8() {
        LOGGER.info("testAtomString_8");
        final CharSequence s = "Hello";
        AtomString atomString1 = new AtomString("Hello");
        AtomString atomString2 = new AtomString(s);
        assertEquals(atomString1, atomString2);
    }

    /**
     * Test the Constructor, of class AtomString.
     */
    @Test
    public void testAtomString_9() {
        LOGGER.info("testAtomString_9");
        final StringBuilder s = new StringBuilder("Hello");
        AtomString atomString1 = new AtomString("Hello");
        AtomString atomString2 = new AtomString(s);
        assertEquals(atomString1, atomString2);
    }

    /**
     * Test of bLength method, of class AtomString.
     */
    @Test
    public void testBLength() {
        LOGGER.info("testBLength");
        final int actual = new AtomString().bLength();
        final int expected = 2;
        assertEquals(expected, actual);
    }

    /**
     * Test of encode method, of class AtomString.
     */
    @Test
    public void testEncode() {
        LOGGER.info("testEncode");
        final String actual = new AtomString().encode();
        final String expected = "0:";
        assertEquals(expected, actual);
    }

    /**
     * Test of encodeAsBytes method, of class AtomString.
     */
    @Test
    public void testEncodeAsBytes() {
        LOGGER.info("encodeAsBytes");
        final AtomString atomString = new AtomString();
        final byte[] actual = atomString.encodeAsBytes();
        final byte[] expected = {0x30, 0x3a};
        assertArrayEquals(expected, actual);
    }

    /**
     * Test of hashCode method, of class AtomString.
     */
    @Test
    public void testHashCode() {
        LOGGER.info("testHashCode");
        final String value = "Hello";
        final int actual = new AtomString(value).hashCode();
        final int expected = 61 * 7 + Objects.hashCode(value);
        assertEquals(expected, actual);
    }

    /**
     * Test of equals method, of class AtomString.
     */
    @Test
    public void testEquals_1() {
        LOGGER.info("testEquals_1");
        final AtomString atomString = new AtomString("Hello");
        final Atom atom = new AtomString("Hello");
        final boolean actual = atomString.equals(atom);
        assertTrue(actual);
    }

    /**
     * Test of equals method, of class AtomString.
     */
    @Test
    public void testEquals_2() {
        LOGGER.info("testEquals_2");
        final AtomString atomString = new AtomString("Hello");
        final Atom atom = new AtomString("Hej");
        final boolean actual = atomString.equals(atom);
        assertFalse(actual);
    }

    /**
     * Test of equals method, of class AtomString.
     */
    @Test
    public void testEquals_3() {
        LOGGER.info("testEquals_3");
        final AtomString atomString = new AtomString("Hello");
        final Atom atom = null;
        final boolean actual = atomString.equals(atom);
        assertFalse(actual);
    }

    /**
     * Test of toString method, of class AtomString.
     */
    @Test
    public void testToString() {
        LOGGER.info("testToString");
        final String actual = new AtomString("Hello").toString();
        final String expected = "Hello";
        assertEquals(expected, actual);
    }

    /**
     * Test of compareTo method, of class AtomString.
     */
    @Test
    public void testCompareTo_1() {
        LOGGER.info("testCompareTo_1");
        final AtomString atomString1 = new AtomString();
        final AtomString atomString2 = new AtomString();
        final int actual = atomString1.compareTo(atomString2);
        final int expected = 0;
        assertEquals(expected, actual);
    }

    /**
     * Test of compareTo method, of class AtomString.
     */
    @Test
    public void testCompareTo_2() {
        LOGGER.info("testCompareTo_2");
        final AtomString atomString1 = new AtomString();
        final AtomString atomString2 = new AtomString("Hej");
        final int actual = atomString1.compareTo(atomString2);
        final int expected = -3;
        assertEquals(expected, actual);
    }

    /**
     * Test of compareTo method, of class AtomString.
     */
    @Test
    public void testCompareTo_3() {
        LOGGER.info("testCompareTo_3");
        final AtomString atomString1 = new AtomString("Hej");
        final AtomString atomString2 = new AtomString();
        final int actual = atomString1.compareTo(atomString2);
        final int expected = 3;
        assertEquals(expected, actual);
    }

    /**
     * Test of compareTo method, of class AtomString.
     */
    @Test
    public void testCompareTo_4() {
        LOGGER.info("testCompareTo_4");
        final AtomString atomString1 = new AtomString();
        Integer actual = null;
        final AtomString expected = null;
        boolean caught = false;
        try {
            actual = atomString1.compareTo(null);
        } catch (final NullPointerException ex) {
            caught = true;
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of copy method, of class AtomString.
     */
    @Test
    public void testCopy_1() {
        LOGGER.info("testCopy_1");
        final AtomString actual = new AtomString("Hej");
        final AtomString expected = actual.copy();
        assertEquals(expected, actual);
    }

    /**
     * Test of copy method, of class AtomString.
     */
    @Test
    public void testCopy_2() {
        LOGGER.info("testCopy_2");
        final String value = "Hej";
        final AtomString atomString1 = new AtomString(value);
        final AtomString atomString2 = atomString1.copy();
        assertTrue(atomString1 != atomString2);
        assertEquals(atomString1, atomString2);
        final String actual = atomString2.toString();
        final String expected = value;
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomString method, of class AtomString.
     */
    @Test
    public void testRequireAtomString_GenericType() {
        LOGGER.info("requireAtomString");
        Atom actual = null;
        final Atom expected = null;
        boolean caught = false;
        try {
            actual = requireAtomString(null);
        } catch (final ClassCastException ex) {
            caught = true;
            assertEquals("", ex.getMessage());
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomString method, of class AtomString.
     */
    @Test
    public void testRequireAtomString_GenericType_1() {
        LOGGER.info("requireAtomString");
        final AtomString actual = new AtomString();
        final AtomString expected = requireAtomString(actual);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomInteger method, of class AtomString.
     */
    @Test
    public void testRequireAtomString_GenericType_String_1() {
        LOGGER.info("requireAtomString with message");
        Atom actual = null;
        final Atom expected = null;
        boolean caught = false;
        try {
            actual = requireAtomString(null, "Message");
        } catch (final ClassCastException ex) {
            caught = true;
            assertEquals("Message", ex.getMessage());
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomString method, of class AtomString.
     */
    @Test
    public void testRequireAtomString_GenericType_String_2() {
        LOGGER.info("requireAtomString");
        final AtomString actual = new AtomString();
        final AtomString expected = requireAtomString(actual, "Message");
        assertEquals(expected, actual);
    }

    /**
     * Test the getBytes method, of class AtomString.
     */
    @Test
    public void testGetBytes() {
        LOGGER.info("testGetBytes");
        final String s = "Hello";
        final byte[] expected = s.getBytes(US_ASCII);
        final AtomString atomString1 = new AtomString(s);
        final byte[] actual = atomString1.getBytes();
        assertArrayEquals(expected, actual);
    }

    /**
     * Test the charAt method, of class AtomString.
     */
    @Test
    public void testCharAt() {
        LOGGER.info("testCharAt");
        final AtomString atomString1 = new AtomString("Hello");
        final char expected = 'e';
        final char actual = atomString1.charAt(1);
        assertEquals(expected, actual);
    }

    /**
     * Test the charAt method, of class AtomString.
     */
    @Test
    public void testSubSequence() {
        LOGGER.info("testSubSequence");
        final AtomString atomString1 = new AtomString("Hello");
        final CharSequence expected = "el";
        final CharSequence actual = atomString1.subSequence(1, 3);
        assertEquals(expected, actual);
    }

    /**
     * Test the chars method, of class AtomString.
     */
    @Test
    public void testChars() {
        LOGGER.info("testChars");
        final String expected = "Hello";
        final AtomString atomString1 = new AtomString(expected);
        final String actual = atomString1.chars()
                .mapToObj(i -> new String(new int[]{i}, 0, 1))
                .collect(joining());
        assertEquals(expected, actual);
    }

    /**
     * Test the codePoints method, of class AtomString.
     */
    @Test
    public void testCodePoints() {
        LOGGER.info("testCodePoints");
        final String expected = "Hello";
        final AtomString atomString1 = new AtomString(expected);
        final String actual = atomString1.codePoints()
                .mapToObj(i -> new String(new int[]{i}, 0, 1))
                .collect(joining());
        assertEquals(expected, actual);
    }

}
