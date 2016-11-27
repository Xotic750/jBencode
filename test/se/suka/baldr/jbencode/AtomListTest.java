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
    public AtomListTest() {
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
    public void testAtomListAddRemove() {
        AtomList ai = new AtomList();
        Atom i = new AtomInteger(1);
        ai.add(i);
        Atom s = new AtomString("Hello");
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
        Atom atomList = new AtomList(ai);
        assertEquals(atomList, ai);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorAtomSet() {
        AtomList ai = new AtomList();
        Atom ai1 = new AtomList(ai);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorAtomValue() {
        AtomList ai = new AtomList();
        Atom ai1 = new AtomList(ai);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmpty() {
        Atom atomList = new AtomList();
        assertEquals(atomList, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyBLength() {
        AtomList ai = new AtomList();
        Atom i = new AtomInteger(1);
        ai.add(i);
        Atom s = new AtomString("Hello");
        ai.add(s);
        assertEquals(ai.bLength(), 12);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyEquals() {
        AtomList ai = new AtomList();
        Atom i = new AtomInteger(1);
        ai.add(i);
        Atom s = new AtomString("Hello");
        ai.add(s);

        AtomList ai1 = new AtomList();
        Atom i1 = new AtomInteger(1);
        ai1.add(i1);
        Atom s1 = new AtomString("Hello");
        ai1.add(s1);

        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyLength() {
        AtomList ai = new AtomList();
        Atom i = new AtomInteger(1);
        ai.add(i);
        Atom s = new AtomString("Hello");
        ai.add(s);
        assertEquals(ai.size(), 2);
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyString() {
        AtomList ai = new AtomList();
        Atom i = new AtomInteger(1);
        ai.add(i);
        Atom s = new AtomString("Hello");
        ai.add(s);
        assertEquals(ai.toString(), "[1, Hello]");
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptytEncode() {
        AtomList ai = new AtomList();
        Atom i = new AtomInteger(1);
        ai.add(i);
        Atom s = new AtomString("Hello");
        ai.add(s);
        assertEquals(ai.encode(), "li1e5:Helloe");
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorEmptyValue() {
        Atom ai = new AtomList();
        assertEquals(ai, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorString() {
        Atom atomList = new AtomList();
        assertEquals(atomList, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorStringSet() {
        Atom ai = new AtomList();
        assertEquals(ai, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListConstructorStringValue() {
        Atom ai = new AtomList();
        assertEquals(ai, new AtomList());
    }

    /**
     *
     */
    @Test
    public void testAtomListCopyConstructor() {
        AtomList ai = new AtomList();
        Atom i = new AtomInteger(1);
        ai.add(i);
        Atom s = new AtomString("Hello");
        ai.add(s);

        AtomList ai1 = new AtomList();
        Atom i1 = new AtomInteger(1);
        ai1.add(i1);
        Atom s1 = new AtomString("Hello");
        ai1.add(s1);

        AtomList ai2 = new AtomList();
        ai2.add(ai);
        ai2.add(ai1);

        AtomList ai3 = new AtomList(ai2);

        Atom ai2first = ai2.iterator().next();
        Atom ai3first = ai3.iterator().next();
        assertTrue(ai == ai2first);
        assertTrue(ai == ai3first);
        assertEquals(ai2first, ai3first);
    }

    /**
     *
     */
    @Test
    public void testAtomListIterate() {
        AtomList ai = new AtomList();
        Atom i = new AtomInteger(1);
        ai.add(i);
        Atom s = new AtomString("Hello");
        ai.add(s);
        ai.forEach(atom -> assertTrue(atom instanceof Atom));
    }

    /**
     *
     */
    @Test
    public void testAtomListOrder() {
        AtomList ai = new AtomList();
        ai.add(new AtomString("foo"));
        ai.add(new AtomString("bar"));
        ai.add(new AtomString("boo"));
        ai.add(new AtomString("90"));
        ai.add(new AtomString("100"));
        ai.add(new AtomString("ABC90"));
        ai.add(new AtomString("ABC100"));
        String order = "";
        order = ai.stream().map(atom -> atom.toString()).reduce(order, String::concat);
        assertEquals(order, "foobarboo90100ABC90ABC100");
    }

    /**
     *
     */
    @Test
    public void testAtomListClone() {
        AtomList ai = new AtomList();
        ai.add(new AtomString("foo"));
        ai.add(new AtomString("bar"));
        ai.add(new AtomString("boo"));
        ai.add(new AtomString("90"));
        ai.add(new AtomString("100"));
        ai.add(new AtomString("ABC90"));
        ai.add(new AtomString("ABC100"));
        AtomList aj = ai.clone();
        assertTrue(ai != aj);
        assertTrue(ai.getClass() == aj.getClass());
        assertEquals(ai, aj);
    }

}
