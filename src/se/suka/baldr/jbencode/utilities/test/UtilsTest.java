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
package se.suka.baldr.jbencode.utilities.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import static se.suka.baldr.jbencode.utilities.Utils.findFirstNotOf;
import static se.suka.baldr.jbencode.utilities.Utils.findFirstOf;
import static se.suka.baldr.jbencode.utilities.Utils.readFileBytesToString;
import static se.suka.baldr.jbencode.utilities.Utils.readFileLinesToString;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public class UtilsTest {

    /**
     *
     */
    @Test
    public void testFindFirstNotOfABC() {
        String s = "abc123def456";
        int i = findFirstNotOf(s, "abc");
        assertEquals(i, 3);
    }

    /**
     *
     */
    @Test
    public void testFindFirstNotOfABCStartindex() {
        String s = "abc123abc123";
        int i = findFirstNotOf(s, "abc", 6);
        assertEquals(i, 9);
    }

    /**
     *
     */
    @Test
    public void testFindFirstOf123Startindex() {
        String s = "abc123abc123";
        int i = findFirstNotOf(s, "abc", 6);
        assertEquals(i, 9);
    }

    /**
     *
     */
    @Test
    public void testFindFirstOfDEF() {
        String s = "abc123def456";
        int i = findFirstOf(s, "def");
        assertEquals(i, 6);
    }

    /**
     *
     */
    @Test
    public void testreadFileBytesToStringEmptyEmpty() {
        String s = readFileBytesToString("", "");
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testreadFileBytesToStringEmptyNull() {
        String s = readFileBytesToString("", null);
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testReadFileBytesToStringSample1() {
        String f = readFileBytesToString("bin/sample.torrent", "windows-1252");
        String s = "d8:announce35:udp://tracker.openbittorrent.com:8013:creation datei1327049827e4:infod6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1eee\r";
        assertEquals(f, s);
    }

    /**
     *
     */
    @Test
    public void testReadFileBytesToStringSample2() {
        String f = readFileBytesToString("bin/sample2.torrent", "windows-1252");
        assertNotNull(f);
    }

    /**
     *
     */
    @Test
    public void testreadFileLinesToStringEmpty() {
        String s = readFileLinesToString("");
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testreadFileLinesToStringNull() {
        String s = readFileLinesToString(null);
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testReadFileLinesToStringSample1() {
        String f = readFileLinesToString("bin/sample.torrent");
        String s = "d8:announce35:udp://tracker.openbittorrent.com:8013:creation datei1327049827e4:infod6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1eee";
        assertEquals(f, s);
    }

    /**
     *
     */
    @Test
    public void testReadFileLinesToStringSample2() {
        String f = readFileLinesToString("bin/sample2.torrent");
        assertNull(f);
    }

}
