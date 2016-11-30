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
import java.util.Arrays;
import java.util.Collection;
import static java.util.Collections.unmodifiableCollection;
import static java.util.Collections.unmodifiableList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import static java.util.Objects.requireNonNull;
import java.util.RandomAccess;
import java.util.Spliterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.Utilities.clampInt;
import static se.suka.baldr.jbencode.Utilities.randInt;

/**
 * A list of values is encoded as l&lt;contents&gt;e . The contents consist of
 * the Bencoded elements of the list, in order, concatenated. A list consisting
 * of the string "spam" and the number 42 would be encoded as: l4:spami42ee.
 * Note the absence of separators between elements.
 *
 * <p>
 * A thread-safe variant of {@link java.util.ArrayList} in which all mutative
 * operations ({@code add}, {@code set}, and so on) are implemented by making a
 * fresh copy of the underlying array.
 *
 * <p>
 * This is ordinarily too costly, but may be <em>more</em> efficient than
 * alternatives when traversal operations vastly outnumber mutations, and is
 * useful when you cannot or don't want to synchronize traversals, yet need to
 * preclude interference among concurrent threads. The "snapshot" style iterator
 * method uses a reference to the state of the array at the point that the
 * iterator was created. This array never changes during the lifetime of the
 * iterator, so interference is impossible and the iterator is guaranteed not to
 * throw {@code ConcurrentModificationException}. The iterator will not reflect
 * additions, removals, or changes to the list since the iterator was created.
 * Element-changing operations on iterators themselves ({@code remove},
 * {@code set}, and {@code add}) are not supported. These methods throw
 * {@code UnsupportedOperationException}.
 *
 * <p>
 * All {@link Atom} elements are permitted, excluding {@code null}.
 *
 * <p>
 * Memory consistency effects: As with other concurrent collections, actions in
 * a thread prior to placing an object into a {@code CopyOnWriteArrayList}
 * <a href="package-summary.html#MemoryVisibility"><i>happen-before</i></a>
 * actions subsequent to the access or removal of that element from the
 * {@code CopyOnWriteArrayList} in another thread.
 *
 *
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 * @see CopyOnWriteArrayList
 */
public final class AtomList extends Atom implements List<Atom>, RandomAccess, Cloneable, Serializable, Comparable<AtomList> {

    private static final Logger LOGGER = getLogger(AtomList.class);

    /**
     * Backing List
     */
    private CopyOnWriteArrayList<Atom> value;

    /**
     * Constructs an empty list.
     *
     * @throws IllegalArgumentException if the specified initial capacity is
     * negative
     */
    public AtomList() {
        value = new CopyOnWriteArrayList<>();
    }

    /**
     * Constructs a list containing a copy of the elements of the specified
     * collection, in the order they are returned by the collection's iterator.
     *
     * @param c the collection whose elements are to be copied placed into this
     * list
     * @throws NullPointerException if the specified collection is null
     */
    public AtomList(final Collection<? extends Atom> c) {
        this();
        addAll(requireNonNull(c));
    }

    /**
     * Appends the specified element to the end of this list (optional
     * operation).
     *
     * <p>
     * Lists that support this operation may place limitations on what elements
     * may be added to this list. In particular, some lists will refuse to add
     * null elements, and others will impose restrictions on the type of
     * elements that may be added. List classes should clearly specify in their
     * documentation any restrictions on what elements may be added.
     *
     * @param atom element to be appended to this list
     * @return <tt>true</tt> (as specified by {@link Collection#add})
     * @throws UnsupportedOperationException if the <tt>add</tt> operation is
     * not supported by this list
     * @throws ClassCastException if the class of the specified element prevents
     * it from being added to this list
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * @throws IllegalArgumentException if some property of this element
     * prevents it from being added to this list
     */
    @Override
    public boolean add(final Atom atom) {
        return value.add(requireAtom(atom));
    }

