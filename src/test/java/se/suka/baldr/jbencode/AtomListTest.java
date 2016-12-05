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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.concurrent.atomic.AtomicInteger;
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

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomListTest {

    private static final Logger LOGGER = getLogger(AtomListTest.class);

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
    public AtomListTest() {
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
     * Test the Constructor, of class AtomList.
     */
    @Test
    public void testAtomList() {
        LOGGER.info("testAtomInteger");
        AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        final AtomList actual = new AtomList(atomList);
        AtomList expected = new AtomList();
        expected.add(new AtomString("foo"));
        expected.add(new AtomString("bar"));
        expected.add(new AtomString("boo"));
        expected.add(new AtomString("90"));
        expected.add(new AtomString("100"));
        expected.add(new AtomString("ABC90"));
        expected.add(new AtomString("ABC100"));
        assertEquals(expected, actual);
    }

    /**
     * Test of add method, of class AtomList.
     */
    @Test
    public void testAdd_Atom() {
        LOGGER.info("add");
        final AtomList actual = new AtomList();
        final boolean result = actual.add(new AtomList());
        assertTrue(result);
        final AtomList expected = new AtomList();
        expected.add(new AtomList());
        assertEquals(expected, actual);
    }

    /**
     * Test of add method, of class AtomList.
     */
    @Test
    public void testAdd_int_Atom() {
        LOGGER.info("add at index");
        final int index = 0;
        final AtomList actual = new AtomList();
        actual.add(index, new AtomList());
        final AtomList expected = new AtomList();
        expected.add(new AtomList());
        assertEquals(expected, actual);
    }

    /**
     * Test of addAll method, of class AtomList.
     */
    @Test
    public void testAddAll_Collection() {
        LOGGER.info("addAll");
        final List<Atom> list = new ArrayList();
        list.add(new AtomInteger());
        list.add(new AtomString());
        list.add(new AtomList());
        list.add(new AtomDictionary());
        final Collection<? extends Atom> c = list;
        final AtomList actual = new AtomList();
        final boolean result = actual.addAll(c);
        assertTrue(result);
        final AtomList expected = new AtomList();
        expected.add(new AtomInteger());
        expected.add(new AtomString());
        expected.add(new AtomList());
        expected.add(new AtomDictionary());
        assertEquals(expected, actual);
    }

    /**
     * Test of addAll method, of class AtomList.
     */
    @Test
    public void testAddAll_int_Collection() {
        LOGGER.info("addAll at index");
        final List<Atom> list = new ArrayList();
        list.add(new AtomInteger());
        list.add(new AtomString());
        list.add(new AtomList());
        list.add(new AtomDictionary());
        final Collection<? extends Atom> c = list;
        final AtomList actual = new AtomList();
        actual.add(new AtomInteger());
        actual.add(new AtomString());
        final boolean result = actual.addAll(1, c);
        assertTrue(result);
        final AtomList expected = new AtomList();
        expected.add(new AtomInteger());
        expected.add(new AtomInteger());
        expected.add(new AtomString());
        expected.add(new AtomList());
        expected.add(new AtomDictionary());
        expected.add(new AtomString());
        assertEquals(expected, actual);
    }

    /**
     * Test of addAllAbsent method, of class AtomList.
     */
    @Test
    public void testAddAllAbsent() {
        LOGGER.info("addAllAbsent");
        final List<Atom> list = new ArrayList();
        list.add(new AtomInteger());
        list.add(new AtomString());
        list.add(new AtomList());
        list.add(new AtomDictionary());
        final Collection<? extends Atom> c = list;
        final AtomList actual = new AtomList();
        actual.add(new AtomList());
        actual.add(new AtomDictionary());
        final int result = actual.addAllAbsent(c);
        assertEquals(2, result);
        final AtomList expected = new AtomList();
        expected.add(new AtomList());
        expected.add(new AtomDictionary());
        expected.add(new AtomInteger());
        expected.add(new AtomString());
        assertEquals(expected, actual);
    }

    /**
     * Test of addIfAbsent method, of class AtomList.
     */
    @Test
    public void testAddIfAbsent() {
        LOGGER.info("addIfAbsent");
        final AtomList actual = new AtomList();
        actual.add(new AtomList());
        boolean result = actual.addIfAbsent(new AtomInteger());
        assertTrue(result);
        result = actual.addIfAbsent(new AtomInteger());
        assertFalse(result);
        final AtomList expected = new AtomList();
        expected.add(new AtomList());
        expected.add(new AtomInteger());
        assertEquals(expected, actual);
    }

    /**
     * Test of bLength method, of class AtomList.
     */
    @Test
    public void testBLength() {
        LOGGER.info("bLength");
        final AtomList atomList = new AtomList();
        int actual = atomList.bLength();
        int expected = 2;
        assertEquals(expected, actual);
        atomList.add(new AtomList());
        actual = atomList.bLength();
        expected = 4;
        assertEquals(expected, actual);
    }

    /**
     * Test of clear method, of class AtomList.
     */
    @Test
    public void testClear() {
        LOGGER.info("clear");
        final AtomList actual = new AtomList();
        actual.add(new AtomList());
        actual.add(new AtomDictionary());
        actual.add(new AtomInteger());
        actual.add(new AtomString());
        final AtomList expected = new AtomList();
        assertNotEquals(expected, actual);
        actual.clear();
        assertEquals(expected, actual);
    }

    /**
     * Test element oder, of class AtomList.
     */
    @Test
    public void testAtomListOrder() {
        LOGGER.info("AtomList order");
        AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        final String actual = atomList.stream()
                .map(atom -> atom.toString())
                .collect(joining());
        final String expected = "foobarboo90100ABC90ABC100";
        assertEquals(expected, actual);
    }

    /**
     * Test of clone method, of class AtomList.
     */
    @Test
    public void testClone() {
        LOGGER.info("clone");
        final AtomList atomList1 = new AtomList();
        atomList1.add(new AtomString("foo"));
        atomList1.add(new AtomString("bar"));
        atomList1.add(new AtomString("boo"));
        atomList1.add(new AtomString("90"));
        atomList1.add(new AtomString("100"));
        atomList1.add(new AtomString("ABC90"));
        atomList1.add(new AtomString("ABC100"));
        final AtomList atomList2 = atomList1.clone();
        assertTrue(atomList1 != atomList2);
        assertTrue(atomList1.getClass() == atomList2.getClass());
        assertEquals(atomList1, atomList2);
    }

    /**
     * Test of contains method, of class AtomList.
     */
    @Test
    public void testContains() {
        LOGGER.info("contains");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        boolean actual = atomList.contains(new AtomString());
        assertFalse(actual);
        actual = atomList.contains(new AtomString("boo"));
        assertTrue(actual);
    }

    /**
     * Test of containsAll method, of class AtomList.
     */
    @Test
    public void testContainsAll() {
        LOGGER.info("containsAll");
        final Collection c = new ArrayList();
        c.add(new AtomString("boo"));
        c.add(new AtomString("90"));
        c.add(new AtomString("100"));
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        boolean actual = atomList.containsAll(c);
        assertTrue(actual);
        c.add(new AtomString());
        actual = atomList.containsAll(c);
        assertFalse(actual);
    }

    /**
     * Test of copy method, of class AtomList.
     */
    @Test
    public void testCopy() {
        LOGGER.info("copy");
        final AtomList atomList1 = new AtomList();
        final Atom atomInteger1 = new AtomInteger(1);
        atomList1.add(atomInteger1);
        final Atom atomString1 = new AtomString("Hello");
        atomList1.add(atomString1);
        final AtomList atomList2 = atomList1.copy();
        final Atom atom1 = atomList1.iterator().next();
        final Atom atom2 = atomList2.iterator().next();
        assertTrue(atomInteger1 == atom1);
        assertTrue(atomInteger1 != atom2);
        assertTrue(atom1 != atom2);
        assertEquals(atom1, atom2);
    }

    /**
     * Test of encode method, of class AtomList.
     */
    @Test
    public void testEncode() {
        LOGGER.info("encode");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomInteger(1));
        atomList.add(new AtomString("Hej"));
        final String actual = atomList.encode();
        final String expected = "li1e3:Heje";
        assertEquals(expected, actual);
    }

    /**
     * Test of forEach method, of class AtomList.
     */
    @Test
    public void testForEach() {
        LOGGER.info("forEach");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        final String[] words = new String[]{"foo", "bar", "boo"};
        final AtomicInteger index = new AtomicInteger();
        atomList.forEach(atom -> assertEquals(atom.toString(), words[index.getAndIncrement()]));
    }

    /**
     * Test of get method, of class AtomList.
     */
    @Test
    public void testGet() {
        LOGGER.info("get");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        final AtomString expected = new AtomString("boo");
        atomList.add(expected);
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        final int index = 2;
        final Atom actual = atomList.get(index);
        assertEquals(expected, actual);
    }

    /**
     * Test of indexOf method, of class AtomList.
     */
    @Test
    public void testIndexOf_Object() {
        LOGGER.info("indexOf");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        int expected = -1;
        int actual = atomList.indexOf(new AtomString());
        assertEquals(expected, actual);
        expected = 2;
        actual = atomList.indexOf(new AtomString("boo"));
        assertEquals(expected, actual);
    }

    /**
     * Test of indexOf method, of class AtomList.
     */
    @Test
    public void testIndexOf_Atom_int() {
        LOGGER.info("indexOf with start index");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        atomList.add(new AtomString("boo"));
        int expected = -1;
        int actual = atomList.indexOf(new AtomString(), 0);
        assertEquals(expected, actual);
        expected = 7;
        actual = atomList.indexOf(new AtomString("boo"), 3);
        assertEquals(expected, actual);
    }

    /**
     * Test of isEmpty method, of class AtomList.
     */
    @Test
    public void testIsEmpty() {
        LOGGER.info("isEmpty");
        final AtomList atomList = new AtomList();
        boolean result = atomList.isEmpty();
        assertTrue(result);
        atomList.add(new AtomList());
        result = atomList.isEmpty();
        assertFalse(result);
    }

    /**
     * Test of iterator method, of class AtomList.
     */
    @Test
    public void testIterator() {
        LOGGER.info("iterator");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        int index = 0;
        for (final Iterator<Atom> it = atomList.iterator(); it.hasNext();) {
            assertEquals(it.next(), atomList.get(index++));
        }
        final int expected = 3;
        assertEquals(expected, index);
    }

    /**
     * Test of lastIndexOf method, of class AtomList.
     */
    @Test
    public void testLastIndexOf_Object() {
        LOGGER.info("lastIndexOf");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        int expected = -1;
        int actual = atomList.lastIndexOf(new AtomString());
        assertEquals(expected, actual);
        expected = 2;
        actual = atomList.lastIndexOf(new AtomString("boo"));
        assertEquals(expected, actual);
    }

    /**
     * Test of lastIndexOf method, of class AtomList.
     */
    @Test
    public void testLastIndexOf_Atom_int() {
        LOGGER.info("lastIndexOf with start index");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        atomList.add(new AtomString("boo"));
        int expected = -1;
        int actual = atomList.lastIndexOf(new AtomString(), 0);
        assertEquals(expected, actual);
        expected = 0;
        actual = atomList.lastIndexOf(new AtomString("boo"), 2);
        assertEquals(expected, actual);
    }

    /**
     * Test of listIterator method, of class AtomList.
     */
    @Test
    public void testListIterator_0args() {
        LOGGER.info("listIterator");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        int index = 0;
        for (final ListIterator<Atom> it = atomList.listIterator(); it.hasNext();) {
            assertEquals(it.next(), atomList.get(index++));
        }
        final int expected = 3;
        assertEquals(expected, index);
    }

    /**
     * Test of listIterator method, of class AtomList.
     */
    @Test
    public void testListIterator_int() {
        LOGGER.info("listIterator with index");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        int index = 3;
        for (final ListIterator<Atom> it = atomList.listIterator(index); it.hasPrevious();) {
            assertEquals(it.previous(), atomList.get(--index));
        }
        final int expected = 0;
        assertEquals(expected, index);
    }

    /**
     * Test of getRandomSlice method, of class AtomList.
     */
    @Test
    public void testGetRandomSlice() {
        LOGGER.info("getRandomSlice");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        final int howMany = 2;
        final AtomList actual = atomList.getRandomSlice(howMany);
        assertEquals(howMany, actual.size());
        final boolean result = atomList.containsAll(actual);
        assertTrue(result);
    }

    /**
     * Test of remove method, of class AtomList.
     */
    @Test
    public void testRemove_int() {
        LOGGER.info("remove by index");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        final AtomString expected = new AtomString("boo");
        atomList.add(expected);
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        final int index = 2;
        final Atom actual = atomList.remove(index);
        assertEquals(expected, actual);

    }

    /**
     * Test of remove method, of class AtomList.
     */
    @Test
    public void testRemove_Object() {
        LOGGER.info("remove");
        final AtomList actual = new AtomList();
        actual.add(new AtomString("foo"));
        actual.add(new AtomString("bar"));
        actual.add(new AtomString("boo"));
        actual.add(new AtomString("90"));
        actual.add(new AtomString("100"));
        actual.add(new AtomString("ABC90"));
        actual.add(new AtomString("ABC100"));
        final boolean result = actual.remove(new AtomString("boo"));
        assertTrue(result);
        final AtomList expected = new AtomList();
        expected.add(new AtomString("foo"));
        expected.add(new AtomString("bar"));
        expected.add(new AtomString("90"));
        expected.add(new AtomString("100"));
        expected.add(new AtomString("ABC90"));
        expected.add(new AtomString("ABC100"));
        assertEquals(expected, actual);
    }

    /**
     * Test of removeAll method, of class AtomList.
     */
    @Test
    public void testRemoveAll() {
        LOGGER.info("removeAll");
        final AtomList actual = new AtomList();
        actual.add(new AtomString("foo"));
        actual.add(new AtomString("bar"));
        actual.add(new AtomString("boo"));
        actual.add(new AtomString("90"));
        actual.add(new AtomString("100"));
        actual.add(new AtomString("ABC90"));
        actual.add(new AtomString("ABC100"));
        Collection c = new AtomList();
        c.add(new AtomString("100"));
        c.add(new AtomString("ABC90"));
        c.add(new AtomString("ABC100"));
        final boolean result = actual.removeAll(c);
        assertTrue(result);
        final AtomList expected = new AtomList();
        expected.add(new AtomString("foo"));
        expected.add(new AtomString("bar"));
        expected.add(new AtomString("boo"));
        expected.add(new AtomString("90"));
        assertEquals(expected, actual);
    }

    /**
     * Test of removeIf method, of class AtomList.
     */
    @Test
    public void testRemoveIf() {
        LOGGER.info("removeIf");
        final AtomList actual = new AtomList();
        actual.add(new AtomString("foo"));
        actual.add(new AtomString("bar"));
        actual.add(new AtomString("boo"));
        actual.add(new AtomString("90"));
        actual.add(new AtomString("100"));
        actual.add(new AtomString("ABC90"));
        actual.add(new AtomString("ABC100"));
        final boolean result = actual.removeIf(atom -> atom.toString().contains("0"));
        assertTrue(result);
        final AtomList expect = new AtomList();
        expect.add(new AtomString("foo"));
        expect.add(new AtomString("bar"));
        expect.add(new AtomString("boo"));
        assertEquals(expect, actual);
    }

    /**
     * Test of replaceAll method, of class AtomList.
     */
    @Test
    public void testReplaceAll() {
        LOGGER.info("replaceAll");
        final AtomList actual = new AtomList();
        actual.add(new AtomString("foo"));
        actual.add(new AtomString("bar"));
        actual.add(new AtomString("boo"));
        actual.add(new AtomString("90"));
        actual.add(new AtomString("100"));
        actual.add(new AtomString("ABC90"));
        actual.add(new AtomString("ABC100"));
        actual.replaceAll(atom -> new AtomString("replaced"));
        final AtomList expected = new AtomList();
        expected.add(new AtomString("replaced"));
        expected.add(new AtomString("replaced"));
        expected.add(new AtomString("replaced"));
        expected.add(new AtomString("replaced"));
        expected.add(new AtomString("replaced"));
        expected.add(new AtomString("replaced"));
        expected.add(new AtomString("replaced"));
        assertEquals(expected, actual);
    }

    /**
     * Test of retainAll method, of class AtomList.
     */
    @Test
    public void testRetainAll() {
        LOGGER.info("retainAll");
        final Collection c = new AtomList();
        c.add(new AtomString("foo"));
        c.add(new AtomString("bar"));
        c.add(new AtomString("boo"));
        final AtomList actual = new AtomList();
        actual.add(new AtomString("foo"));
        actual.add(new AtomString("bar"));
        actual.add(new AtomString("boo"));
        actual.add(new AtomString("90"));
        actual.add(new AtomString("100"));
        actual.add(new AtomString("ABC90"));
        actual.add(new AtomString("ABC100"));
        final boolean result = actual.retainAll(c);
        assertTrue(result);
        final AtomList expected = new AtomList();
        expected.add(new AtomString("foo"));
        expected.add(new AtomString("bar"));
        expected.add(new AtomString("boo"));
        assertEquals(expected, actual);
    }

    /**
     * Test of set method, of class AtomList.
     */
    @Test
    public void testSet() {
        LOGGER.info("set");
        final AtomList actual = new AtomList();
        actual.add(new AtomString("foo"));
        actual.add(new AtomString("bar"));
        actual.add(new AtomString("boo"));
        final Atom ninety = new AtomString("90");
        actual.add(ninety);
        actual.add(new AtomString("100"));
        actual.add(new AtomString("ABC90"));
        actual.add(new AtomString("ABC100"));
        final int index = 3;
        final Atom atom = new AtomString("far");
        final Atom result = actual.set(index, atom);
        assertEquals(ninety, result);
        final AtomList expected = new AtomList();
        expected.add(new AtomString("foo"));
        expected.add(new AtomString("bar"));
        expected.add(new AtomString("boo"));
        expected.add(new AtomString("far"));
        expected.add(new AtomString("100"));
        expected.add(new AtomString("ABC90"));
        expected.add(new AtomString("ABC100"));
        assertEquals(expected, actual);
    }

    /**
     * Test of size method, of class AtomList.
     */
    @Test
    public void testSize() {
        LOGGER.info("size");
        final AtomList atomList = new AtomList();
        int expected = 0;
        int result = atomList.size();
        assertEquals(expected, result);
        expected = 1;
        atomList.add(new AtomList());
        result = atomList.size();
        assertEquals(expected, result);
    }

    /**
     * Test of sort method, of class AtomList.
     */
    @Test
    public void testSort() {
        LOGGER.info("sort");
        final AtomList actual = new AtomList();
        actual.add(new AtomString("foo"));
        actual.add(new AtomString("bar"));
        actual.add(new AtomString("boo"));
        actual.add(new AtomString("90"));
        actual.add(new AtomString("100"));
        actual.add(new AtomString("ABC90"));
        actual.add(new AtomString("ABC100"));
        Comparator<? super Atom> c = null;
        actual.sort(c);
        final AtomList expected = new AtomList();
        expected.add(new AtomString("100"));
        expected.add(new AtomString("90"));
        expected.add(new AtomString("ABC100"));
        expected.add(new AtomString("ABC90"));
        expected.add(new AtomString("bar"));
        expected.add(new AtomString("boo"));
        expected.add(new AtomString("foo"));
        assertEquals(expected, actual);
    }

    /**
     * Test of spliterator method, of class AtomList.
     */
    @Test
    public void testSpliterator() {
        LOGGER.info("spliterator");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("ABC100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("foo"));
        final Spliterator<Atom> spliterator = atomList.spliterator();
        final AtomicInteger index = new AtomicInteger();
        spliterator.forEachRemaining(atom -> atomList.get(index.getAndIncrement()).equals(atom));
        assertEquals(atomList.size(), index.get());
    }

    /**
     * Test of subList method, of class AtomList.
     */
    @Test
    public void testSubList() {
        LOGGER.info("subList");
        final AtomList atomList1 = new AtomList();
        atomList1.add(new AtomString("foo"));
        atomList1.add(new AtomString("bar"));
        atomList1.add(new AtomString("boo"));
        atomList1.add(new AtomString("90"));
        atomList1.add(new AtomString("100"));
        atomList1.add(new AtomString("ABC90"));
        atomList1.add(new AtomString("ABC100"));
        final List<Atom> expected = new ArrayList();
        expected.add(new AtomString("boo"));
        expected.add(new AtomString("90"));
        final int fromIndex = 2;
        final int toIndex = 4;
        final List<Atom> actual = atomList1.subList(fromIndex, toIndex);
        assertEquals(expected, actual);
    }

    /**
     * Test of toArray method, of class AtomList.
     */
    @Test
    public void testToArray_0args() {
        LOGGER.info("toArray");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        final Object[] expected = new Object[]{
            new AtomString("foo"),
            new AtomString("bar"),
            new AtomString("boo"),
            new AtomString("90"),
            new AtomString("100"),
            new AtomString("ABC90"),
            new AtomString("ABC100")
        };
        final Object[] actual = atomList.toArray();
        assertArrayEquals(expected, actual);
    }

    /**
     * Test of toArray method, of class AtomList.
     */
    @Test
    public void testToArray_GenericType() {
        LOGGER.info("toArray");
        final AtomList atomList = new AtomList();
        atomList.add(new AtomString("foo"));
        atomList.add(new AtomString("bar"));
        atomList.add(new AtomString("boo"));
        atomList.add(new AtomString("90"));
        atomList.add(new AtomString("100"));
        atomList.add(new AtomString("ABC90"));
        atomList.add(new AtomString("ABC100"));
        final Object[] expected = new Object[]{
            new AtomString("foo"),
            new AtomString("bar"),
            new AtomString("boo"),
            new AtomString("90"),
            new AtomString("100"),
            new AtomString("ABC90"),
            new AtomString("ABC100")
        };
        final Object[] a = new Object[atomList.size()];
        final Object[] actual = atomList.toArray(a);
        assertArrayEquals(expected, actual);
        assertArrayEquals(expected, a);
    }

    /**
     * Test of hashCode method, of class AtomList.
     */
    @Test
    public void testHashCode() {
        LOGGER.info("hashCode");
        final AtomList atomList = new AtomList();
        final int expected = 120;
        final int actual = atomList.hashCode();
        assertEquals(expected, actual);
    }

    /**
     * Test of equals method, of class AtomList.
     */
    @Test
    public void testEquals() {
        LOGGER.info("equals");
        final AtomList atomList1 = new AtomList();
        atomList1.add(new AtomString("foo"));
        atomList1.add(new AtomString("bar"));
        atomList1.add(new AtomString("boo"));
        atomList1.add(new AtomString("90"));
        atomList1.add(new AtomString("100"));
        atomList1.add(new AtomString("ABC90"));
        final AtomList atomList2 = new AtomList();
        atomList2.add(new AtomString("foo"));
        atomList2.add(new AtomString("bar"));
        atomList2.add(new AtomString("boo"));
        atomList2.add(new AtomString("90"));
        atomList2.add(new AtomString("100"));
        atomList2.add(new AtomString("ABC90"));
        atomList2.add(new AtomString("ABC100"));
        assertFalse(atomList2.equals(atomList1));
        atomList1.add(new AtomString("ABC100"));
        assertTrue(atomList1.equals(atomList1));
        assertTrue(atomList2.equals(atomList1));
        final Atom atomInteger = new AtomInteger();
        assertFalse(atomList2.equals(atomInteger));
        final AtomList atomList3 = null;
        assertFalse(atomList2.equals(atomList3));
    }

    /**
     * Test of toString method, of class AtomList.
     */
    @Test
    public void testToString() {
        LOGGER.info("toString");
        final AtomList atomList = new AtomList();
        final String expected = "[]";
        final String actual = atomList.toString();
        assertEquals(expected, actual);
    }

    /**
     * Test of compareTo method, of class AtomList.
     */
    @Test
    public void testCompareTo() {
        LOGGER.info("compareTo");
        final AtomList atomList1 = new AtomList();
        atomList1.add(new AtomString("foo"));
        atomList1.add(new AtomString("bar"));
        atomList1.add(new AtomString("boo"));
        atomList1.add(new AtomString("90"));
        atomList1.add(new AtomString("100"));
        atomList1.add(new AtomString("ABC90"));
        atomList1.add(new AtomString("ABC100"));
        final AtomList atomList2 = new AtomList();
        atomList2.add(new AtomString("foo"));
        atomList2.add(new AtomString("bar"));
        atomList2.add(new AtomString("boo"));
        atomList2.add(new AtomString("90"));
        atomList2.add(new AtomString("100"));
        atomList2.add(new AtomString("ABC90"));
        int expected = -47;
        int result = atomList1.compareTo(atomList2);
        assertEquals(expected, result);
        atomList2.add(new AtomString("ABC100"));
        expected = 0;
        result = atomList1.compareTo(atomList2);
        assertEquals(expected, result);
        atomList2.add(new AtomString("Whoop"));
        expected = 48;
        result = atomList1.compareTo(atomList2);
        assertEquals(expected, result);
    }

}
