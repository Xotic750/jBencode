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

import java.io.Serializable;
import java.util.Collection;
import static java.util.Collections.unmodifiableMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListMap;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;
import static se.suka.baldr.jbencode.Utilities.requireString;

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
public final class AtomDictionary extends Atom implements Map<String, Atom>, Cloneable, Serializable {

    /**
     * Backing {@link ConcurrentSkipListMap}
     */
    private Map<String, Atom> value;

    /**
     * Constructs a new, empty tree map, using the natural ordering of its keys.
     * All keys inserted into the map must implement the {@link
     * Comparable} interface. Furthermore, all such keys must be
     * <em>mutually comparable</em>: {@code k1.compareTo(k2)} must not throw a
     * {@code ClassCastException} for any keys {@code k1} and {@code k2} in the
     * map. If the user attempts to put a key into the map that violates this
     * constraint (for example, the user attempts to put a string key into a map
     * whose keys are integers), the {@code put(Object key, Object value)} call
     * will throw a {@code ClassCastException}.
     */
    public AtomDictionary() {
        value = new ConcurrentSkipListMap<>();
    }

    /**
     * Constructs a new tree map containing a copy of the mappings as the given
     * map, ordered according to the <em>natural ordering</em> of its keys. All
     * keys inserted into the new map must implement the {@link
     * Comparable} interface. Furthermore, all such keys must be
     * <em>mutually comparable</em>: {@code k1.compareTo(k2)} must not throw a
     * {@code ClassCastException} for any keys {@code k1} and {@code k2} in the
     * map. This method runs in n*log(n) time.
     *
     * @param m the map whose mappings are to be copied and placed in this map
     * @throws ClassCastException if the keys in m are not {@link Comparable},
     * or are not mutually comparable
     * @throws NullPointerException if the specified map is null
     */
    public AtomDictionary(Map<? extends String, ? extends Atom> m) {
        this();
        putAll(requireNonNull(m));
    }

    /**
     *
     * @return
     */
    @Override
    public int bLength() {
        return 2 + value.entrySet().stream().parallel()
                .collect(summingInt(entry -> {
                    Atom atom = new AtomString(entry.getKey());
                    return atom.bLength() + entry.getValue().bLength();
                }));
    }

    /**
     * Removes all of the mappings from this map (optional operation). The map
     * will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation is
     * not supported by this map
     */
    @Override
    public void clear() {
        value.clear();
    }

