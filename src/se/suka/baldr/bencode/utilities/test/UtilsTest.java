package se.suka.baldr.bencode.utilities.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static se.suka.baldr.bencode.utilities.Utils.findFirstNotOf;
import static se.suka.baldr.bencode.utilities.Utils.findFirstOf;
import static se.suka.baldr.bencode.utilities.Utils.readFileBytesToString;
import static se.suka.baldr.bencode.utilities.Utils.readFileLinesToString;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public class UtilsTest {

    @Test
    public void testFindFirstNotOfABC() {
        String s = "abc123def456";
        int i = findFirstNotOf(s, "abc");
        assertEquals(i, 3);
    }

    @Test
    public void testFindFirstNotOfABCStartindex() {
        String s = "abc123abc123";
        int i = findFirstNotOf(s, "abc", 6);
        assertEquals(i, 9);
    }

    @Test
    public void testFindFirstOf123Startindex() {
        String s = "abc123abc123";
        int i = findFirstNotOf(s, "abc", 6);
        assertEquals(i, 9);
    }

    @Test
    public void testFindFirstOfDEF() {
        String s = "abc123def456";
        int i = findFirstOf(s, "def");
        assertEquals(i, 6);
    }

    @Test
    public void testreadFileBytesToStringEmptyEmpty() {
        String s = readFileBytesToString("", "");
        assertNull(s);
    }

    @Test
    public void testreadFileBytesToStringEmptyNull() {
        String s = readFileBytesToString("", null);
        assertNull(s);
    }

    @Test
    public void testReadFileBytesToStringSample1() {
        String f = readFileBytesToString("bin/sample.torrent", "windows-1252");
        String s = "d8:announce35:udp://tracker.openbittorrent.com:8013:creation datei1327049827e4:infod6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1eee\r";
        assertEquals(f, s);
    }

    @Test
    public void testReadFileBytesToStringSample2() {
        String f = readFileBytesToString("bin/sample2.torrent", "windows-1252");
        assertNotNull(f);
    }

    @Test
    public void testreadFileLinesToStringEmpty() {
        String s = readFileLinesToString("");
        assertNull(s);
    }

    @Test
    public void testreadFileLinesToStringNull() {
        String s = readFileLinesToString(null);
        assertNull(s);
    }

    @Test
    public void testReadFileLinesToStringSample1() {
        String f = readFileLinesToString("bin/sample.torrent");
        String s = "d8:announce35:udp://tracker.openbittorrent.com:8013:creation datei1327049827e4:infod6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1eee";
        assertEquals(f, s);
    }

    @Test
    public void testReadFileLinesToStringSample2() {
        String f = readFileLinesToString("bin/sample2.torrent");
        assertNull(f);
    }

}
