package se.suka.baldr.bencode.models.test;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import se.suka.baldr.bencode.models.AtomString;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomStringTest {

    @Test
    public void testAtomStringConstructorAtom() {
        AtomString ai = new AtomString("Hello");
        AtomString atomString = new AtomString(ai);
        assertEquals(ai, atomString);
    }

    @Test
    public void testAtomStringConstructorAtomSet() {
        AtomString ai = new AtomString("Hello");
        assertEquals(ai.toString(), "Hello");
    }

    @Test
    public void testAtomStringConstructorAtomValue() {
        AtomString ai = new AtomString("Hello");
        AtomString ai1 = new AtomString(ai);
        assertEquals(ai1.toString(), "Hello");
    }

    @Test
    public void testAtomStringConstructorEmpty() {
        AtomString atomString = new AtomString();
        assertEquals(new AtomString(), atomString);
    }

    @Test
    public void testAtomStringConstructorEmptyBLength() {
        AtomString ai = new AtomString();
        assertEquals(ai.bLength(), 2);
    }

    @Test
    public void testAtomStringConstructorEmptyEquals() {
        AtomString ai = new AtomString();
        AtomString ai1 = new AtomString();
        assertEquals(ai, ai1);
    }

    @Test
    public void testAtomStringConstructorEmptyLength() {
        AtomString ai = new AtomString();
        assertEquals(ai.length(), 0);
    }

    @Test
    public void testAtomStringConstructorEmptyString() {
        AtomString ai = new AtomString();
        assertEquals(ai.toString(), "");
    }

    @Test
    public void testAtomStringConstructorEmptytEncode() {
        AtomString ai = new AtomString();
        assertEquals(ai.encode(), "0:");
    }

    @Test
    public void testAtomStringConstructorEmptyValue() {
        AtomString ai = new AtomString();
        assertEquals(ai.toString(), "");
    }

    @Test
    public void testAtomStringConstructorLongerStringBLength() {
        AtomString ai = new AtomString("Hello World");
        assertEquals(ai.bLength(), 14);
    }

    @Test
    public void testAtomStringConstructorString() {
        AtomString atomString = new AtomString("Hello");
        assertEquals(new AtomString("Hello"), atomString);
    }

    @Test
    public void testAtomStringConstructorStringBLength() {
        AtomString ai = new AtomString("Hello");
        assertEquals(ai.bLength(), 7);
    }

    @Test
    public void testAtomStringConstructorStringEncode() {
        AtomString ai = new AtomString("Hello");
        assertEquals(ai.encode(), "5:Hello");
    }

    @Test
    public void testAtomStringConstructorStringEquals() {
        AtomString ai = new AtomString("Hello");
        AtomString ai1 = new AtomString("Hello");
        assertEquals(ai, ai1);
    }

    @Test
    public void testAtomStringConstructorStringLength() {
        AtomString ai = new AtomString("Hello");
        assertEquals(ai.length(), 5);
    }

    @Test
    public void testAtomStringConstructorStringSet() {
        AtomString ai = new AtomString("Hello");
        assertEquals(ai.toString(), "Hello");
    }

    @Test
    public void testAtomStringConstructorStringString() {
        AtomString ai = new AtomString("Hello");
        assertEquals(ai.toString(), "Hello");
    }

    @Test
    public void testAtomStringConstructorStringValue() {
        AtomString ai = new AtomString("Hello");
        assertEquals(ai.toString(), "Hello");
    }

}
