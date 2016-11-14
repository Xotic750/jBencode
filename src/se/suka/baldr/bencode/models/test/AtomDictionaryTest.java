package se.suka.baldr.bencode.models.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import se.suka.baldr.bencode.models.Atom;
import se.suka.baldr.bencode.models.AtomDictionary;
import se.suka.baldr.bencode.models.AtomInteger;
import se.suka.baldr.bencode.models.AtomString;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomDictionaryTest {

    @Test
    public void testAtomDictionaryConstructorAtom() {
        AtomDictionary ai = new AtomDictionary();
        AtomDictionary atomDictionary = new AtomDictionary(ai);
        assertEquals(ai, atomDictionary);
    }

    @Test
    public void testAtomDictionaryConstructorAtomSet() {
        AtomDictionary ai = new AtomDictionary();
        AtomDictionary ai1 = new AtomDictionary(ai);
        assertEquals(ai, ai1);
    }

    @Test
    public void testAtomDictionaryConstructorAtomValue() {
        AtomDictionary ai = new AtomDictionary();
        AtomDictionary ai1 = new AtomDictionary(ai);
        assertEquals(ai, ai1);
    }

    @Test
    public void testAtomDictionaryConstructorEmpty() {
        AtomDictionary atomDictionary = new AtomDictionary();
        assertEquals(new AtomDictionary(), atomDictionary);
    }

    @Test
    public void testAtomDictionaryConstructorEmptyBLength() {
        AtomDictionary ai = new AtomDictionary();
        AtomInteger i = new AtomInteger(1);
        ai.put("foo", i);
        AtomString s = new AtomString("Hello");
        ai.put("bar", s);
        assertEquals(ai.bLength(), 22);
    }

    @Test
    public void testAtomDictionaryConstructorEmptyEncode() {
        AtomDictionary ai = new AtomDictionary();
        AtomInteger i = new AtomInteger(1);
        ai.put("foo", i);
        AtomString s = new AtomString("Hello");
        ai.put("bar", s);
        assertEquals(ai.encode(), "d3:bar5:Hello3:fooi1ee");
    }

    @Test
    public void testAtomDictionaryConstructorEmptyEquals() {
        AtomDictionary ai = new AtomDictionary();
        AtomInteger i = new AtomInteger(1);
        ai.put("foo", i);
        AtomString s = new AtomString("Hello");
        ai.put("bar", s);

        AtomDictionary ai1 = new AtomDictionary();
        AtomInteger i1 = new AtomInteger(1);
        ai1.put("foo", i1);
        AtomString s1 = new AtomString("Hello");
        ai1.put("bar", s1);

        assertEquals(ai, ai1);
    }

    @Test
    public void testAtomDictionaryConstructorEmptyLength() {
        AtomDictionary ai = new AtomDictionary();
        AtomInteger i = new AtomInteger(1);
        ai.put("foo", i);
        AtomString s = new AtomString("Hello");
        ai.put("bar", s);
        assertEquals(ai.size(), 2);
    }

    @Test
    public void testAtomDictionaryConstructorEmptyString() {
        AtomDictionary ai = new AtomDictionary();
        AtomInteger i = new AtomInteger(1);
        ai.put("foo", i);
        AtomString s = new AtomString("Hello");
        ai.put("bar", s);
        assertEquals(ai.toString(), "{bar=Hello, foo=1}");
    }

    @Test
    public void testAtomDictionaryConstructorEmptyValue() {
        AtomDictionary ai = new AtomDictionary();
        assertEquals(ai, new AtomDictionary());
    }

    @Test
    public void testAtomDictionaryConstructorString() {
        AtomDictionary atomDictionary = new AtomDictionary();
        assertEquals(new AtomDictionary(), atomDictionary);
    }

    @Test
    public void testAtomDictionaryConstructorStringValue() {
        AtomDictionary ai = new AtomDictionary();
        AtomDictionary ai1 = new AtomDictionary();
        assertEquals(ai, ai1);
    }

    @Test
    public void testAtomDictionaryIterateKeys() {
        AtomDictionary ai = new AtomDictionary();
        AtomInteger i = new AtomInteger(1);
        ai.put("foo", i);
        AtomString s = new AtomString("Hello");
        ai.put("bar", s);
        ai.keySet().forEach((key) -> {
            assertTrue(key instanceof String);
        });
    }

    @Test
    public void testAtomDictionaryIterateValues() {
        AtomDictionary ai = new AtomDictionary();
        AtomInteger i = new AtomInteger(1);
        ai.put("foo", i);
        AtomString s = new AtomString("Hello");
        ai.put("bar", s);
        ai.values().forEach((value) -> {
            assertTrue(value instanceof Atom);
        });
    }

    @Test
    public void testAtomDictionaryOrder() {
        AtomDictionary ai = new AtomDictionary();
        AtomInteger i = new AtomInteger(1);
        ai.put("foo", i);
        ai.put("bar", i);
        ai.put("boo", i);
        ai.put("90", i);
        ai.put("100", i);
        ai.put("ABC90", i);
        ai.put("ABC100", i);
        String keysConcat = "";
        keysConcat = ai.keySet().stream().map((key) -> key).reduce(keysConcat, String::concat);
        assertEquals(keysConcat, "10090ABC100ABC90barboofoo");
    }

}
