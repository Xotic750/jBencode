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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import static se.suka.baldr.jbencode.Utils.randInt;

/**
 * A list of values is encoded as l&lt;contents&gt;e . The contents consist of
 * the Bencoded elements of the list, in order, concatenated. A list consisting
 * of the string "spam" and the number 42 would be encoded as: l4:spami42ee.
 * Note the absence of separators between elements.
 *
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomList extends Atom implements List<Atom>, Serializable {

    /**
     * Backing List
     */
    private final List<Atom> value;

    /**
     * Object on which to synchronize
     */
    private final Object mutex;

    /**
     *
     */
    public AtomList() {
        this(10);
    }

    /**
     *
     * @param initialCapacity
     * @param initialCapacity
     */
    public AtomList(int initialCapacity) {
        value = Collections.synchronizedList(new ArrayList<>(initialCapacity));
        mutex = this;
    }

    /**
     *
     * @param atomList
     */
    public AtomList(final AtomList atomList) {
        this(atomList.size());
        synchronized (atomList) {
            atomList.stream().forEachOrdered(atom -> {
                if (atom instanceof AtomInteger) {
                    add(new AtomInteger((AtomInteger) atom));
                } else if (atom instanceof AtomString) {
                    add(new AtomString((AtomString) atom));
                } else if (atom instanceof AtomList) {
                    add(new AtomList((AtomList) atom));
                } else if (atom instanceof AtomDictionary) {
                    add(new AtomDictionary((AtomDictionary) atom));
                } else {
                    System.err.println("AtomList: unknown Atom type");
                }
            });
        }
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
    public final boolean add(final Atom atom) {
        return value.add(Objects.requireNonNull(atom));
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
    public final void add(final int index, final Atom atom) {
        value.add(index, Objects.requireNonNull(atom));
    }

    /**
     * For private use with addAll
     *
     * @param index
     * @param syncCol
     * @return
     */
    private boolean addAllBase(final int index, final Collection<? extends Atom> syncCol) {
        final AtomicInteger count = new AtomicInteger(index);
        syncCol.stream().forEachOrdered(atom -> add(count.getAndAdd(1), atom));
        return !syncCol.isEmpty();
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
    public final boolean addAll(final Collection<? extends Atom> c) {
        final Collection<? extends Atom> syncCol = Collections.synchronizedCollection(c);
        synchronized (syncCol) {
            return addAllBase(syncCol.size(), syncCol);
        }
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
    public final boolean addAll(final int index, final Collection<? extends Atom> c) {
        final Collection<? extends Atom> syncCol = Collections.synchronizedCollection(c);
        synchronized (syncCol) {
            return addAllBase(index, syncCol);
        }
    }

    /**
     *
     * @return The length of the encoded string when Bencoded.
     */
    @Override
    public final int bLength() {
        synchronized (mutex) {
            return value.stream().map(atom -> atom.bLength()).reduce(2, Integer::sum);
        }
    }

    /**
     * Removes all of the elements from this list (optional operation). The list
     * will be empty after this call returns.
     *
     * @throws UnsupportedOperationException if the <tt>clear</tt> operation is
     * not supported by this list
     */
    @Override
    public final void clear() {
        value.clear();
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element. More
     * formally, returns <tt>true</tt> if and only if this list contains at
     * least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @return <tt>true</tt> if this list contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public final boolean contains(final Object o) {
        return value.contains(o);
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
    public final boolean containsAll(final Collection<?> c) {
        return value.containsAll(Collections.synchronizedCollection(c));
    }

    /**
     *
     * @return
     */
    @Override
    public final String encode() {
        final StringBuilder encoded = new StringBuilder("l");
        synchronized (mutex) {
            value.stream().forEachOrdered(atom -> encoded.append(atom.encode()));
        }
        return encoded.append("e").toString();
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
    public final Atom get(final int index) {
        return value.get(index);
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. More
     * formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param o element to search for
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
    public final int indexOf(final Object o) {
        return value.indexOf(o);
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    @Override
    public final boolean isEmpty() {
        return value.isEmpty();
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public final Iterator<Atom> iterator() {
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
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    @Override
    public final int lastIndexOf(final Object o) {
        return value.lastIndexOf(o);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @return a list iterator over the elements in this list (in proper
     * sequence)
     */
    @Override
    public final ListIterator<Atom> listIterator() {
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
    public final ListIterator<Atom> listIterator(final int index) {
        return value.listIterator(index);
    }

    /**
     *
     */
    public final void randomise() {
        Collections.shuffle(value);
    }

    /**
     *
     * @param howMany
     * @return
     */
    public final AtomList getRandomSlice(final int howMany) {
        final List<Atom> randomSlice;
        synchronized (mutex) {
            final int size = value.size();
            randomSlice = IntStream.range(0, Utils.clampInt(howMany, 0, size))
                    .mapToObj(i -> value.get(randInt(0, size)))
                    .collect(Collectors.toList());
        }
        return (AtomList) randomSlice;
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
    public final Atom remove(final int index) {
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
    public final boolean remove(final Object o) {
        return value.remove(o);
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
    public final boolean removeAll(final Collection<?> c) {
        return value.removeAll(Collections.synchronizedCollection(c));
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
    public final boolean retainAll(final Collection<?> c) {
        return value.retainAll(Collections.synchronizedCollection(c));
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
    public final Atom set(final int index, final Atom atom) {
        return value.set(index, Objects.requireNonNull(atom));
    }

    /**
     * Returns the number of elements in this list. If this list contains more
     * than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this list
     */
    @Override
    public final int size() {
        return value.size();
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
    public final List<Atom> subList(final int fromIndex, final int toIndex) {
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
    public final Object[] toArray() {
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
    public final <T> T[] toArray(final T[] a) {
        // TODO: look at this?
        return value.toArray(a);
    }

    @Override
    public final int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.value);
        return hash;
    }

    @Override
    public final boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof AtomList) {
            return value.equals(((AtomList) obj).value);
        }
        return false;
    }

    @Override
    public final String toString() {
        return value.toString();
    }

}