    /**
     * Inserts the specified element at the specified position in this list
     * (optional operation). Shifts the element currently at that position (if
     * any) and any subsequent elements to the right (adds one to their
     * indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param atom element to be inserted
     * @throws UnsupportedOperationException if the <tt>add</tt> operation is
     * not supported by this list
     * @throws ClassCastException if the class of the specified element prevents
     * it from being added to this list
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this list
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt; size()</tt>)
     */
    @Override
    public void add(final int index, final Atom atom) {
        value.add(requireNonNull(index), requireAtom(atom));
    }

    /**
     * Appends all of the elements in the specified collection to the end of
     * this list, in the order that they are returned by the specified
     * collection's iterator (optional operation). The behavior of this
     * operation is undefined if the specified collection is modified while the
     * operation is in progress. (Note that this will occur if the specified
     * collection is this list, and it's nonempty.)
     *
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation is
     * not supported by this list
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it from being added to this list
     * @throws NullPointerException if the specified collection contains one or
     * more null elements and this list does not permit null elements, or if the
     * specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this list
     * @see #add(Object)
     */
    @Override
    public boolean addAll(final Collection<? extends Atom> c) {
        final Collection<? extends Atom> uc = unmodifiableCollection(requireNonNull(c));
        uc.stream().forEachOrdered(atom -> value.add(requireAtom(atom)));
        return !uc.isEmpty();
    }

    /**
     * Inserts all of the elements in the specified collection into this list at
     * the specified position (optional operation). Shifts the element currently
     * at that position (if any) and any subsequent elements to the right
     * (increases their indices). The new elements will appear in this list in
     * the order that they are returned by the specified collection's iterator.
     * The behaviour of this operation is undefined if the specified collection
     * is modified while the operation is in progress. (Note that this will
     * occur if the specified collection is this list, and it's nonempty.)
     *
     * @param index index at which to insert the first element from the
     * specified collection
     * @param c collection containing elements to be added to this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>addAll</tt> operation is
     * not supported by this list
     * @throws ClassCastException if the class of an element of the specified
     * collection prevents it from being added to this list
     * @throws NullPointerException if the specified collection contains one or
     * more null elements and this list does not permit null elements, or if the
     * specified collection is null
     * @throws IllegalArgumentException if some property of an element of the
     * specified collection prevents it from being added to this list
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt; size()</tt>)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends Atom> c) {
        final AtomicInteger count = new AtomicInteger(requireNonNull(index));
        final Collection<? extends Atom> uc = unmodifiableCollection(requireNonNull(c));
        uc.stream().forEachOrdered(atom -> value.add(count.getAndAdd(1), requireAtom(atom)));
        return !uc.isEmpty();
    }

    /**
     * Appends all of the elements in the specified collection that are not
     * already contained in this list, to the end of this list, in the order
     * that they are returned by the specified collection's iterator.
     *
     * @param c collection containing elements to be added to this list
     * @return the number of elements added
     * @throws NullPointerException if the specified collection is null
     */
    public int addAllAbsent(final Collection<? extends Atom> c) {
        return (int) unmodifiableCollection(requireNonNull(c)).stream()
                .map(atom -> value.addIfAbsent(requireAtom(atom)))
                .count();
    }

    /**
     * Appends the element, if not present.
     *
     * @param atom element to be added to this list, if absent
     * @return {@code true} if the element was added
     */
    public boolean addIfAbsent(final Atom atom) {
        return value.addIfAbsent(requireAtom(atom));
    }

    /**
     *
     * @return The length of the encoded string when Bencoded.
     */
    @Override
    public int bLength() {
        return 2 + value.stream().parallel()
                .collect(summingInt(atom -> atom.bLength()));
    }

    /**
     * Removes all of the elements from this list (optional operation). The list
     * will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation is
     * not supported by this list
     */
    @Override
    public void clear() {
        value.clear();
    }