    /**
     * Returns a shallow copy of this map. (The keys and values themselves are
     * not cloned.)
     *
     * @return a clone of this map
     */
    @Override
    public AtomDictionary clone() {
        try {
            AtomDictionary atomDictionary = (AtomDictionary) super.clone();
            atomDictionary.value = new ConcurrentSkipListMap<>(value);
            return atomDictionary;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key. More formally, returns <tt>true</tt> if and only if this map
     * contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>. (There can be at most one
     * such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     * key
     * @throws ClassCastException if the key is of an inappropriate type for
     * this map
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     * does not permit null keys
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsKey(final Object key) {
        return value.containsKey(requireString(key));
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the specified
     * value. More formally, returns <tt>true</tt> if and only if this map
     * contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>. This operation will
     * probably require time linear in the map size for most implementations of
     * the <tt>Map</tt> interface.
     *
     * @param atom value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the specified
     * value
     * @throws ClassCastException if the value is of an inappropriate type for
     * this map
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this map
     * does not permit null values
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean containsValue(final Object atom) {
        return value.containsValue(requireAtom(atom));
    }

    /**
     * Returns a deep copy of this map. (The keys and values themselves are
     * cloned.)
     *
     * @return a copy of this map
     */
    @Override
    public AtomDictionary copy() {
        return (AtomDictionary) value.entrySet().stream()
                .collect(toMap(Entry::getKey, e -> (Atom) e.getValue().copy()));
    }

    /**
     *
     * @return
     */
    @Override
    public String encode() {
        return value.entrySet().stream()
                .map(entry -> new AtomString(entry.getKey()).encode() + entry.getValue().encode())
                .collect(joining("", "d", "e"));
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map. The set
     * is backed by the map, so changes to the map are reflected in the set, and
     * vice-versa. If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own
     * <tt>remove</tt> operation, or through the
     * <tt>setValue</tt> operation on a map entry returned by the iterator) the
     * results of the iteration are undefined. The set supports element removal,
     * which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>,
     * <tt>Set.remove</tt>, <tt>removeAll</tt>, <tt>retainAll</tt> and
     * <tt>clear</tt> operations. It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a set view of the mappings contained in this map
     */
    @Override
    public Set<Entry<String, Atom>> entrySet() {
        return value.entrySet();
    }

    /**
     * Returns the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key.
     *
     * <p>
     * More formally, if this map contains a mapping from a key {@code k} to a
     * value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise it returns
     * {@code null}. (There can be at most one such mapping.)
     *
     * <p>
     * If this map permits null values, then a return value of {@code null} does
     * not <i>necessarily</i> indicate that the map contains no mapping for the
     * key; it's also possible that the map explicitly maps the key to
     * {@code null}. The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or {@code null}
     * if this map contains no mapping for the key
     * @throws ClassCastException if the key is of an inappropriate type for
     * this map
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     * does not permit null keys
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public Atom get(final Object key) {
        return value.get(requireString(key));
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map. The set is
     * backed by the map, so changes to the map are reflected in the set, and
     * vice-versa. If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own <tt>remove</tt>
     * operation), the results of the iteration are undefined. The set supports
     * element removal, which removes the corresponding mapping from the map,
     * via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    @Override
    public Set<String> keySet() {
        return value.keySet();
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation). If the map previously contained a mapping for the
     * key, the old value is replaced by the specified value. (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key key with which the specified value is to be associated
     * @param atom value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>. (A
     * <tt>null</tt>
     * return can also indicate that the map previously associated
     * <tt>null</tt>
     * with <tt>key</tt>, if the implementation supports <tt>null</tt>
     * values.)
     * @throws UnsupportedOperationException if the <tt>put</tt> operation is
     * not supported by this map
     * @throws ClassCastException if the class of the specified key or value
     * prevents it from being stored in this map
     * @throws NullPointerException if the specified key or value is null and
     * this map does not permit null keys or values
     * @throws IllegalArgumentException if some property of the specified key or
     * value prevents it from being stored in this map
     */
    @Override
    public Atom put(final String key, final Atom atom) {
        return value.put(requireString(key), requireAtom(atom));
    }

    /**
     * Copies all of the mappings from the specified map to this map (optional
     * operation). The effect of this call is equivalent to that of calling
     * {@link #put(Object,Object) put(k, v)} on this map once for each mapping
     * from key <tt>k</tt> to value <tt>v</tt> in the specified map. The
     * behaviour of this operation is undefined if the specified map is modified
     * while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws UnsupportedOperationException if the <tt>putAll</tt>
     * operation is not supported by this map
     * @throws ClassCastException if the class of a key or value in the
     * specified map prevents it from being stored in this map
     * @throws NullPointerException if the specified map is null, or if this map
     * does not permit null keys or values, and the specified map contains null
     * keys or values
     * @throws IllegalArgumentException if some property of a key or value in
     * the specified map prevents it from being stored in this map
     */
    @Override
    public void putAll(final Map<? extends String, ? extends Atom> m) {
        unmodifiableMap(requireNonNull(m)).entrySet().stream()
                .forEachOrdered(entry -> value.put(requireString(entry.getKey()), requireAtom(entry.getValue())));
    }

    /**
     * Removes the mapping for a key from this map if it is present (optional
     * operation). More formally, if this map contains a mapping from key
     * <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping is
     * removed. (The map can contain at most one such mapping.)
     *
     * <p>
     * Returns the value to which this map previously associated the key, or
     * <tt>null</tt> if the map contained no mapping for the key.
     *
     * <p>
     * If this map permits null values, then a return value of
     * <tt>null</tt> does not <i>necessarily</i> indicate that the map contained
     * no mapping for the key; it's also possible that the map explicitly mapped
     * the key to <tt>null</tt>.
     *
     * <p>
     * The map will not contain a mapping for the specified key once the call
     * returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     * <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * @throws UnsupportedOperationException if the <tt>remove</tt>
     * operation is not supported by this map
     * @throws ClassCastException if the key is of an inappropriate type for
     * this map
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     * does not permit null keys
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public Atom remove(final Object key) {
        return value.remove(requireString(key));
    }

    /**
     * Returns the number of key-value mappings in this map. If the map contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return value.size();
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are reflected
     * in the collection, and vice-versa. If the map is modified while an
     * iteration over the collection is in progress (except through the
     * iterator's own <tt>remove</tt> operation), the results of the iteration
     * are undefined. The collection supports element removal, which removes the
     * corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map
     */
    @Override
    public Collection<Atom> values() {
        return value.values();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AtomDictionary) {
            return value.equals(((AtomDictionary) obj).value);
        }
        return false;
    }

    @Override
    public String toString() {
        return value.toString();
    }

}
