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
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomListTest {

    /**
     *
     */
    public AtomListTest() {
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
    public void testAtomListClear() {
        AtomList ai = new AtomList();
        AtomInteger i = new AtomInteger(1);
        ai.add(i);
        AtomString s = new AtomString("Hello");
        ai.add(s);
        ai.remove(i);
        assertEquals(ai.toString(), "[Hello]");
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorAtom() {
        AtomList ai = new AtomList();
        AtomList atomList = new AtomList(ai);
        assertEquals(atomList, ai);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorAtomSet() {
        AtomList ai = new AtomList();
        AtomList ai1 = new AtomList(ai);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorAtomValue() {
        AtomList ai = new AtomList();
        AtomList ai1 = new AtomList(ai);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmpty() {
        AtomList atomList = new AtomList();
        assertEquals(atomList, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyBLength() {
        AtomList ai = new AtomList();
        AtomInteger i = new AtomInteger(1);
        ai.add(i);
        AtomString s = new AtomString("Hello");
        ai.add(s);
        assertEquals(ai.bLength(), 12);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyEquals() {
        AtomList ai = new AtomList();
        AtomInteger i = new AtomInteger(1);
        ai.add(i);
        AtomString s = new AtomString("Hello");
        ai.add(s);

        AtomList ai1 = new AtomList();
        AtomInteger i1 = new AtomInteger(1);
        ai1.add(i1);
        AtomString s1 = new AtomString("Hello");
        ai1.add(s1);

        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyLength() {
        AtomList ai = new AtomList();
        AtomInteger i = new AtomInteger(1);
        ai.add(i);
        AtomString s = new AtomString("Hello");
        ai.add(s);
        assertEquals(ai.size(), 2);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyString() {
        AtomList ai = new AtomList();
        AtomInteger i = new AtomInteger(1);
        ai.add(i);
        AtomString s = new AtomString("Hello");
        ai.add(s);
        assertEquals(ai.toString(), "[1, Hello]");
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptytEncode() {
        AtomList ai = new AtomList();
        AtomInteger i = new AtomInteger(1);
        ai.add(i);
        AtomString s = new AtomString("Hello");
        ai.add(s);
        assertEquals(ai.encode(), "li1e5:Helloe");
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyValue() {
        AtomList ai = new AtomList();
        assertEquals(ai, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorString() {
        AtomList atomList = new AtomList();
        assertEquals(atomList, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorStringSet() {
        AtomList ai = new AtomList();
        assertEquals(ai, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorStringValue() {
        AtomList ai = new AtomList();
        assertEquals(ai, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListCopyConstructor() {
        AtomList ai = new AtomList();
        AtomInteger i = new AtomInteger(1);
        ai.add(i);
        AtomString s = new AtomString("Hello");
        ai.add(s);

        AtomList ai1 = new AtomList();
        AtomInteger i1 = new AtomInteger(1);
        ai1.add(i1);
        AtomString s1 = new AtomString("Hello");
        ai1.add(s1);

        AtomList ai2 = new AtomList();
        ai2.add(ai);
        ai2.add(ai1);

        AtomList ai3 = new AtomList(ai2);

        Atom<?> ai2first = ai2.iterator().next();
        Atom<?> ai3first = ai3.iterator().next();
        assertTrue(ai == ai2first);
        assertTrue(ai != ai3first);
        assertEquals(ai2first, ai3first);
    }

    /**
     *
     */
    @Test
    public void testAtomListIterate() {
        AtomList ai = new AtomList();
        AtomInteger i = new AtomInteger(1);
        ai.add(i);
        AtomString s = new AtomString("Hello");
        ai.add(s);
        for (Atom<?> atom : ai) {
            assertTrue(atom instanceof Atom);
        }
    }

    /**
     *
     */
    @Test
    public void testAtomOrder() {
        AtomList ai = new AtomList();
        ai.add(new AtomString("foo"));
        ai.add(new AtomString("bar"));
        ai.add(new AtomString("boo"));
        ai.add(new AtomString("90"));
        ai.add(new AtomString("100"));
        ai.add(new AtomString("ABC90"));
        ai.add(new AtomString("ABC100"));
        String order = "";
        for (Atom<?> atom : ai) {
            order += atom.toString();
        }
        assertEquals(order, "foobarboo90100ABC90ABC100");
    }

    /**
     *
     */
    @Test
    public void testAtomRandomise() {
        AtomList list1 = new AtomList();
        list1.add(new AtomString("foo"));
        list1.add(new AtomString("bar"));
        list1.add(new AtomString("boo"));
        list1.add(new AtomString("90"));
        list1.add(new AtomString("100"));
        list1.add(new AtomString("ABC90"));
        list1.add(new AtomString("ABC100"));

        AtomList list2 = new AtomList(list1);
        list2.randomise();
        assertEquals(list1.bLength(), list2.bLength());
        assertEquals(list1.toString(), "[foo, bar, boo, 90, 100, ABC90, ABC100]");
        assertNotEquals(list1, list2);
    }
}
