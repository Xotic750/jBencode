package se.suka.baldr.bencode.models.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import se.suka.baldr.bencode.models.Atom;
import se.suka.baldr.bencode.models.AtomDictionary;
import se.suka.baldr.bencode.models.AtomInteger;
import se.suka.baldr.bencode.models.AtomList;
import se.suka.baldr.bencode.models.AtomString;
import se.suka.baldr.bencode.models.Bencode;

/**
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class BencodeTest {

    @Test
    public void testDecodeDictIntStrLst() {
        AtomDictionary l = Bencode.decodeDict("d3:fooli5e5:Helloe3:barli50000000e11:Hello Worldee");
        AtomDictionary expected = new AtomDictionary();
        AtomList il = new AtomList();
        il.add(new AtomInteger(5));
        il.add(new AtomString("Hello"));
        AtomList im = new AtomList();
        im.add(new AtomInteger(50000000));
        im.add(new AtomString("Hello World"));
        expected.put("foo", il);
        expected.put("bar", im);
        assertEquals(l, expected);
    }

    @Test
    public void testDecodeEmptyInt() {
        Atom<?> atom = Bencode.decode("ie");
        assertNull(atom);
    }

    @Test
    public void testDecodeEmptyStr() {
        Atom<?> atom = Bencode.decodeStr("0:");
        assertEquals(atom, new AtomString());
    }

    @Test
    public void testDecodeFileDownloads() {
        Atom<?> atom = Bencode.decodeFile("bin/Downloads.torrent");
        assertNotNull(atom);
        assertTrue(atom instanceof Atom);
    }

    @Test
    public void testDecodeFileSample1() {
        Atom<?> atom = Bencode.decodeFile("bin/sample.torrent");
        assertNotNull(atom);
    }

    @Test
    public void testDecodeFileSample2() {
        Atom<?> atom = Bencode.decodeFile("bin/sample2.torrent");
        assertNotNull(atom);
    }

    @Test
    public void testDecodeFileLinux() {
        Atom<?> atom = Bencode.decodeFile("bin/linux.torrent");
        assertNotNull(atom);
    }

    @Test
    public void testDecodeFileUTorrent() {
        Atom<?> atom = Bencode.decodeFile("bin/uTorrent.torrent");
        assertNotNull(atom);
        assertTrue(atom instanceof Atom);
    }

    @Test
    public void testDecodeFileWorkspace() {
        Atom<?> atom = Bencode.decodeFile("bin/workspace.torrent");
        assertNotNull(atom);
        assertTrue(atom instanceof Atom);
    }

    @Test
    public void testDecodeIntEmpty() {
        AtomInteger atom = Bencode.decodeInt("");
        assertNull(atom);
    }

    @Test
    public void testDecodeIntEmptyLE() {
        AtomInteger atom = Bencode.decodeInt("ie");
        assertNull(atom);
    }

    @Test
    public void testDecodeIntInvalidNegZero() {
        AtomInteger atom = Bencode.decodeInt("i-0e");
        assertNull(atom);
    }

    @Test
    public void testDecodeIntNeg() {
        AtomInteger atom = Bencode.decodeInt("i-12e");
        assertEquals(atom, new AtomInteger(-12));
    }

    @Test
    public void testDecodeIntNull() {
        AtomInteger atom = Bencode.decodeInt(null);
        assertNull(atom);
    }

    @Test
    public void testDecodeIntPos() {
        AtomInteger atom = Bencode.decodeInt("i12e");
        assertEquals(atom, new AtomInteger(12));
    }

    @Test
    public void testDecodeIntPosZero() {
        AtomInteger atom = Bencode.decodeInt("i0e");
        assertEquals(atom, new AtomInteger(0));
    }

    @Test
    public void testDecodeIntStrLst() {
        Atom<?> l = Bencode.decode("d3:barli50000000e11:Hello Worlde3:fooli5e5:Helloee");
        AtomDictionary expected = new AtomDictionary();
        AtomList il = new AtomList();
        il.add(new AtomInteger(5));
        il.add(new AtomString("Hello"));
        AtomList im = new AtomList();
        im.add(new AtomInteger(50000000));
        im.add(new AtomString("Hello World"));
        expected.put("foo", il);
        expected.put("bar", im);
        assertEquals(l, expected);
    }

    @Test
    public void testDecodeInvalidNegZeroInt() {
        Atom<?> atom = Bencode.decode("i-0e");
        assertNull(atom);
    }

    @Test
    public void testDecodeListEmpty() {
        AtomList atom = Bencode.decodeList("");
        assertNull(atom);
    }

    @Test
    public void testDecodeListEmptyLE() {
        AtomList atom = Bencode.decodeList("le");
        assertEquals(atom, new AtomList());
    }

    @Test
    public void testDecodeListIntStr() {
        AtomList l = Bencode.decodeList("li5e5:Helloe");
        AtomList expected = new AtomList();
        expected.add(new AtomInteger(5));
        expected.add(new AtomString("Hello"));
        assertEquals(l, expected);
    }

    @Test
    public void testDecodeListIntStrLst() {
        AtomList l = Bencode.decodeList("lli5e5:Helloeli50000000e11:Hello Worldee");
        AtomList expected = new AtomList();
        AtomList il = new AtomList();
        il.add(new AtomInteger(5));
        il.add(new AtomString("Hello"));
        AtomList im = new AtomList();
        im.add(new AtomInteger(50000000));
        im.add(new AtomString("Hello World"));
        expected.add(il);
        expected.add(im);
        assertEquals(l, expected);
    }

    @Test
    public void testDecodeListNull() {
        AtomList atom = Bencode.decodeList(null);
        assertNull(atom);
    }

    @Test
    public void testDecodeNegInt() {
        Atom<?> atom = Bencode.decode("i-12e");
        assertEquals(atom, new AtomInteger(-12));
    }

    @Test
    public void testDecodeNull() {
        Atom<?> atom = Bencode.decode(null);
        assertNull(atom);
    }

    @Test
    public void testDecodePartialTorrent1() {
        String s = "d6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1ee";
        Atom<?> atom = Bencode.decodeDict(s);
        assertNotNull(atom);
        assertEquals(s.length(), atom.bLength());
    }

    @Test
    public void testDecodePartialTorrent2() {
        String s = "d8:announce35:udp://tracker.openbittorrent.com:8013:creation datei1327049827e4:infod6:lengthi20e4:name10:sample.txt12:piece lengthi65536e6:pieces0:7:privatei1eee";
        Atom<?> atom = Bencode.decodeDict(s);
        assertNotNull(atom);
        assertEquals(s.length(), atom.bLength());
    }

    @Test
    public void testDecodePosInt() {
        Atom<?> atom = Bencode.decode("i12e");
        assertEquals(atom, new AtomInteger(12));
    }

    @Test
    public void testDecodePosZeroInt() {
        Atom<?> atom = Bencode.decode("i0e");
        assertEquals(atom, new AtomInteger(0));
    }

    @Test
    public void testDecodeSmallStr() {
        Atom<?> atom = Bencode.decodeStr("5:Hello");
        assertEquals(atom, new AtomString("Hello"));
    }

    @Test
    public void testDecodeStr() {
        AtomString atom = Bencode.decodeStr("5:Hello");
        assertEquals(atom, new AtomString("Hello"));
    }

    @Test
    public void testDecodeStrEmpty() {
        AtomString atom = Bencode.decodeStr("");
        assertNull(atom);
    }

    @Test
    public void testDecodeStrEmpty0() {
        AtomString atom = Bencode.decodeStr("0:");
        assertEquals(atom, new AtomString(""));
    }

    @Test
    public void testDecodeStrInvalidChar() {
        Atom<?> atom = Bencode.decodeStr("0a:");
        assertNull(atom);
    }

    @Test
    public void testDecodeStrLarge() {
        AtomString atom = Bencode.decodeStr("11:Hello World");
        assertEquals(atom, new AtomString("Hello World"));
    }

    @Test
    public void testDecodeStrLargeStr() {
        Atom<?> atom = Bencode.decodeStr("11:Hello World");
        assertEquals(atom, new AtomString("Hello World"));
    }

    @Test
    public void testDecodeStrMissing() {
        Atom<?> s = Bencode.decodeStr("0");
        assertNull(s);
    }

    @Test
    public void testDecodeStrMissingNum() {
        Atom<?> atom = Bencode.decodeStr("a");
        assertNull(atom);
    }

    @Test
    public void testDecodeStrNull() {
        AtomString atom = Bencode.decodeStr(null);
        assertNull(atom);
    }

    @Test
    public void testEncodeAtom() {
        Atom<?> atom = new AtomInteger(5);
        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "i5e");
    }

    @Test
    public void testEncodeDict() {
        AtomDictionary atom = new AtomDictionary();

        AtomList il = new AtomList();
        il.add(new AtomInteger(5));
        il.add(new AtomString("Hello"));
        atom.put("foo", il);

        AtomList im = new AtomList();
        im.add(new AtomInteger(50000000));
        im.add(new AtomString("Hello World"));
        atom.put("bar", im);

        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "d3:barli50000000e11:Hello Worlde3:fooli5e5:Helloee");
    }

    @Test
    public void testEncodeList() {
        AtomList atom = new AtomList();
        atom.add(new AtomInteger(5));
        atom.add(new AtomString("Hello"));
        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "li5e5:Helloe");
    }

    @Test
    public void testEncodeNull() {
        String encoded = Bencode.encode(null);
        assertNull(encoded);
    }

    @Test
    public void testEncondeInt() {
        AtomInteger atom = new AtomInteger(1);
        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "i1e");
    }

    @Test
    public void testEncondeStr() {
        AtomString atom = new AtomString("Hello");
        String encoded = Bencode.encode(atom);
        assertEquals(encoded, "5:Hello");
    }

}
