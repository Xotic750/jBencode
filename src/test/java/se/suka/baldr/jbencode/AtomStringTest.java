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
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomStringTest {

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
    public AtomStringTest() {
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
    public void testAtomStringConstructorAtom() {
        AtomString ai = new AtomString("Hello");
        Atom atomString = new AtomString(ai);
        assertEquals(ai, atomString);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorAtomSet() {
        Atom ai = new AtomString("Hello");
        assertEquals(ai.toString(), "Hello");
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorAtomValue() {
        AtomString ai = new AtomString("Hello");
        Atom ai1 = new AtomString(ai);
        assertEquals(ai1.toString(), "Hello");
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorEmpty() {
        Atom atomString = new AtomString();
        assertEquals(new AtomString(), atomString);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorEmptyBLength() {
        Atom ai = new AtomString();
        assertEquals(ai.bLength(), 2);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorEmptyEquals() {
        Atom ai = new AtomString();
        Atom ai1 = new AtomString();
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorEmptyLength() {
        Atom ai = new AtomString();
        assertEquals(ai.toString().length(), 0);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorEmptyString() {
        Atom ai = new AtomString();
        assertEquals(ai.toString(), "");
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorEmptytEncode() {
        Atom ai = new AtomString();
        assertEquals(ai.encode(), "0:");
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorEmptyValue() {
        Atom ai = new AtomString();
        assertEquals(ai.toString(), "");
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorLongerStringBLength() {
        Atom ai = new AtomString("Hello World");
        assertEquals(ai.bLength(), 14);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorString() {
        Atom atomString = new AtomString("Hello");
        assertEquals(new AtomString("Hello"), atomString);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorStringBLength() {
        Atom ai = new AtomString("Hello");
        assertEquals(ai.bLength(), 7);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorStringEncode() {
        Atom ai = new AtomString("Hello");
        assertEquals(ai.encode(), "5:Hello");
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorStringEquals() {
        Atom ai = new AtomString("Hello");
        Atom ai1 = new AtomString("Hello");
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorStringLength() {
        Atom ai = new AtomString("Hello");
        assertEquals(ai.toString().length(), 5);
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorStringSet() {
        Atom ai = new AtomString("Hello");
        assertEquals(ai.toString(), "Hello");
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorStringString() {
        Atom ai = new AtomString("Hello");
        assertEquals(ai.toString(), "Hello");
    }

    /**
     *
     */
    @Test
    public void testAtomStringConstructorStringValue() {
        Atom ai = new AtomString("Hello");
        assertEquals(ai.toString(), "Hello");
    }

}
