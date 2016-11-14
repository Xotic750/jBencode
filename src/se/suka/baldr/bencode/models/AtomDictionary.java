package se.suka.baldr.bencode.models;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

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
public final class AtomDictionary extends Atom<TreeMap<String, Atom<?>>> implements Map<String, Atom<?>> {

    public AtomDictionary() {
        this(new TreeMap<>());
    }

    public AtomDictionary(AtomDictionary atomDict) {
        this();
        Objects.requireNonNull(atomDict);
        atomDict.entrySet().forEach((entry) -> {
            String key = entry.getKey();
            Atom<?> value = entry.getValue();
            Class<?> atomClass = value.getClass();
            if (atomClass.equals(AtomInteger.class)) {
                put(key, new AtomInteger((AtomInteger) value));
            } else if (atomClass.equals(AtomString.class)) {
                put(key, new AtomString((AtomString) value));
            } else if (atomClass.equals(AtomList.class)) {
                put(key, new AtomList((AtomList) value));
            } else if (atomClass.equals(AtomDictionary.class)) {
                put(key, new AtomDictionary((AtomDictionary) value));
            } else {
                System.err.println("AtomDictionary: unknown Atom type");
            }
        });
    }

    private AtomDictionary(TreeMap<String, Atom<?>> value) {
        super(value);
    }

    @Override
    public int bLength() {
        return getValue().entrySet().stream().map((final Entry<String, Atom<?>> ENTRY) -> new AtomString(ENTRY.getKey()).bLength() + ENTRY.getValue().bLength()).reduce(2, Integer::sum);
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
        getValue().clear();
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
    public boolean containsKey(String key) {
        Objects.requireNonNull(key);
        return getValue().containsKey(key);
    }

    @Override
    public boolean containsKey(Object key) {
        Objects.requireNonNull(key);
        stringCheck(key);
        return containsKey((String) key);
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the specified
     * value. More formally, returns <tt>true</tt> if and only if this map
     * contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>. This operation will
     * probably require time linear in the map size for most implementations of
     * the <tt>Map</tt> interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the specified
     * value
     * @throws ClassCastException if the value is of an inappropriate type for
     * this map
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified value is null and this map
     * does not permit null values
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public boolean containsValue(Atom<?> value) {
        Objects.requireNonNull(value);
        return getValue().containsValue(value);
    }

    @Override
    public boolean containsValue(Object value) {
        Objects.requireNonNull(value);
        atomCheck(value);
        return containsValue((Atom<?>) value);
    }

    @Override
    public String encode() {
        StringBuilder str = new StringBuilder("d");
        getValue().keySet().forEach((final String KEY) -> {
            str.append(new AtomString(KEY).encode()).append(get(KEY).encode());
        });
        return str.append("e").toString();
    }

    /**
     * Returns a {@link Set} view of the mappings contained in this map. The set
     * is backed by the map, so changes to the map are reflected in the set, and
     * vice-versa. If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own <tt>remove</tt> operation, or
     * through the
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
    public Set<Entry<String, Atom<?>>> entrySet() {
        return getValue().entrySet();
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
    public Atom<?> get(String key) {
        Objects.requireNonNull(key);
        return getValue().get(key);
    }

    @Override
    public Atom<?> get(Object key) {
        Objects.requireNonNull(key);
        stringCheck(key);
        return get((String) key);
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return getValue().isEmpty();
    }

    /**
     * Returns a {@link Set} view of the keys contained in this map. The set is
     * backed by the map, so changes to the map are reflected in the set, and
     * vice-versa. If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined. The set supports element
     * removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    @Override
    public Set<String> keySet() {
        return getValue().keySet();
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
     * <tt>null</tt> if there was no mapping for <tt>key</tt>. (A <tt>null</tt>
     * return can also indicate that the map previously associated <tt>null</tt>
     * with <tt>key</tt>, if the implementation supports <tt>null</tt> values.)
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
    public Atom<?> put(String key, Atom<?> atom) {
        Objects.requireNonNull(key);
        Objects.requireNonNull(atom);
        return getValue().put(key, atom);
    }

    /**
     * Copies all of the mappings from the specified map to this map (optional
     * operation). The effect of this call is equivalent to that of calling
     * {@link #put(Object,Object) put(k, v)} on this map once for each mapping
     * from key <tt>k</tt> to value <tt>v</tt> in the specified map. The
     * behaviour of this operation is undefined if the specified map is modified
     * while the operation is in progress.
     *
     * @param map mappings to be stored in this map
     * @throws UnsupportedOperationException if the <tt>putAll</tt> operation is
     * not supported by this map
     * @throws ClassCastException if the class of a key or value in the
     * specified map prevents it from being stored in this map
     * @throws NullPointerException if the specified map is null, or if this map
     * does not permit null keys or values, and the specified map contains null
     * keys or values
     * @throws IllegalArgumentException if some property of a key or value in
     * the specified map prevents it from being stored in this map
     */
    @Override
    public void putAll(Map<? extends String, ? extends Atom<?>> map) {
        Objects.requireNonNull(map);
        final TreeMap<String, Atom<?>> DICT = getValue();
        map.entrySet().stream().forEach((Entry<? extends String, ? extends Atom<?>> ENTRY) -> {
            DICT.put(Objects.requireNonNull(ENTRY.getKey()), Objects.requireNonNull(ENTRY.getValue()));
        });
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
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation is
     * not supported by this map
     * @throws ClassCastException if the key is of an inappropriate type for
     * this map
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified key is null and this map
     * does not permit null keys
     * (<a href="{@docRoot}/java/util/Collection.html#optional-restrictions">optional</a>)
     */
    public Atom<?> remove(String key) {
        Objects.requireNonNull(key);
        return getValue().remove(key);
    }

    @Override
    public Atom<?> remove(Object key) {
        Objects.requireNonNull(key);
        stringCheck(key);
        return remove((String) key);
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
        return getValue().size();
    }

    /**
     * Returns a {@link Collection} view of the values contained in this map.
     * The collection is backed by the map, so changes to the map are reflected
     * in the collection, and vice-versa. If the map is modified while an
     * iteration over the collection is in progress (except through the
     * iterator's own <tt>remove</tt> operation), the results of the iteration
     * are undefined. The collection supports element removal, which removes the
     * corresponding mapping from the map, via the <tt>Iterator.remove</tt>,
     * <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt> and <tt>clear</tt> operations. It does not support the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map
     */
    @Override
    public Collection<Atom<?>> values() {
        return getValue().values();
    }

}
