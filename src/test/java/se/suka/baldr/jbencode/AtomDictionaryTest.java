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

import static java.util.stream.Collectors.joining;
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
import static se.suka.baldr.jbencode.AtomDictionary.isAtomDictionary;
import static se.suka.baldr.jbencode.AtomDictionary.requireAtomDictionary;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomDictionaryTest {
    
    private static final Logger LOGGER = getLogger(AtomDictionaryTest.class);

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
    public AtomDictionaryTest() {
    }

    /**
     *
     */
    @Before
    public void setUp() {
    }
    
    /**
     *
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     *
     */
    @Test
    public void testAtomDictionary_1() {
        final AtomDictionary ai = new AtomDictionary();
        final Atom atomDictionary = new AtomDictionary(ai);
        assertEquals(ai, atomDictionary);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionary_2() {
        final AtomDictionary ai = new AtomDictionary();
        final Atom ai1 = new AtomDictionary(ai);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionary_3() {
        final Atom atomDictionary = new AtomDictionary();
        assertEquals(new AtomDictionary(), atomDictionary);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryOrder() {
        final AtomDictionary ai = new AtomDictionary();
        final Atom i = new AtomInteger(1);
        ai.put(new AtomString("foo"), i);
        ai.put(new AtomString("bar"), i);
        ai.put(new AtomString("boo"), i);
        ai.put(new AtomString("90"), i);
        ai.put(new AtomString("100"), i);
        ai.put(new AtomString("ABC90"), i);
        ai.put(new AtomString("ABC100"), i);
        final String keysConcat = ai.keySet().stream().map(key -> key.toString()).collect(joining());
        assertEquals(keysConcat, "10090ABC100ABC90barboofoo");
    }

    /**
     * Test of requireAtomDictionary method, of class AtomDictionary.
     */
    @Test
    public void testRequireAtomDictionary_GenericType() {
        LOGGER.info("requireAtomDictionary");
        Atom actual = null;
        final Atom expected = null;
        boolean caught = false;
        try {
            actual = requireAtomDictionary(null);
        } catch (final ClassCastException ex) {
            caught = true;
            assertEquals("", ex.getMessage());
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomDictionary method, of class AtomDictionary.
     */
    @Test
    public void testRequireAtomDictionary_GenericType_1() {
        LOGGER.info("requireAtomDictionary");
        final AtomDictionary actual = new AtomDictionary();
        final AtomDictionary expected = requireAtomDictionary(actual);
        assertEquals(expected, actual);
    }

    /**
     * Test of requireAtomList method, of class AtomDictionary.
     */
    @Test
    public void testRequireAtomDictionary_GenericType_String() {
        LOGGER.info("requireAtomDictionary with message");
        Atom actual = null;
        final Atom expected = null;
        boolean caught = false;
        try {
            actual = requireAtomDictionary(null, "Message");
        } catch (final ClassCastException ex) {
            caught = true;
            assertEquals("Message", ex.getMessage());
        }
        assertTrue(caught);
        assertEquals(expected, actual);
    }

    /**
     * Test of encodeAsBytes method, of class AtomDictionary.
     */
    @Test
    public void testEncodeAsBytes() {
        LOGGER.info("encodeAsBytes");
        final AtomDictionary atomDictionary = new AtomDictionary();
        final byte[] actual = atomDictionary.encodeAsBytes();
        final byte[] expected = {0x64, 0x65};
        assertArrayEquals(expected, actual);
    }

    /**
     * Test of isAtomDictionary method, of class AtomDictionary.
     */
    @Test
    public void testIsAtomDictionary() {
        LOGGER.info("isAtomDictionary");
        final Atom atom = new AtomDictionary();
        final boolean result = isAtomDictionary(atom);
        assertTrue(result);
    }

    /**
     * Test of bLength method, of class AtomDictionary.
     */
    @Test
    public void testBLength() {
        LOGGER.info("bLength");
        final AtomDictionary ai = new AtomDictionary();
        final Atom i = new AtomInteger(1);
        ai.put(new AtomString("foo"), i);
        final Atom s = new AtomString("Hello");
        ai.put(new AtomString("bar"), s);
        assertEquals(ai.bLength(), 22);
    }

    /**
     * Test of clone method, of class AtomDictionary.
     */
    @Test
    public void testClone() {
        LOGGER.info("clone");
        final AtomDictionary ai = new AtomDictionary();
        final Atom i = new AtomInteger(1);
        ai.put(new AtomString("foo"), i);
        ai.put(new AtomString("bar"), i);
        ai.put(new AtomString("boo"), i);
        ai.put(new AtomString("90"), i);
        ai.put(new AtomString("100"), i);
        ai.put(new AtomString("ABC90"), i);
        ai.put(new AtomString("ABC100"), i);
        final AtomDictionary aj = ai.clone();
        assertTrue(ai != aj);
        assertTrue(ai.getClass() == aj.getClass());
        assertEquals(ai, aj);
    }

    /**
     * Test of copy method, of class AtomDictionary.
     */
    @Test
    public void testCopy() {
        LOGGER.info("copy");
        final AtomDictionary ai = new AtomDictionary();
        ai.put(new AtomString("foo"), new AtomInteger(1));
        ai.put(new AtomString("bar"), new AtomString("hello"));
        ai.put(new AtomString("boo"), new AtomList());
        ai.put(new AtomString("far"), new AtomDictionary());
        final AtomDictionary aj = ai.copy();
        assertTrue(ai != aj);
        assertTrue(ai.getClass() == aj.getClass());
        assertEquals(ai, aj);
        ai.entrySet().stream().
                forEach(entry -> assertTrue(entry.getValue() != aj.get(entry.getKey())));
    }

    /**
     * Test of encode method, of class AtomDictionary.
     */
    @Test
    public void testEncode() {
        final AtomDictionary ai = new AtomDictionary();
        final Atom i = new AtomInteger(1);
        ai.put(new AtomString("foo"), i);
        final Atom s = new AtomString("Hello");
        ai.put(new AtomString("bar"), s);
        assertEquals(ai.encode(), "d3:bar5:Hello3:fooi1ee");
    }

    /**
     * Test of hashCode method, of class AtomDictionary.
     */
    @Test
    public void testHashCode() {
        LOGGER.info("hashCode");
        final AtomDictionary instance = new AtomDictionary();
        final int expected = 469;
        final int actual = instance.hashCode();
        assertEquals(expected, actual);
    }

    /**
     * Test of equals method, of class AtomDictionary.
     */
    @Test
    public void testEquals() {
        LOGGER.info("equals");
        final AtomDictionary ai = new AtomDictionary();
        final Atom i = new AtomInteger(1);
        ai.put(new AtomString("foo"), i);
        final Atom s = new AtomString("Hello");
        ai.put(new AtomString("bar"), s);
        final AtomDictionary ai1 = new AtomDictionary();
        final Atom i1 = new AtomInteger(1);
        ai1.put(new AtomString("foo"), i1);
        final Atom s1 = new AtomString("Hello");
        ai1.put(new AtomString("bar"), s1);
        final boolean result = ai.equals(ai1);
        assertTrue(result);
    }

    /**
     * Test of compareTo method, of class AtomDictionary.
     */
    @Test
    public void testCompareTo() {
        LOGGER.info("compareTo");
        final AtomDictionary ai = new AtomDictionary();
        final Atom i = new AtomInteger(1);
        ai.put(new AtomString("foo"), i);
        final Atom s = new AtomString("Hello");
        ai.put(new AtomString("bar"), s);
        final AtomDictionary ai1 = new AtomDictionary();
        final Atom i1 = new AtomInteger(1);
        ai1.put(new AtomString("foo"), i1);
        final Atom s1 = new AtomString("Hello");
        ai1.put(new AtomString("bar"), s1);
        final int expected = 0;
        final int actual = ai.compareTo(ai1);
        assertEquals(expected, actual);
    }
    
}
