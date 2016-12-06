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

import java.util.Collection;
import static java.util.Collections.unmodifiableMap;
import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NavigableSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toMap;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
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
public final class AtomDictionary extends Atom implements Map<String, Atom>, Cloneable, Comparable<AtomDictionary> {

    private static final long serialVersionUID = -7602783133044374261L;

    private static final Logger LOGGER = getLogger(AtomDictionary.class);

    /**
     * Tests if the supplied object is an instance of {@code String}.
     *
     * @param o The object to test
     * @return {@code true} if the object is an instance of {@code String},
     * otherwise {@code false}
     */
    private static boolean isString(Object o) {
        return o instanceof String;
    }

    /**
     * Requires that the supplied object is an instance of {@code String}.
     *
     * @param <T> The {@code Class} of the test object
     * @param o The object to test
     * @throws ClassCastException if the object is not an instance of
     * {@code String}
     * @return The string object
     */
    private static <T> T requireString(T o) {
        return requireString(o, "");
    }

    /**
     * Requires that the supplied object is an instance of {@code String}.
     *
     * @param <T> The {@code Class} of the test object
     * @param o The object to test
     * @param message The message to throw with {@link ClassCastException}
     * @throws ClassCastException if the object is not an instance of
     * {@code String}
     * @return The string object
     */
    private static <T> T requireString(T o, String message) {
        if (!isString(o)) {
            throw new ClassCastException(message);
        }
        return o;
    }

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
    public static final <T> T requireAtomDictionary(T o) {
        return requireAtom(o, "");
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
    public static final <T> T requireAtomDictionary(T o, String message) {
        if (!isAtomDictionary(o)) {
            throw new ClassCastException(message);
        }
        return o;
    }

    /**
     * Backing {@link ConcurrentSkipListMap}
     */
    private ConcurrentSkipListMap<String, Atom> value;

    /**
     * Constructs a new, empty {@code AtomDictionary}, using the natural
     * ordering of its keys.
     */
    public AtomDictionary() {
        value = new ConcurrentSkipListMap<>();
    }

    /**
     * Constructs a new {@code AtomDictionary} containing a copy of the mappings
     * as the given map, ordered according to the <em>natural ordering</em> of
     * its keys.
     *
     * @param m the map whose mappings are to be copied and placed in this map
     * @throws NullPointerException if the specified map is null
     */
    public AtomDictionary(Map<? extends String, ? extends Atom> m) {
        this();
        putAll(requireNonNull(m));
    }

    /**
     * Returns the length of the Bencoded string of this {@link Atom}. This
     * method is faster than performing an <code>encode().length()</code>.
     *
     * @return The length of the Becoded string
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
     * Returns a key-value mapping associated with the least key greater than or
     * equal to the given key, or {@code null} if there is no such entry. The
     * returned entry does <em>not</em>
     * support the {@code Entry.setValue} method.
     *
     * @param key
     * @return
     * @throws NullPointerException if the specified key is {@code null}
     */
    public Entry<String, Atom> ceilingEntry(final String key) {
        return value.ceilingEntry(requireNonNull(key));
    }

    /**
     * @param key
     * @return
     * @throws NullPointerException if the specified key is null
     */
    public String ceilingKey(final String key) {
        return value.ceilingKey(requireNonNull(key));
    }

    /**
     * Removes all of the mappings from this map (optional operation). The map
     * will be empty after this call returns.
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
            final AtomDictionary atomDictionary = (AtomDictionary) super.clone();
            atomDictionary.value = new ConcurrentSkipListMap<>(value);
            return atomDictionary;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    /**
     *
     * @return
     */
    public Comparator<? super String> comparator() {
        return value.comparator();
    }

    /**
     * Attempts to compute a mapping for the specified key and its current
     * mapped value (or {@code null} if there is no current mapping). The
     * function is <em>NOT</em> guaranteed to be applied once atomically.
     *
     * @param key key with which the specified value is to be associated
     * @param remappingFunction the function to compute a value
     * @return the new value associated with the specified key, or null if none
     * @throws NullPointerException if the specified key is null or the
     * remappingFunction is null
     */
    @Override
    public Atom compute(final String key, final BiFunction<? super String, ? super Atom, ? extends Atom> remappingFunction) {
        return value.compute(requireNonNull(key), requireNonNull(remappingFunction));
    }

    /**
     * If the specified key is not already associated with a value, attempts to
     * compute its value using the given mapping function and enters it into
     * this map unless {@code null}. The function is <em>NOT</em> guaranteed to
     * be applied once atomically only if the value is not present.
     *
     * @param key key with which the specified value is to be associated
     * @param mappingFunction the function to compute a value
     * @return the current (existing or computed) value associated with the
     * specified key, or null if the computed value is null
     * @throws NullPointerException if the specified key is null or the
     * mappingFunction is null
     */
    @Override
    public Atom computeIfAbsent(final String key, final Function<? super String, ? extends Atom> mappingFunction) {
        return value.computeIfAbsent(requireNonNull(key), requireNonNull(mappingFunction));
    }

    /**
     * If the value for the specified key is present, attempts to compute a new
     * mapping given the key and its current mapped value. The function is
     * <em>NOT</em> guaranteed to be applied once atomically.
     *
     * @param key key with which a value may be associated
     * @param remappingFunction the function to compute a value
     * @return the new value associated with the specified key, or null if none
     * @throws NullPointerException if the specified key is null or the
     * remappingFunction is null
     */
    @Override
    public Atom computeIfPresent(final String key, final BiFunction<? super String, ? super Atom, ? extends Atom> remappingFunction) {
        return value.computeIfPresent(requireNonNull(key), requireNonNull(remappingFunction));
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
     * @throws NullPointerException if the specified key is null and this map
     * does not permit null keys
     */
    @Override
    public boolean containsKey(final Object key) {
        return value.containsKey(requireString(requireNonNull(key)));
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the specified
     * value. More formally, returns <tt>true</tt> if and only if this map
     * contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>. This operation will
     * probably require time linear in the map size for most implementations of
     * the <tt>Map</tt> interface.
     *
     * @param o value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the specified
     * value
     * @throws ClassCastException if the value is of an inappropriate type for
     * this map
     * @throws NullPointerException if the specified value is null
     */
    @Override
    public boolean containsValue(final Object o) {
        return value.containsValue(requireAtom(requireNonNull(o)));
    }

    /**
     *
     * @return
     */
    public NavigableSet<String> descendingKeySet() {
        return value.descendingKeySet();
    }

    /**
     *
     * @return
     */
    public ConcurrentNavigableMap<String, Atom> descendingMap() {
        return value.descendingMap();
    }

    /**
     * Returns a deep copy of this {@link Atom}.
     *
     * @return a copy of this {@link Atom}
     */
    @Override
    public AtomDictionary copy() {
        Map<String, Atom> collect = value.entrySet().stream()
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
        return value.entrySet().stream()
                .map(entry -> new AtomString(entry.getKey()).encode() + entry.getValue().encode())
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
     * Returns a key-value mapping associated with the least key in this map, or
     * {@code null} if the map is empty. The returned entry does <em>not</em>
     * support the {@code Entry.setValue} method.
     *
     * @return
     */
    public Entry<String, Atom> firstEntry() {
        return value.firstEntry();
    }

    /**
     * Returns a key-value mapping associated with the greatest key less than or
     * equal to the given key, or {@code null} if there is no such key. The
     * returned entry does <em>not</em> support the {@code Entry.setValue}
     * method.
     *
     * @param key the key
     * @return
     * @throws NullPointerException if the specified key is null
     */
    public Entry<String, Atom> floorEntry(final String key) {
        return value.floorEntry(requireNonNull(key));
    }

    /**
     * @param key the key
     * @return
     * @throws NullPointerException if the specified key is null
     */
    public String floorKey(final String key) {
        return value.floorKey(requireNonNull(key));
    }

    /**
     *
     * @return
     */
    public String firstKey() {
        return value.firstKey();
    }

    @Override
    public void forEach(BiConsumer<? super String, ? super Atom> action) {
        value.forEach(requireNonNull(action));
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
     * @throws NullPointerException if the specified key is null
     */
    @Override
    public Atom get(final Object key) {
        return value.get(requireString(requireNonNull(key)));
    }

    /**
     * Returns the value to which the specified key is mapped, or the given
     * defaultValue if this map contains no mapping for the key.
     *
     * @param key the key
     * @param defaultValue the value to return if this map contains no mapping
     * for the given key
     * @return the mapping for the key, if present; else the defaultValue
     * @throws ClassCastException if the key is of an inappropriate type for
     * this map
     * @throws NullPointerException if the specified key is null
     */
    @Override
    public Atom getOrDefault(final Object key, final Atom defaultValue) {
        return value.getOrDefault(requireString(requireNonNull(key)), defaultValue);
    }

    /**
     * @param toKey
     * @return
     * @throws NullPointerException if {@code toKey} is null
     */
    public ConcurrentNavigableMap<String, Atom> headMap(final String toKey) {
        return value.headMap(requireNonNull(toKey));
    }

    /**
     * @param toKey
     * @param inclusive
     * @return
     * @throws NullPointerException if {@code toKey} is null
     */
    public ConcurrentNavigableMap<String, Atom> headMap(final String toKey, final boolean inclusive) {
        return value.headMap(requireNonNull(toKey), inclusive);
    }

    /**
     * Returns a key-value mapping associated with the least key strictly
     * greater than the given key, or {@code null} if there is no such key. The
     * returned entry does <em>not</em> support the {@code Entry.setValue}
     * method.
     *
     * @param key the key
     * @return
     * @throws NullPointerException if the specified key is null
     */
    public Entry<String, Atom> higherEntry(final String key) {
        return value.higherEntry(requireNonNull(key));
    }

    /**
     * @param key the key
     * @return
     * @throws NullPointerException if the specified key is null
     */
    public String higherKey(final String key) {
        return value.higherKey(requireNonNull(key));
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
     * Returns a key-value mapping associated with the greatest key in this map,
     * or {@code null} if the map is empty. The returned entry does <em>not</em>
     * support the {@code Entry.setValue} method.
     *
     * @return
     */
    public Entry<String, Atom> lastEntry() {
        return value.lastEntry();
    }

    /**
     * @return @throws NoSuchElementException {@inheritDoc}
     */
    public String lastKey() {
        return value.lastKey();
    }

    /**
     * Returns a key-value mapping associated with the greatest key strictly
     * less than the given key, or {@code null} if there is no such key. The
     * returned entry does <em>not</em> support the {@code Entry.setValue}
     * method.
     *
     * @param key
     * @return
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if the specified key is null
     */
    public Map.Entry<String, Atom> lowerEntry(String key) {
        return value.lowerEntry(key);
    }

    /**
     * @param key
     * @return
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if the specified key is null
     */
    public String lowerKey(String key) {
        return value.lowerKey(key);
    }

    /**
     * If the specified key is not already associated with a value, associates
     * it with the given value. Otherwise, replaces the value with the results
     * of the given remapping function, or removes if {@code null}. The function
     * is <em>NOT</em>
     * guaranteed to be applied once atomically.
     *
     * @param key key with which the specified value is to be associated
     * @param value the value to use if absent
     * @param remappingFunction the function to recompute a value if present
     * @return the new value associated with the specified key, or null if none
     * @throws NullPointerException if the specified key or value is null or the
     * remappingFunction is null
     * @since 1.8
     */
    @Override
    public Atom merge(String key, Atom value, BiFunction<? super Atom, ? super Atom, ? extends Atom> remappingFunction) {
        return this.value.merge(key, value, remappingFunction);
    }

    /**
     *
     * @return
     */
    public NavigableSet<String> navigableKeySet() {
        return value.navigableKeySet();
    }

    /**
     * Removes and returns a key-value mapping associated with the least key in
     * this map, or {@code null} if the map is empty. The returned entry does
     * <em>not</em> support the {@code Entry.setValue} method.
     *
     * @return
     */
    public Map.Entry<String, Atom> pollFirstEntry() {
        return value.pollFirstEntry();
    }

    /**
     * Removes and returns a key-value mapping associated with the greatest key
     * in this map, or {@code null} if the map is empty. The returned entry does
     * <em>not</em> support the {@code Entry.setValue} method.
     *
     * @return
     */
    public Map.Entry<String, Atom> pollLastEntry() {
        return value.pollLastEntry();
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
     * @throws NullPointerException if the specified key or value is null
     */
    @Override
    public Atom put(final String key, final Atom atom) {
        return value.put(requireNonNull(key), requireNonNull(atom));
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
     * @throws NullPointerException if the specified map is null, or if the
     * specified map contains null keys or values
     */
    @Override
    public void putAll(final Map<? extends String, ? extends Atom> m) {
        unmodifiableMap(requireNonNull(m)).entrySet().stream()
                .forEachOrdered(entry -> {
                    final String key = requireNonNull(entry.getKey());
                    final Atom atom = requireNonNull(entry.getValue());
                    value.put(key, atom);
                });
    }

    /**
     * {@inheritDoc}
     *
     * @return the previous value associated with the specified key, or
     * {@code null} if there was no mapping for the key
     * @throws ClassCastException if the specified key cannot be compared with
     * the keys currently in the map
     * @throws NullPointerException if the specified key or value is null
     */
    @Override
    public Atom putIfAbsent(String key, Atom value) {
        return this.value.putIfAbsent(key, value);
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
     * @throws ClassCastException if the key is of an inappropriate type for
     * this map
     * @throws NullPointerException if the specified key is null
     */
    @Override
    public Atom remove(final Object key) {
        return value.remove(requireString(requireNonNull(key)));
    }

    @Override
    public void replaceAll(BiFunction<? super String, ? super Atom, ? extends Atom> function) {
        value.replaceAll(function);
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
     * @param fromKey
     * @param toKey
     * @return
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if {@code fromKey} or {@code toKey} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public ConcurrentNavigableMap<String, Atom> subMap(String fromKey, String toKey) {
        return value.subMap(fromKey, toKey);
    }

    /**
     * @param fromKey
     * @param fromInclusive
     * @param toKey
     * @param toInclusive
     * @return
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if {@code fromKey} or {@code toKey} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public ConcurrentNavigableMap<String, Atom> subMap(String fromKey, boolean fromInclusive, String toKey, boolean toInclusive) {
        return value.subMap(fromKey, fromInclusive, toKey, toInclusive);
    }

    /**
     * @param fromKey
     * @return
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if {@code fromKey} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public ConcurrentNavigableMap<String, Atom> tailMap(String fromKey) {
        return value.tailMap(fromKey);
    }

    /**
     * @param fromKey
     * @param inclusive
     * @return
     * @throws ClassCastException {@inheritDoc}
     * @throws NullPointerException if {@code fromKey} is null
     * @throws IllegalArgumentException {@inheritDoc}
     */
    public ConcurrentNavigableMap<String, Atom> tailMap(String fromKey, boolean inclusive) {
        return value.tailMap(fromKey, inclusive);
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
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.value);
        return hash;
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
            return value.equals(((AtomDictionary) obj).value);
        }
        return false;
    }

    /**
     * Returns a {@code String} object representing this
     * {@code AtomDictionary}'s value.
     *
     * @return a string representation of the value of this object
     */
    @Override
    public String toString() {
        return value.toString();
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
        return encode().compareTo(requireNonNull(anotherAtomDictionary).encode());
    }

}
