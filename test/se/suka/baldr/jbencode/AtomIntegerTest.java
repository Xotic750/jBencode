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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomIntegerTest {

    /**
     *
     */
    public AtomIntegerTest() {
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
    public void testAtomIntegerConstructorEmpty() {
        Atom atomInteger = new AtomInteger();
        assertEquals(new AtomInteger(), atomInteger);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorEmptyBLength() {
        Atom ai = new AtomInteger();
        assertEquals(ai.bLength(), 3);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorEmptyEncode() {
        Atom ai = new AtomInteger();
        assertEquals(ai.encode(), "i0e");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorEmptyLength() {
        Atom ai = new AtomInteger();
        assertEquals(ai.toString().length(), 1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorEmptyString() {
        Atom ai = new AtomInteger();
        assertEquals(ai.toString(), "0");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorEmptyValue() {
        AtomInteger ai = new AtomInteger();
        assertEquals(ai.intValue(), 0);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeAtom() {
        AtomInteger ai = new AtomInteger(-1);
        Atom atomInteger = new AtomInteger(ai);
        assertEquals(ai, atomInteger);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeAtomBLength() {
        AtomInteger ai = new AtomInteger(-1);
        Atom ai1 = new AtomInteger(ai);
        assertEquals(ai1.bLength(), 4);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeAtomEncode() {
        AtomInteger ai = new AtomInteger(-1);
        Atom ai1 = new AtomInteger(ai);
        assertEquals(ai1.encode(), "i-1e");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeAtomLength() {
        AtomInteger ai = new AtomInteger(-1);
        Atom ai1 = new AtomInteger(ai);
        assertEquals(ai1.toString().length(), 2);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeAtomSet() {
        AtomInteger ai = new AtomInteger(-1);
        assertEquals(ai.intValue(), -1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeAtomString() {
        AtomInteger ai = new AtomInteger(-1);
        Atom ai1 = new AtomInteger(ai);
        assertEquals(ai1.toString(), "-1");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeAtomValue() {
        AtomInteger ai = new AtomInteger(-1);
        AtomInteger ai1 = new AtomInteger(ai);
        assertEquals(ai1.intValue(), -1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeEquals() {
        Atom ai = new AtomInteger(-1);
        Atom ai1 = new AtomInteger(-1);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeInt() {
        Atom atomInteger = new AtomInteger(-1);
        assertEquals(new AtomInteger(-1), atomInteger);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeIntBLength() {
        Atom ai = new AtomInteger(-1);
        assertEquals(ai.bLength(), 4);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeIntEncode() {
        Atom ai = new AtomInteger(-1);
        assertEquals(ai.encode(), "i-1e");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeIntLength() {
        Atom ai = new AtomInteger(-1);
        assertEquals(ai.toString().length(), 2);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeIntString() {
        Atom ai = new AtomInteger(-1);
        assertEquals(ai.toString(), "-1");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeIntValue() {
        AtomInteger ai = new AtomInteger(-1);
        assertEquals(ai.intValue(), -1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorNegativeNotEquals() {
        Atom ai = new AtomInteger(-1);
        Atom ai1 = new AtomInteger(-2);
        assertNotEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveAtom() {
        AtomInteger ai = new AtomInteger(1);
        Atom atomInteger = new AtomInteger(ai);
        assertEquals(ai, atomInteger);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveAtomBLength() {
        AtomInteger ai = new AtomInteger(1);
        Atom ai1 = new AtomInteger(ai);
        assertEquals(ai1.bLength(), 3);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveAtomEncode() {
        AtomInteger ai = new AtomInteger(1);
        Atom ai1 = new AtomInteger(ai);
        assertEquals(ai1.encode(), "i1e");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveAtomLength() {
        AtomInteger ai = new AtomInteger(1);
        Atom ai1 = new AtomInteger(ai);
        assertEquals(ai1.toString().length(), 1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveAtomSet() {
        AtomInteger ai = new AtomInteger(1);
        assertEquals(ai.intValue(), 1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveAtomString() {
        AtomInteger ai = new AtomInteger(1);
        Atom ai1 = new AtomInteger(ai);
        assertEquals(ai1.toString(), "1");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveAtomValue() {
        AtomInteger ai = new AtomInteger(1);
        AtomInteger ai1 = new AtomInteger(ai);
        assertEquals(ai1.intValue(), 1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveEquals() {
        Atom ai = new AtomInteger(1);
        Atom ai1 = new AtomInteger(1);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveInt() {
        Atom atomInteger = new AtomInteger(1);
        assertEquals(new AtomInteger(1), atomInteger);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveIntBLength() {
        Atom ai = new AtomInteger(1);
        assertEquals(ai.bLength(), 3);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveIntEncode() {
        Atom ai = new AtomInteger(1);
        assertEquals(ai.encode(), "i1e");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveIntLength() {
        Atom ai = new AtomInteger(1);
        assertEquals(ai.toString().length(), 1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveIntSet() {
        AtomInteger ai = new AtomInteger(1);
        assertEquals(ai.intValue(), 1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveIntString() {
        Atom ai = new AtomInteger(1);
        assertEquals(ai.toString(), "1");
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveIntValue() {
        AtomInteger ai = new AtomInteger(1);
        assertEquals(ai.intValue(), 1);
    }

    /**
     *
     */
    @Test
    public void testAtomIntegerConstructorPositiveNotEquals() {
        Atom ai = new AtomInteger(1);
        Atom ai1 = new AtomInteger(2);
        assertNotEquals(ai, ai1);
    }

}
