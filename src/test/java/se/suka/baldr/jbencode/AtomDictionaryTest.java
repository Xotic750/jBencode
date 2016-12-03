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

import java.util.Map;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomDictionaryTest {

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
     */
    @Test
    public void testAtomDictionaryConstructorAtom() {
        AtomDictionary ai = new AtomDictionary();
        Atom atomDictionary = new AtomDictionary(ai);
        assertEquals(ai, atomDictionary);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorAtomSet() {
        AtomDictionary ai = new AtomDictionary();
        Atom ai1 = new AtomDictionary(ai);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorAtomValue() {
        AtomDictionary ai = new AtomDictionary();
        Atom ai1 = new AtomDictionary(ai);
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorEmpty() {
        Atom atomDictionary = new AtomDictionary();
        assertEquals(new AtomDictionary(), atomDictionary);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorEmptyBLength() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        Atom s = new AtomString("Hello");
        ai.put("bar", s);
        assertEquals(ai.bLength(), 22);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorEmptyEncode() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        Atom s = new AtomString("Hello");
        ai.put("bar", s);
        assertEquals(ai.encode(), "d3:bar5:Hello3:fooi1ee");
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorEmptyEquals() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        Atom s = new AtomString("Hello");
        ai.put("bar", s);

        AtomDictionary ai1 = new AtomDictionary();
        Atom i1 = new AtomInteger(1);
        ai1.put("foo", i1);
        Atom s1 = new AtomString("Hello");
        ai1.put("bar", s1);

        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorEmptyLength() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        Atom s = new AtomString("Hello");
        ai.put("bar", s);
        assertEquals(ai.size(), 2);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorEmptyString() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        Atom s = new AtomString("Hello");
        ai.put("bar", s);
        assertEquals(ai.toString(), "{bar=Hello, foo=1}");
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorEmptyValue() {
        Atom ai = new AtomDictionary();
        assertEquals(ai, new AtomDictionary());
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorString() {
        Atom atomDictionary = new AtomDictionary();
        assertEquals(new AtomDictionary(), atomDictionary);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryConstructorStringValue() {
        Atom ai = new AtomDictionary();
        Atom ai1 = new AtomDictionary();
        assertEquals(ai, ai1);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryClear() {
        Atom ai = new AtomDictionary();
        AtomDictionary atomDictionary = new AtomDictionary();
        atomDictionary.put("first", ai);
        atomDictionary.clear();
        assertEquals(ai, atomDictionary);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryIterateKeys() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        Atom s = new AtomString("Hello");
        ai.put("bar", s);
        ai.keySet().stream().forEach(key -> assertTrue(key instanceof String));
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryIterateValues() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        Atom s = new AtomString("Hello");
        ai.put("bar", s);
        ai.values().stream().forEach(value -> assertTrue(value instanceof Atom));
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryOrder() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        ai.put("bar", i);
        ai.put("boo", i);
        ai.put("90", i);
        ai.put("100", i);
        ai.put("ABC90", i);
        ai.put("ABC100", i);
        String keysConcat = "";
        keysConcat = ai.keySet().stream().map(key -> key).reduce(keysConcat, String::concat);
        assertEquals(keysConcat, "10090ABC100ABC90barboofoo");
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryClone() {
        AtomDictionary ai = new AtomDictionary();
        Atom i = new AtomInteger(1);
        ai.put("foo", i);
        ai.put("bar", i);
        ai.put("boo", i);
        ai.put("90", i);
        ai.put("100", i);
        ai.put("ABC90", i);
        ai.put("ABC100", i);
        AtomDictionary aj = ai.clone();
        assertTrue(ai != aj);
        assertTrue(ai.getClass() == aj.getClass());
        assertEquals(ai, aj);
    }

    /**
     *
     */
    @Test
    public void testAtomDictionaryCopy() {
        AtomDictionary ai = new AtomDictionary();
        ai.put("foo", new AtomInteger(1));
        ai.put("bar", new AtomString("hello"));
        ai.put("boo", new AtomList());
        ai.put("far", new AtomDictionary());
        AtomDictionary aj = ai.copy();
        assertTrue(ai != aj);
        assertTrue(ai.getClass() == aj.getClass());
        assertEquals(ai, aj);
        ai.entrySet().stream().
                forEach(entry -> assertTrue(entry.getValue() != aj.get(entry.getKey())));
    }

}