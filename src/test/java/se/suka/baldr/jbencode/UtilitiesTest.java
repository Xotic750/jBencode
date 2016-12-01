/*
 * The MIT License
 *
 * Copyright 2016 graham.
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
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static se.suka.baldr.jbencode.Utilities.findFirstNotOf;
import static se.suka.baldr.jbencode.Utilities.readTorrentFile;

/**
 *
 * @author graham
 */
public class UtilitiesTest {

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
    public UtilitiesTest() {
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
    public void testreadFileBytesToStringEmpty() {
        String s = readTorrentFile("");
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testreadFileBytesToStringNull() {
        String s = readTorrentFile(null);
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testreadFileBytesToStringDirectory() {
        String s = readTorrentFile("samples");
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testreadFileBytesToStringNotExist() {
        String s = readTorrentFile("wibble");
        assertNull(s);
    }

    /**
     *
     */
    @Test
    public void testReadFileBytesToStringSample1() {
        String f = readTorrentFile("samples/sample1.torrent");
        String s = "d8:announce35:udp://tracker.openbittorrent.com:8013:creation datei1327049827e4:infod6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1eee\r";
        assertEquals(f, s);
    }

    /**
     *
     */
    @Test
    public void testReadFileBytesToStringSample2() {
        String f = readTorrentFile("samples/sample2.torrent");
        assertNotNull(f);
    }

}
