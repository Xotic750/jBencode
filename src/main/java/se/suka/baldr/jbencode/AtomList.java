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
import static java.util.Collections.unmodifiableList;
import java.util.List;
import static java.util.Objects.requireNonNull;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.UnaryOperator;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.summingInt;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.IntStream.range;
import static se.suka.baldr.jbencode.Atom.requireAtomCollection;
import static se.suka.baldr.jbencode.Atom.requireNonNullAtom;
import static se.suka.baldr.jbencode.Utilities.clamp;
import static se.suka.baldr.jbencode.Utilities.randInt;
import static se.suka.baldr.jbencode.Utilities.stringToAsciiBytes;

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
public final class AtomList extends CopyOnWriteArrayList<Atom> implements Atom, Comparable<AtomList> {

    private static final long serialVersionUID = -1527286384432951976L;

    //private static final Logger LOGGER = getLogger(AtomList.class);
    /**
     * Test if an object reference is an instance of {@code AtomList}.
     *
     * @param o the object reference to check is instance of {@code AtomList}
     * @return {@code true} if the object is an instance of {@code AtomList},
     * otherwise {@code false}
     */
    public static boolean isAtomList(Object o) {
        return o instanceof AtomList;
    }

    /**
     * Test if an object reference is an instance of {@code AtomList}, throw a
     * {@link ClassCastException} if it is not, otherwise return the reference
     * object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @throws ClassCastException if {@code obj} is not an instance of
     * {@code AtomList}
     * @return the object reference
     */
    public static <T> T requireAtomList(T o) {
        return requireAtomList(o, "");
    }

    /**
     * Test if an object reference is an instance of {@code AtomList}, throw a
     * {@link ClassCastException} if it is not, otherwise return the reference
     * object.
     *
     * @param <T> the type of the value being boxed
     * @param o the object reference to check
     * @param message detail message to be used in the event that a
     * {@code ClassCastException} is thrown
     * @throws ClassCastException if {@code obj} is not an instance of
     * {@code AtomList}
     * @return the object reference
     */
    public static <T> T requireAtomList(T o, String message) {
        if (!isAtomList(o)) {
            throw new ClassCastException(message);
        }
        return o;
    }

    /**
     * Constructs an empty list.
     *
     */
    public AtomList() {
        super();
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
        super(requireAtomCollection(c));
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
        return super.add(requireNonNull(atom));
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
        super.add(index, requireNonNull(atom));
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
        return super.addAll(requireAtomCollection(c));
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
        return super.addAll(index, requireAtomCollection(c));
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
    @Override
    public int addAllAbsent(final Collection<? extends Atom> c) {
        return super.addAllAbsent(requireAtomCollection(c));
    }

    /**
     * Appends the element, if not present.
     *
     * @param atom element to be added to this list, if absent
     * @return {@code true} if the element was added
     * @throws NullPointerException if the specified element is null
     */
    @Override
    public boolean addIfAbsent(final Atom atom) {
        return super.addIfAbsent(requireNonNull(atom));
    }

    /**
     * Returns the length of the Bencoded string of this {@link Atom}. This
     * method is faster than performing an <code>encode().length()</code>.
     *
     * @return The length of the Becoded string
     */
    @Override
    public int bLength() {
        return 2 + super.stream().parallel()
                .collect(summingInt(atom -> atom.bLength()));
    }

    /**
     * Returns a shallow copy of this list. (The elements themselves are not
     * copied.)
     *
     * @return a clone of this list
     */
    @Override
    public AtomList clone() {
        return (AtomList) super.clone();
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
        return super.contains(requireNonNullAtom(o));
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
        return super.containsAll(requireAtomCollection(c));
    }

    /**
     * Returns a deep copy of this {@link Atom}.
     *
     * @return a copy of this {@link Atom}
     */
    @Override
    public AtomList copy() {
        return super.stream()
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
        return super.stream()
                .map(atom -> atom.encode())
                .collect(joining("", "l", "e"));
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
        return super.indexOf(requireNonNullAtom(o));
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
    @Override
    public int indexOf(final Atom atom, final int startIndex) {
        return super.indexOf(requireNonNull(atom), startIndex);
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
        return super.lastIndexOf(requireNonNullAtom(o));
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
    @Override
    public int lastIndexOf(final Atom atom, final int startIndex) {
        return super.lastIndexOf(requireNonNull(atom), startIndex);
    }

    /**
     *
     * @param howMany
     * @return
     */
    public AtomList getRandomSlice(final int howMany) {
        final List<Atom> ul = unmodifiableList(this);
        return range(0, clamp(howMany, 0, ul.size()))
                .mapToObj(i -> ul.get(randInt(0, ul.size())).copy())
                .collect(toCollection(AtomList::new));
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
        return super.remove(requireNonNullAtom(o));
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
        return super.removeAll(requireAtomCollection(c));
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
        super.replaceAll(a -> requireNonNullAtom(operator.apply(a)));
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
        return super.retainAll(requireAtomCollection(c));
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
        return super.set(index, requireNonNull(atom));
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
        return 119 + super.hashCode();
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
            return super.equals(obj);
        }
        return false;
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
        return encode().compareTo(anotherAtomList.encode());
    }

}
