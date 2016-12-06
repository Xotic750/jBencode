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
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.Atom.requireAtom;
import static se.suka.baldr.jbencode.Utilities.stringToAsciiBytes;

/**
 * Test the base abstract Atom class.
 *
 * @author Graham Fairweather
 */
public class AtomTest {

    private static final Logger LOGGER = getLogger(AtomTest.class);

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
    public AtomTest() {
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
     * Test of requireAtom method, of class Atom.
     */
    @Test
    public void testRequireAtom_GenericType() {
        LOGGER.info("requireAtom");
        Atom actual = null;
        final Atom expected = null;
        boolean caught = false;
        try {
            actual = requireAtom(null);
        } catch (final ClassCastException ex) {
            caught = true;
            assertEquals("", ex.getMessage());
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtom method, of class Atom.
     */
    @Test
    public void testRequireAtom_GenericType_1() {
        LOGGER.info("requireAtom");
        final Atom actual = new AtomImpl();
        final Atom expected = requireAtom(actual);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtom method, of class Atom.
     */
    @Test
    public void testRequireAtom_GenericType_String() {
        LOGGER.info("requireAtom with message");
        Atom actual = null;
        final Atom expected = null;
        boolean caught = false;
        try {
            actual = requireAtom(null, "Message");
        } catch (final ClassCastException ex) {
            caught = true;
            assertEquals("Message", ex.getMessage());
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of bLength method, of class Atom.
     */
    @Test
    public void testBLength() {
        LOGGER.info("bLength");
        final Atom atom = new AtomImpl();
        final int expected = 0;
        final int actual = atom.bLength();
        assertEquals(expected, actual);
    }

    /**
     * Test of copy method, of class Atom.
     */
    @Test
    public void testCopy() {
        LOGGER.info("copy");
        final Atom atom = new AtomImpl();
        final Atom expected = null;
        final Atom actual = atom.copy();
        assertEquals(expected, actual);
    }

    /**
     * Test of encode method, of class Atom.
     */
    @Test
    public void testEncode() {
        LOGGER.info("encode");
        final Atom atom = new AtomImpl();
        final String expected = "";
        final String actual = atom.encode();
        assertEquals(expected, actual);
    }

    /**
     * Test of encodeAsBytes method, of class AtomList.
     */
    @Test
    public void testEncodeAsBytes() {
        LOGGER.info("encodeAsBytes");
        final Atom atom = new AtomImpl();
        final byte[] actual = atom.encodeAsBytes();
        final byte[] expected = {};
        assertArrayEquals(expected, actual);
    }

    /**
     * A test class
     */
    static class AtomImpl extends Atom {

        private static final long serialVersionUID = -3640442364342462294L;

        /**
         * Returns the length of the Bencoded string of this {@link Atom}. This
         * method is faster than performing an <code>encode().length()</code>.
         *
         * @return The length of the Becoded string
         */
        @Override
        public int bLength() {
            return 0;
        }

        /**
         * Returns a deep copy of this {@link Atom}.
         *
         * @return a copy of this {@link Atom}
         */
        @Override
        public Atom copy() {
            return null;
        }

        /**
         * Returns the Bencoded string of this {@link Atom}.
         *
         * @return The Benoded string
         */
        @Override
        public String encode() {
            return "";
        }

        /**
         * Returns the Bencoded ASCII bytes of this {@link AtomString}.
         *
         * @return The Benoded ASCII bytes
         */
        @Override
        public byte[] encodeAsBytes() {
            return stringToAsciiBytes(encode());
        }
    }

}
