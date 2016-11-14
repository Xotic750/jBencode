package se.suka.baldr.bencode.models;

/**
 * A byte string (a sequence of bytes, not necessarily characters) is encoded as
 * &lt;length&gt;:&lt;contents&gt;. The length is encoded in base 10, like
 * integers, but must be non-negative (zero is allowed); the contents are just
 * the bytes that make up the string. The string "spam" would be encoded as
 * 4:spam. The specification does not deal with encoding of characters outside
 * the ASCII set; to mitigate this, some BitTorrent applications explicitly
 * communicate the encoding (most commonly UTF-8) in various non-standard ways.
 * This is identical to how
 * <a href="https://en.wikipedia.org/wiki/Netstring">netstrings</a> work, except
 * that netstrings additionally append a comma suffix after the byte sequence.
 *
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomString extends Atom<String> {

    /**
     * Initialises a newly created {@code AtomString} object so that it
     * represents an empty character sequence.
     */
    public AtomString() {
        this("");
    }

    /**
     * Initialises a newly created {@code AtomString} object so that it
     * represents the same sequence of characters as the argument AtomString; in
     * other words, the newly created AtomString is a copy of the argument
     * AtomString.
     *
     * @param atom A {@code AtomString}
     */
    public AtomString(AtomString atom) {
        this(atom.toString());
    }

    /**
     * Initialises a newly created {@code AtomString} object so that it
     * represents the same sequence of characters as the argument; in other
     * words, the newly created AtomString is a copy of the argument string.
     *
     * @param value A {@code String}
     */
    public AtomString(String value) {
        super(value);
    }

    @Override
    public int bLength() {
        final int LEN = length();
        return Integer.toString(LEN).length() + 1 + LEN;
    }

    @Override
    public String encode() {
        return length() + ":" + this;
    }

    /**
     * Returns the length of this string. The length is equal to the number of
     * <a href="Character.html#unicode">Unicode code units</a> in the string.
     *
     * @return the length of the sequence of characters represented by this
     * object.
     */
    public int length() {
        return getValue().length();
    }

}
