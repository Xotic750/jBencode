package se.suka.baldr.bencode.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import se.suka.baldr.bencode.utilities.Utils;

/**
 * A list of values is encoded as l&lt;contents&gt;e . The contents consist of
 * the Bencoded elements of the list, in order, concatenated. A list consisting
 * of the string "spam" and the number 42 would be encoded as: l4:spami42ee.
 * Note the absence of separators between elements.
 *
 * @author Graham Fairweather
 * @see <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a>
 */
public final class AtomList extends Atom<ArrayList<Atom<?>>> implements List<Atom<?>> {

    public AtomList() {
        this(new ArrayList<>());
    }

    public AtomList(AtomList atomList) {
        this();
        Objects.requireNonNull(atomList);
        atomList.forEach((atom) -> {
            Class<?> atomClass = atom.getClass();
            if (atomClass.equals(AtomInteger.class)) {
                add(new AtomInteger((AtomInteger) atom));
            } else if (atomClass.equals(AtomString.class)) {
                add(new AtomString((AtomString) atom));
            } else if (atomClass.equals(AtomList.class)) {
                add(new AtomList((AtomList) atom));
            } else if (atomClass.equals(AtomDictionary.class)) {
                add(new AtomDictionary((AtomDictionary) atom));
            } else {
                System.err.println("AtomList: unknown Atom type");
            }
        });
    }

