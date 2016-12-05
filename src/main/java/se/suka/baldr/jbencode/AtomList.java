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
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.IntStream.range;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.getLogger;
import static se.suka.baldr.jbencode.Utilities.clamp;
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
public final class AtomList extends Atom implements List<Atom>, RandomAccess, Cloneable, Comparable<AtomList> {

    private static final long serialVersionUID = -1527286384432951976L;

    private static final Logger LOGGER = getLogger(AtomList.class);

    /**
     * Backing List
     */
    private CopyOnWriteArrayList<Atom> value;

    /**
     * Constructs an empty list.
     *
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
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean add(final Atom atom) {
        return value.add(requireNonNull(atom));
    }

    /**
     * Inserts the specified element at the specified position in this list
     * (optional operation). Shifts the element currently at that position (if
     * any) and any subsequent elements to the right (adds one to their
     * indices).
     *
     * @param index index at which the specified element is to be inserted
     * @param atom element to be inserted
     * @throws NullPointerException if the specified element is null
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt; size()</tt>)
     */
    @Override
    public void add(final int index, final Atom atom) {
        value.add(index, requireNonNull(atom));
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
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     * @see #add(Object)
     */
    @Override
    public boolean addAll(final Collection<? extends Atom> c) {
        final Collection<? extends Atom> uc = unmodifiableCollection(requireNonNull(c));
        uc.stream().parallel().forEach(atom -> requireNonNull(atom));
        return value.addAll(uc);
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
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt; size()</tt>)
     */
    @Override
    public boolean addAll(final int index, final Collection<? extends Atom> c) {
        final Collection<? extends Atom> uc = unmodifiableCollection(requireNonNull(c));
        uc.stream().parallel().forEach(atom -> requireNonNull(atom));
        return value.addAll(index, uc);
    }

    /**
     * Appends all of the elements in the specified collection that are not
     * already contained in this list, to the end of this list, in the order
     * that they are returned by the specified collection's iterator.
     *
     * @param c collection containing elements to be added to this list
     * @return the number of elements added
     * @throws NullPointerException if the specified collection contains one or
     * more null elements, or if the specified collection is null
     */
    public int addAllAbsent(final Collection<? extends Atom> c) {
        final Collection<? extends Atom> uc = unmodifiableCollection(requireNonNull(c));
        uc.stream().parallel().forEach(atom -> requireNonNull(atom));
        return value.addAllAbsent(uc);
    }

    /**
     * Appends the element, if not present.
     *
     * @param atom element to be added to this list, if absent
     * @return {@code true} if the element was added
     */
    public boolean addIfAbsent(final Atom atom) {
        return value.addIfAbsent(requireNonNull(atom));
    }

    /**
     * Returns the length of the Bencoded string of this {@link Atom}. This
     * method is faster than performing an <code>encode().length()</code>.
     *
     * @return The length of the Becoded string
     */
    @Override
    public int bLength() {
        return 2 + value.stream().parallel()
                .collect(summingInt(atom -> atom.bLength()));
    }

    /**
     * Removes all of the elements from this list (optional operation). The list
     * will be empty after this call returns.
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
            final AtomList atomList = (AtomList) super.clone();
            atomList.value = new CopyOnWriteArrayList<>(value);
            return atomList;
        } catch (final CloneNotSupportedException e) {
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
     * @param o
     * @return <tt>true</tt> if this list contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean contains(final Object o) {
        return value.contains(requireAtom(requireNonNull(o)));
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
     * @throws NullPointerException if the specified collection contains one or
     * more null elements
     * @see #contains(Object)
     */
    @Override
    public boolean containsAll(final Collection<?> c) {
        final Collection<?> uc = unmodifiableCollection(requireNonNull(c));
        uc.stream().parallel().forEach(atom -> requireAtom(requireNonNull(atom)));
        return value.containsAll(uc);
    }

    /**
     * Returns a deep copy of this {@link Atom}.
     *
     * @return a copy of this {@link Atom}
     */
    @Override
    public AtomList copy() {
        return unmodifiableList(value).stream()
                .map(atom -> atom.copy())
                .collect(toCollection(AtomList::new));
    }

    /**
     * Returns the Bencoded string of this {@link Atom}.
     *
     * @return The Benoded string
     */
    @Override
    public String encode() {
        return value.stream()
                .map(atom -> atom.encode())
                .collect(joining("", "l", "e"));
    }

    /**
     * Performs the given action for each element of the {@code Iterable} until
     * all elements have been processed or the action throws an exception.
     * Unless otherwise specified by the implementing class, actions are
     * performed in the order of iteration (if an iteration order is specified).
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is {@code null}
     */
    @Override
    public void forEach(Consumer<? super Atom> action) {
        value.forEach(requireNonNull(action));
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
        return value.get(index);
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element.
     *
     * @param o
     * @return the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public int indexOf(final Object o) {
        return value.indexOf(requireAtom(requireNonNull(o)));
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, searching forwards from {@code index}, or returns -1 if the
     * element is not found.
     *
     * @param atom element to search for
     * @param startIndex index to start searching from
     * @return the index of the first occurrence of the element in this list at
     * position {@code index} or later in the list; {@code -1} if the element is
     * not found.
     * @throws NullPointerException if the specified element is null
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
     * @param o element to search for
     * @return the index of the last occurrence of the specified element in this
     * list, or -1 if this list does not contain the element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public int lastIndexOf(final Object o) {
        return value.lastIndexOf(requireAtom(requireNonNull(o)));
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
     * @throws NullPointerException if the specified element is null
     */
    public int lastIndexOf(final Atom atom, final int startIndex) {
        return value.lastIndexOf(requireNonNull(atom), startIndex);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @throws UnsupportedOperationException for unsupported {@code set} or
     * {@code add}
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
     * <p>
     * The returned iterator provides a snapshot of the state of the list when
     * the iterator was constructed. No synchronization is needed while
     * traversing the iterator. The iterator does <em>NOT</em> support the
     * {@code remove}, {@code set} or {@code add} methods.
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
        return value.listIterator(index);
    }

    /**
     *
     * @param howMany
     * @return
     */
    public AtomList getRandomSlice(final int howMany) {
        final List<Atom> ul = unmodifiableList(value);
        return range(0, clamp(howMany, 0, ul.size()))
                .mapToObj(i -> ul.get(randInt(0, ul.size())).copy())
                .collect(toCollection(AtomList::new));
    }

    /**
     * Removes the element at the specified position in this list (optional
     * operation). Shifts any subsequent elements to the left (subtracts one
     * from their indices). Returns the element that was removed from the list.
     *
     * @param index the index of the element to be removed
     * @return the element previously at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt;= size()</tt>)
     */
    @Override
    public Atom remove(final int index) {
        return value.remove(index);
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
     * @param o
     * @return <tt>true</tt> if this list contained the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean remove(final Object o) {
        return value.remove(requireAtom(requireNonNull(o)));
    }

    /**
     * Removes from this list all of its elements that are contained in the
     * specified collection (optional operation).
     *
     * @param c collection containing elements to be removed from this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list is
     * incompatible with the specified collection
     * @throws NullPointerException if this list contains a null element
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean removeAll(final Collection<?> c) {
        final Collection<?> uc = unmodifiableCollection(requireNonNull(c));
        uc.stream().parallel().forEach(atom -> requireAtom(requireNonNull(atom)));
        return value.removeAll(uc);
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
     */
    @Override
    public boolean removeIf(final Predicate<? super Atom> filter) {
        return value.removeIf(requireNonNull(filter));
    }

    /**
     * Replaces each element of this list with the result of applying the
     * operator to that element. Errors or runtime exceptions thrown by the
     * operator are relayed to the caller.
     *
     * @param operator the operator to apply to each element
     * @throws ClassCastException if the class of the operators result is
     * incompatible with the specified collection
     * @throws NullPointerException if the specified operator is null or if the
     * operator result is a null value and this list does not permit null
     * elements
     */
    @Override
    public void replaceAll(final UnaryOperator<Atom> operator) {
        value.replaceAll(a -> requireAtom(requireNonNull(operator).apply(a)));
    }

    /**
     * Retains only the elements in this list that are contained in the
     * specified collection. In other words, removes from this list all of its
     * elements that are not contained in the specified collection.
     *
     * @param c collection containing elements to be retained in this list
     * @return <tt>true</tt> if this list changed as a result of the call
     * @throws ClassCastException if the class of an element of this list is
     * incompatible with the specified collection
     * @throws NullPointerException if this list contains a null element and the
     * specified collection does not permit null elements, or if the specified
     * collection is null
     * @see #remove(Object)
     * @see #contains(Object)
     */
    @Override
    public boolean retainAll(final Collection<?> c) {
        final Collection<?> uc = unmodifiableCollection(requireNonNull(c));
        uc.stream().parallel().forEach(atom -> requireAtom(requireNonNull(atom)));
        return value.retainAll(uc);
    }

    /**
     * Replaces the element at the specified position in this list with the
     * specified element.
     *
     * @param index index of the element to replace
     * @param atom element to be stored at the specified position
     * @return the element previously at the specified position
     * @throws NullPointerException if the specified element is null
     * @throws IndexOutOfBoundsException if the index is out of range (<tt>index
     * &lt; 0 || index &gt;= size()</tt>)
     */
    @Override
    public Atom set(final int index, final Atom atom) {
        return value.set(index, requireNonNull(atom));
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
     * @throws IllegalArgumentException if the comparator is found to violate
     * the {@link Comparator} contract
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
        return value.subList(fromIndex, toIndex);
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
        /*
        TODO: 
            Detects such calls, whose array type parameter does not match the
            Collection's type parameter. The collection's type parameter should
            be assignable to the array type. For raw Collections, the hint
            checks that the array type is actually assignable to the casted-to
            array type.
            The hint offers to change the newly created array type, or to change
            the toArray parameter to new Type[],but the fix is not available if 
            the collection expression may have some side effects.
         */
        return value.toArray(a);
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
        hash = 17 * hash + Objects.hashCode(this.value);
        return hash;
    }

    /**
     * Compares the specified object with this list for equality. Returns
     * {@code true} if the specified object is the same object as this object,
     * or if it is also a {@link List} and the sequence of elements returned by
     * an {@linkplain List#iterator() iterator} over the specified list is the
     * same as the sequence returned by an iterator over this list. The two
     * sequences are considered to be the same if they have the same length and
     * corresponding elements at the same position in the sequence are
     * <em>equal</em>. Two elements {@code e1} and {@code e2} are considered
     * <em>equal</em> if {@code (e1==null ? e2==null : e1.equals(e2))}.
     *
     * @param obj the object to be compared for equality with this list
     * @return {@code true} if the specified object is equal to this list
     */
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

    /**
     * Returns a {@code String} object representing this {@code AtomList}'s
     * value.
     *
     * @return a string representation of the value of this object
     */
    @Override
    public String toString() {
        return value.toString();
    }

    /**
     * Compares two {@code AtomList} objects lexicographically using the
     * {@code encode} method.
     *
     * @param anotherAtomList the {@code AtomList} to be compared.
     * @throws NullPointerException if the specified {@code anotherAtomList} is
     * {@code null}
     * @return the value {@code 0} if the argument {@code AtomList} is equal to
     * this {@code AtomList}; a value less than {@code 0} if this
     * {@code AtomString} is lexicographically less than the {@code AtomList}
     * argument; and a value greater than {@code 0} if this {@code AtomList} is
     * lexicographically greater than the {@code AtomList} argument.
     */
    @Override
    public int compareTo(final AtomList anotherAtomList) {
        return encode().compareTo(requireNonNull(anotherAtomList).encode());
    }

}
