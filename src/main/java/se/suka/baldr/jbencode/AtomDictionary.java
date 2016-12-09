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
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;
import static se.suka.baldr.jbencode.Utilities.stringToAsciiBytes;

/**
 * A dictionary is encoded as d&lt;contents&gt;e. The elements of the dictionary
 * are encoded each key immediately followed by its value. All keys must be byte
 * strings and must appear in lexicographical order. A dictionary that
 * associates the values 42 and "spam" with the keys "foo" and "bar",
 * respectively (in other words, {"bar": "spam", "foo": 42}}), would be encoded
 * as follows: d3:bar4:spam3:fooi42ee. (This might be easier to read by
 * inserting some spaces: d 3:bar 4:spam 3:foo i42e e.)
 *
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomDictionary extends ConcurrentSkipListMap<AtomString, Atom> implements Atom, ConcurrentNavigableMap<AtomString, Atom>, Cloneable, Comparable<AtomDictionary> {

    private static final long serialVersionUID = -7602783133044374261L;

    //private static final Logger LOGGER = getLogger(AtomDictionary.class);
    /**
     * Test if an object reference is an instance of {@code AtomDictionary}.
     *
     * @param o the object reference to check is instance of
     * {@code AtomDictionary}
     * @return {@code true} if the object is an instance of
     * {@code AtomDictionary}, otherwise {@code false}
     */
    public static boolean isAtomDictionary(Object o) {
        return o instanceof AtomDictionary;
    }

    /**
     * Test if an object reference is an instance of {@code AtomDictionary},
     * throw a {@link ClassCastException} if it is not, otherwise return the
     * reference object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @throws ClassCastException if {@code obj} is not an instance of
     * {@code AtomDictionary}
     * @return the object reference
     */
    public static <T> T requireAtomDictionary(T o) {
        return requireAtomDictionary(o, "");
    }

    /**
     * Test if an object reference is an instance of {@code AtomDictionary},
     * throw a {@link ClassCastException} if it is not, otherwise return the
     * reference object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @param message detail message to be used in the event that a
     * {@code ClassCastException} is thrown
     * @throws ClassCastException if {@code obj} is not an instance of
     * {@code AtomDictionary}
     * @return the object reference
     */
    public static <T> T requireAtomDictionary(T o, String message) {
        if (!isAtomDictionary(o)) {
            throw new ClassCastException(message);
        }
        return o;
    }

    /**
     * Constructs a new, empty {@code AtomDictionary}, using the natural
     * ordering of its keys.
     */
    public AtomDictionary() {
        super();
    }

    /**
     * Constructs a new {@code AtomDictionary} containing a copy of the mappings
     * as the given map, ordered according to the <em>natural ordering</em> of
     * its keys.
     *
     * @param m the map whose mappings are to be copied and placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public AtomDictionary(Map<? extends AtomString, ? extends Atom> m) {
        super(m);
    }

    /**
     * Returns the length of the Bencoded string of this {@link Atom}. This
     * method is faster than performing an <code>encode().length()</code>.
     *
     * @return The length of the Becoded string
     */
    @Override
    public int bLength() {
        return 2 + super.entrySet().stream().parallel()
                .collect(summingInt(entry -> entry.getKey().bLength() + entry.getValue().bLength()));
    }

    /**
     * Returns a shallow copy of this map. (The keys and values themselves are
     * not cloned.)
     *
     * @return a clone of this map
     */
    @Override
    public AtomDictionary clone() {
        return (AtomDictionary) super.clone();
    }

    /**
     * Returns a deep copy of this {@link Atom}.
     *
     * @return a copy of this {@link Atom}
     */
    @Override
    public AtomDictionary copy() {
        final Map<AtomString, Atom> collect = super.entrySet().stream().parallel()
                .collect(toMap(Entry::getKey, e -> e.getValue().copy()));
        return new AtomDictionary(collect);
    }

    /**
     * Returns the Bencoded string of this {@link Atom}.
     *
     * @return The Benoded string
     */
    @Override
    public String encode() {
        return super.entrySet().stream()
                .map(entry -> entry.getKey().encode() + entry.getValue().encode())
                .collect(joining("", "d", "e"));
    }

    /**
     * Returns the Bencoded ASCII bytes of this {@link Atom}.
     *
     * @return The Benoded ASCII bytes
     */
    @Override
    public byte[] encodeAsBytes() {
        return stringToAsciiBytes(encode());
    }

    /**
     * Returns a hash code value for the object. This method is supported for
     * the benefit of hash tables such as those provided by
     * {@link java.util.HashMap}.
     * <p>
     * The general contract of {@code hashCode} is:
     * <ul>
     * <li>Whenever it is invoked on the same object more than once during an
     * execution of a Java application, the {@code hashCode} method must
     * consistently return the same integer, provided no information used in
     * {@code equals} comparisons on the object is modified. This integer need
     * not remain consistent from one execution of an application to another
     * execution of the same application.
     * <li>If two objects are equal according to the {@code equals(Object)}
     * method, then calling the {@code hashCode} method on each of the two
     * objects must produce the same integer result.
     * <li>It is <em>not</em> required that if two objects are unequal according
     * to the {@link java.lang.Object#equals(java.lang.Object)} method, then
     * calling the {@code hashCode} method on each of the two objects must
     * produce distinct integer results. However, the programmer should be aware
     * that producing distinct integer results for unequal objects may improve
     * the performance of hash tables.
     * </ul>
     * <p>
     * As much as is reasonably practical, the hashCode method defined by class
     * {@code Object} does return distinct integers for distinct objects. (This
     * is typically implemented by converting the internal address of the object
     * into an integer, but this implementation technique is not required by the
     * Java&trade; programming language.)
     *
     * @return a hash code value for this object.
     * @see java.lang.Object#equals(java.lang.Object)
     * @see java.lang.System#identityHashCode
     */
    @Override
    public int hashCode() {
        return 469 + super.hashCode();
    }

    /**
     * Compares the specified object with this map for equality. Returns
     * {@code true} if the given object is also a map and the two maps represent
     * the same mappings. More formally, two maps {@code m1} and {@code m2}
     * represent the same mappings if
     * {@code m1.entrySet().equals(m2.entrySet())}. This operation may return
     * misleading results if either map is concurrently modified during
     * execution of this method.
     *
     * @param obj object to be compared for equality with this map
     * @return {@code true} if the specified object is equal to this map
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AtomDictionary) {
            return super.equals(obj);
        }
        return false;
    }

    /**
     * Compares two {@code AtomDictionary} objects lexicographically using the
     * {@code toString} method.
     *
     * @param anotherAtomDictionary the {@code AtomDictionary} to be compared.
     * @throws NullPointerException if the specified
     * {@code anotherAtomDictionary} is {@code null}
     * @return the value {@code 0} if the argument {@code AtomDictionary} is
     * equal to this {@code AtomDictionary}; a value less than {@code 0} if this
     * {@code AtomString} is lexicographically less than the
     * {@code AtomDictionary} argument; and a value greater than {@code 0} if
     * this {@code AtomDictionary} is lexicographically greater than the
     * {@code AtomDictionary} argument.
     */
    @Override
    public int compareTo(final AtomDictionary anotherAtomDictionary) {
        return encode().compareTo(anotherAtomDictionary.encode());
    }

}