    private AtomList(ArrayList<Atom<?>> value) {
        super(value);
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
    public boolean add(Atom<?> atom) {
        Objects.requireNonNull(atom);
        return getValue().add(atom);
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
    public void add(int index, Atom<?> atom) {
        Objects.requireNonNull(atom);
        rangeCheck(index, size());
        getValue().add(index, atom);
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
    public boolean addAll(Collection<? extends Atom<?>> c) {
        Objects.requireNonNull(c);
        final ArrayList<Atom<?>> LIST = getValue();
        c.stream().map((final Atom<?> ATOM) -> {
            Objects.requireNonNull(ATOM);
            return ATOM;
        }).forEachOrdered((final Atom<?> ATOM) -> {
            LIST.add(ATOM);
        });
        return !c.isEmpty();
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
    public boolean addAll(int index, Collection<? extends Atom<?>> c) {
        Objects.requireNonNull(c);
        final int SIZE = size();
        rangeCheck(index, SIZE);
        final ArrayList<Atom<?>> LIST = getValue();
        for (final Atom<?> ATOM : c) {
            LIST.add(rangeCheck(index++, SIZE), Objects.requireNonNull(ATOM));
        }
        return !c.isEmpty();
    }

    /**
     *
     * @return The length of the encoded string when Bencoded.
     */
    @Override
    public int bLength() {
        return getValue().stream().map((final Atom<?> ATOM) -> ATOM.bLength()).reduce(2, Integer::sum);
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
        getValue().clear();
    }

    /**
     * Returns <tt>true</tt> if this list contains the specified element. More
     * formally, returns <tt>true</tt> if and only if this list contains at
     * least one element <tt>e</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
     *
     * @param atom element whose presence in this list is to be tested
     * @return <tt>true</tt> if this list contains the specified element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    public boolean contains(Atom<?> atom) {
        Objects.requireNonNull(atom);
        return getValue().contains(atom);
    }

    @Override
    public boolean contains(Object o) {
        Objects.requireNonNull(o);
        atomCheck(o);
        return contains((Atom<?>) o);
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
    public boolean containsAll(Collection<?> c) {
        return getValue().containsAll(c);
    }

    @Override
    public String encode() {
        StringBuilder str = new StringBuilder("l");
        getValue().stream().forEach((final Atom<?> ATOM) -> {
            str.append(ATOM.encode());
        });
        return str.append("e").toString();
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
    public Atom<?> get(int index) {
        rangeCheck(index, size());
        return getValue().get(index);
    }

    /**
     * Returns the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element. More
     * formally, returns the lowest index <tt>i</tt> such that
     * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
     * or -1 if there is no such index.
     *
     * @param atom element to search for
     * @return the index of the first occurrence of the specified element in
     * this list, or -1 if this list does not contain the element
     * @throws ClassCastException if the type of the specified element is
     * incompatible with this list
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     * @throws NullPointerException if the specified element is null and this
     * list does not permit null elements
     * (<a href="Collection.html#optional-restrictions">optional</a>)
     */
    public int indexOf(Atom<?> atom) {
        Objects.requireNonNull(atom);
        return getValue().indexOf(atom);
    }

    @Override
    public int indexOf(Object o) {
        Objects.requireNonNull(o);
        atomCheck(o);
        return indexOf((Atom<?>) o);
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    @Override
    public boolean isEmpty() {
        return getValue().isEmpty();
    }

    /**
     * Returns an iterator over the elements in this list in proper sequence.
     *
     * @return an iterator over the elements in this list in proper sequence
     */
    @Override
    public Iterator<Atom<?>> iterator() {
        return getValue().iterator();
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
    public int lastIndexOf(Atom<?> atom) {
        Objects.requireNonNull(atom);
        return getValue().lastIndexOf(atom);
    }

    @Override
    public int lastIndexOf(Object o) {
        Objects.requireNonNull(o);
        atomCheck(o);
        return lastIndexOf((Atom<?>) o);
    }

    /**
     * Returns a list iterator over the elements in this list (in proper
     * sequence).
     *
     * @return a list iterator over the elements in this list (in proper
     * sequence)
     */
    @Override
    public ListIterator<Atom<?>> listIterator() {
        return getValue().listIterator();
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
    public ListIterator<Atom<?>> listIterator(int index) {
        rangeCheck(index, size());
        return getValue().listIterator(index);
    }

    public void randomise() {
        Collections.shuffle(getValue());
    }

    public AtomList getRandomSlice(final int HOW_MANY) {
        int index = Utils.clampInt(HOW_MANY, 0, getValue().size());
        final ArrayList<Atom<?>> LIST = getValue();
        final AtomList NEW_LIST = new AtomList();
        while (index-- > 0) {
            NEW_LIST.add(LIST.get(index));
        }
        return NEW_LIST;
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
    public Atom<?> remove(int index) {
        rangeCheck(index, size());
        return getValue().remove(index);
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
     * @param atom element to be removed from this list, if present
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
    public boolean remove(Atom<?> atom) {
        Objects.requireNonNull(atom);
        return getValue().remove(atom);
    }

    @Override
    public boolean remove(Object o) {
        Objects.requireNonNull(o);
        atomCheck(o);
        return remove((Atom<?>) o);
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
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean removed = false;
        for (final Object OBJ : c) {
            Objects.requireNonNull(OBJ);
            atomCheck(OBJ);
            removed = removed == false && remove((Atom<?>) OBJ);
        }
        return removed;
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
    public boolean retainAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean removed = false;
        for (final Object OBJ : c) {
            Objects.requireNonNull(OBJ);
            atomCheck(OBJ);
            final Atom<?> ATOM = (Atom<?>) OBJ;
            if (!c.contains(ATOM)) {
                removed = removed == false && remove(ATOM);
            }
        }
        return removed;
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
    public Atom<?> set(int index, Atom<?> atom) {
        Objects.requireNonNull(atom);
        rangeCheck(index, size());
        return getValue().set(index, atom);
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
        return getValue().size();
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
    public List<Atom<?>> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size());
        return getValue().subList(fromIndex, toIndex);
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
        return getValue().toArray();
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
     * @param a the array into which the elements of this list are to be stored,
     * if it is big enough; otherwise, a new array of the same runtime type is
     * allocated for this purpose.
     * @return an array containing the elements of this list
     * @throws ArrayStoreException if the runtime type of the specified array is
     * not a supertype of the runtime type of every element in this list
     * @throws NullPointerException if the specified array is null
     */
    @Override
    public <T> T[] toArray(T[] a) {
        Objects.requireNonNull(a);
        return getValue().toArray(a);
    }

}