    /**
     * Returns a shallow copy of this list. (The elements themselves are not
     * copied.)
     *
     * @return a clone of this list
     */
    @Override
    public AtomList clone() {
        try {
            AtomList atomList = (AtomList) super.clone();
            atomList.value = new CopyOnWriteArrayList<>(value);
            return atomList;
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element. More
     * formally, returns <tt>true</tt> if and only if this list contains at
     * least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param atom
     * @return <tt>true</tt> if this list contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public boolean contains(final Object atom) {
        return value.contains(requireAtom(atom));
    }

    /**
     * Returns <tt>true</tt> if this list contains all of the elements of the
     * specified collection.
     *
     * @param c collection to be checked for containment in this list
     * @return <tt>true</tt> if this list contains all of the elements of the
     * specified collection
     * @throws ClassCastException if the types of one or more elements in the
     * specified collection are incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified collection contains one or
     * more null elements and this list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>), or if the
     * specified collection is null
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
        return value.containsAll(unmodifiableCollection(requireNonNull(c)));
    }

    /**
     * Returns a deep copy of this list. (The elements themselves not copied.)
     *
     * @return a copy of this list
     */
    @Override
    public AtomList copy() {
        return (AtomList) value.stream()
                .map(atom -> (Atom) atom.copy())
                .collect(toList());
    }

    /**
     *
     * @return
     */
    @Override
    public String encode() {
        return value.stream()
                .map(atom -> atom.encode())
                .collect(joining("", "l", "e"));
    }

    @Override
    public void forEach(Consumer<? super Atom> action) {
        value.forEach(action);
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt;= size()</tt>)
     */
    @Override
    public Atom get(final int index) {
        return value.get(requireNonNull(index));
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. More
     * formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param atom
     * @return the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public int indexOf(final Object atom) {
        return value.indexOf(requireNonNull(atom));
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, searching forwards from {@code index}, or returns -1 if the
     * element is not found. More formally, returns the lowest index {@code i}
     * such that
     * <tt>(i&nbsp;&gt;=&nbsp;index&nbsp;&amp;&amp;&nbsp;(e==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;e.equals(get(i))))</tt>,
     * or -1 if there is no such index.
     *
     * @param atom element to search for
     * @param startIndex index to start searching from
     * @return the index of the first occurrence of the element in this list at
     * position {@code index} or later in the list; {@code -1} if the element is
     * not found.
     * @throws IndexOutOfBoundsException if the specified index is negative
     */
    public int indexOf(final Atom atom, final int startIndex) {
        return value.indexOf(requireNonNull(atom), startIndex);
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<Atom> iterator() {
        return value.iterator();
    }

    /**
     * Returns the index of the last occurrence of the specified element in this
     * list, or -1 if this list does not contain the element. More formally,
     * returns the highest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param atom element to search for
     * @return the index of the last occurrence of the specified element in this
     * list, or -1 if this list does not contain the element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public int lastIndexOf(final Object atom) {
        return value.lastIndexOf(requireNonNull(atom));
    }

    /**
     * Returns the index of the last occurrence of the specified element in this
     * list, searching backwards from {@code index}, or returns -1 if the
     * element is not found. More formally, returns the highest index {@code i}
     * such that
     * <tt>(i&nbsp;&lt;=&nbsp;index&nbsp;&amp;&amp;&nbsp;(e==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;e.equals(get(i))))</tt>,
     * or -1 if there is no such index.
     *
     * @param atom element to search for
     * @param startIndex index to start searching backwards from
     * @return the index of the last occurrence of the element at position less
     * than or equal to {@code index} in this list; -1 if the element is not
     * found.
     * @throws IndexOutOfBoundsException if the specified index is greater than
     * or equal to the current size of this list
     */
    public int lastIndexOf(final Atom atom, final int startIndex) {
        return value.lastIndexOf(requireAtom(atom), startIndex);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @return a list iterator over the elements in this list (in proper
     * sequence)
     */
    @Override
    public ListIterator<Atom> listIterator() {
        return value.listIterator();
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list. The specified
     * index indicates the first element that would be returned by an initial
     * call to {@link ListIterator#next next}. An initial call to
     * {@link ListIterator#previous previous} would return the element with the
     * specified index minus one.
     *
     * @param index index of the first element to be returned from the list
     * iterator (by a call to {@link ListIterator#next next})
     * @return a list iterator over the elements in this list (in proper
     * sequence), starting at the specified position in the list
     * @throws IndexOutOfBoundsException if the index is out of range
     * ({@code index < 0 || index > size()})
     */
    @Override
    public ListIterator<Atom> listIterator(final int index) {
        return value.listIterator(requireNonNull(index));
    }

    /**
     *
     * @param howMany
     * @return
     */
    public AtomList getRandomSlice(final int howMany) {
        final List<Atom> ul = unmodifiableList(value);
        final int size = ul.size();
        final int hm = clampInt(requireNonNull(howMany), 0, size);
        return (AtomList) range(0, hm)
                .mapToObj(i -> ul.get(randInt(0, size)))
                .collect(toList());
    }

    /**
     * Removes the element at the specified position in this list (optional
     * operation). Shifts any subsequent elements to the left (subtracts one
     * from their indices). Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation is
     * not supported by this list
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt;= size()</tt>)
     */
    @Override
    public Atom remove(final int index) {
        return value.remove(requireNonNull(index).intValue());
    }

    /**
     * Removes the first occurrence of the specified element from this list, if
     * it is present (optional operation). If this list does not contain the
     * element, it is unchanged. More formally, removes the element with the
     * lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
     * (if such an element exists). Returns <tt>true</tt> if this list contained
     * the specified element (or equivalently, if this list changed as a result
     * of the call).
     *
     * @param atom
     * @return <tt>true</tt> if this list contained the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation is
     * not supported by this list
     */
    @Override
    public boolean remove(final Object atom) {
        return value.remove(requireAtom(atom));
    }

    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection (optional operation).
     *
     * @param c collection containing elements to be removed from this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>removeAll</tt> operation
     * is not supported by this list
     * @throws ClassCastException if the class of an element of this list is
     * incompatible with the specified collection
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the
     * specified collection does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>), or if the
     * specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        return value.removeAll(unmodifiableCollection(requireNonNull(c)));
    }

    /**
     * Removes all of the elements of this collection that satisfy the given
     * predicate. Errors or runtime exceptions thrown during iteration or by the
     * predicate are relayed to the caller.
     *
     * @param filter a predicate which returns {@code true} for elements to be
     * removed
     * @return {@code true} if any elements were removed
     * @throws NullPointerException if the specified filter is null
     * @throws UnsupportedOperationException if elements cannot be removed from
     * this collection. Implementations may throw this exception if a matching
     * element cannot be removed or if, in general, removal is not supported.
     */
    @Override
    public boolean removeIf(Predicate<? super Atom> filter) {
        return value.removeIf(filter);
    }

    /**
     * Replaces each element of this list with the result of applying the
     * operator to that element. Errors or runtime exceptions thrown by the
     * operator are relayed to the caller.
     *
     * The default implementation is equivalent to, for this {@code list}:
     * <pre>{@code
     *     final ListIterator<E> li = list.listIterator();
     *     while (li.hasNext()) {
     *         li.set(operator.apply(li.next()));
     *     }
     * }</pre>
     *
     * If the list's list-iterator does not support the {@code set} operation
     * then an {@code UnsupportedOperationException} will be thrown when
     * replacing the first element.
     *
     * @param operator the operator to apply to each element
     * @throws UnsupportedOperationException if this list is unmodifiable.
     * Implementations may throw this exception if an element cannot be replaced
     * or if, in general, modification is not supported
     * @throws NullPointerException if the specified operator is null or if the
     * operator result is a null value and this list does not permit null
     * elements (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public void replaceAll(UnaryOperator<Atom> operator) {
        value.replaceAll(operator);
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection (optional operation). In other words, removes from
     * this list all of its elements that are not contained in the specified
     * collection.
     *
     * @param c collection containing elements to be retained in this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws UnsupportedOperationException if the <tt>retainAll</tt> operation
     * is not supported by this list
     * @throws ClassCastException if the class of an element of this list is
     * incompatible with the specified collection
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if this list contains a null element and the
     * specified collection does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>), or if the
     * specified collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        return value.retainAll(unmodifiableCollection(requireNonNull(c)));
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element (optional operation).
     *
     * @param index index of the element to replace
     * @param atom element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws UnsupportedOperationException if the <tt>set</tt> operation is
     * not supported by this list
     * @throws ClassCastException if the class of the specified element prevents
     * it from being added to this list
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * @throws IllegalArgumentException if some property of the specified
     * element prevents it from being added to this list
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt;= size()</tt>)
     */
    @Override
    public Atom set(final int index, final Atom atom) {
        return value.set(requireNonNull(index), requireAtom(atom));
    }

    /**
     * Returns the number of elements in this list. If this list contains more
     * than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this list
     */
    @Override
    public int size() {
        return value.size();
    }

    /**
     * Sorts this list according to the order induced by the specified
     * {@link Comparator}.
     *
     * <p>
     * All elements in this list must be <i>mutually comparable</i> using the
     * specified comparator (that is, {@code c.compare(e1, e2)} must not throw a
     * {@code ClassCastException} for any elements {@code e1} and {@code e2} in
     * the list).
     *
     * <p>
     * If the specified comparator is {@code null} then all elements in this
     * list must implement the {@link Comparable} interface and the elements'
     * {@linkplain Comparable natural ordering} should be used.
     *
     * <p>
     * This list must be modifiable, but need not be resizable.
     *
     * <p>
     * The implementation takes equal advantage of ascending and descending
     * order in its input array, and can take advantage of ascending and
     * descending order in different parts of the same input array. It is
     * well-suited to merging two or more sorted arrays: simply concatenate the
     * arrays and sort the resulting array.
     *
     * <p>
     * The implementation was adapted from Tim Peters's list sort for Python
     * (<a href="http://svn.python.org/projects/python/trunk/Objects/listsort.txt">
     * TimSort</a>). It uses techniques from Peter McIlroy's "Optimistic Sorting
     * and Information Theoretic Complexity", in Proceedings of the Fourth
     * Annual ACM-SIAM Symposium on Discrete Algorithms, pp 467-474, January
     * 1993.
     *
     * @param c the {@code Comparator} used to compare list elements. A
     * {@code null} value indicates that the elements'
     * {@linkplain Comparable natural ordering} should be used
     * @throws ClassCastException if the list contains elements that are not
     * <i>mutually comparable</i> using the specified comparator
     * @throws UnsupportedOperationException if the list's list-iterator does
     * not support the {@code set} operation
     * @throws IllegalArgumentException
     * (<a href="Collection.html#optional-restrictions">optional</a>) if the
     * comparator is found to violate the {@link Comparator} contract
     */
    @Override
    public void sort(Comparator<? super Atom> c) {
        value.sort(c);
    }

    /**
     * Returns a {@link Spliterator} over the elements in this list.
     *
     * <p>
     * The {@code Spliterator} reports {@link Spliterator#IMMUTABLE},
     * {@link Spliterator#ORDERED}, {@link Spliterator#SIZED}, and
     * {@link Spliterator#SUBSIZED}.
     *
     * <p>
     * The spliterator provides a snapshot of the state of the list when the
     * spliterator was constructed. No synchronization is needed while operating
     * on the spliterator.
     *
     * @return a {@code Spliterator} over the elements in this list
     */
    @Override
    public Spliterator<Atom> spliterator() {
        return value.spliterator();
    }

    /**
     * Returns a view of the portion of this list between the specified
     * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive. (If
     * <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is
     * empty.) The returned list is backed by this list, so non-structural
     * changes in the returned list are reflected in this list, and vice-versa.
     * The returned list supports all of the optional list operations supported
     * by this list.<p>
     *
     * This method eliminates the need for explicit range operations (of the
     * sort that commonly exist for arrays). Any operation that expects a list
     * can be used as a range operation by passing a subList view instead of a
     * whole list. For example, the following idiom removes a range of elements
     * from a list:
     * <pre>{@code
     *      list.subList(from, to).clear();
     * }</pre> Similar idioms may be constructed for <tt>indexOf</tt> and
     * <tt>lastIndexOf</tt>, and all of the algorithms in the
     * <tt>Collections</tt> class can be applied to a subList.<p>
     *
     * The semantics of the list returned by this method become undefined if the
     * backing list (i.e., this list) is <i>structurally modified</i> in any way
     * other than via the returned list. (Structural modifications are those
     * that change the size of this list, or otherwise perturb it in such a
     * fashion that iterations in progress may yield incorrect results.)
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex high endpoint (exclusive) of the subList
     * @return a view of the specified range within this list
     * @throws IndexOutOfBoundsException for an illegal endpoint index value
     * (<tt>fromIndex &lt; 0 || toIndex &gt; size || fromIndex &gt;
     * toIndex</tt>)
     */
    @Override
    public List<Atom> subList(final int fromIndex, final int toIndex) {
        return value.subList(requireNonNull(fromIndex), requireNonNull(toIndex));
    }

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element).
     *
     * <p>
     * The returned array will be "safe" in that no references to it are
     * maintained by this list. (In other words, this method must allocate a new
     * array even if this list is backed by an array). The caller is thus free
     * to modify the returned array.
     *
     * <p>
     * This method acts as bridge between array-based and collection-based APIs.
     *
     * @return an array containing all of the elements in this list in proper
     * sequence
     * @see Arrays#asList(Object[])
     */
    @Override
    public Object[] toArray() {
        return value.toArray();
    }

    /**
     * Returns an array containing all of the elements in this list in proper
     * sequence (from first to last element); the runtime type of the returned
     * array is that of the specified array. If the list fits in the specified
     * array, it is returned therein. Otherwise, a new array is allocated with
     * the runtime type of the specified array and the size of this list.
     *
     * <p>
     * If the list fits in the specified array with room to spare (i.e., the
     * array has more elements than the list), the element in the array
     * immediately following the end of the list is set to <tt>null</tt>. (This
     * is useful in determining the length of the list <i>only</i> if the caller
     * knows that the list does not contain any null elements.)
     *
     * <p>
     * Like the {@link #toArray()} method, this method acts as bridge between
     * array-based and collection-based APIs. Further, this method allows
     * precise control over the runtime type of the output array, and may, under
     * certain circumstances, be used to save allocation costs.
     *
     * <p>
     * Suppose <tt>x</tt> is a list known to contain only strings. The following
     * code can be used to dump the list into a newly allocated array of
     * <tt>String</tt>:
     *
     * <pre>{@code
     *     String[] y = x.toArray(new String[0]);
     * }</pre>
     *
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     *
     * @param <T>
     * @param a the array into which the elements of this list are to be stored,
     * if it is big enough; otherwise, a new array of the same runtime type is
     * allocated for this purpose.
     * @return an array containing the elements of this list
     * @throws ArrayStoreException if the runtime type of the specified array is
     * not a supertype of the runtime type of every element in this list
     * @throws NullPointerException if the specified array is null
     */
    @Override
    public <T> T[] toArray(final T[] a) {
        // TODO: Need to look at this!
        return value.toArray(a);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AtomList) {
            return value.equals(((AtomList) obj).value);
        }
        return false;
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public int compareTo(AtomList anotherAtomList) {
        return value.toString().compareTo(anotherAtomList.toString());
    }

}
