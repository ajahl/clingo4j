/*
 * Copyright 2017 andrej.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lorislab.clingo4j.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.bridj.Pointer;

/**
 *
 * @author andrej
 * @param <T>
 * @param <K>
 */
public abstract class SpanList<T, K> implements List<T> {

    private final Pointer<K> pointer;

    private final long size;

    public SpanList(Pointer<K> pointer, long size) {
        this.pointer = pointer;
        this.size = size;
    }

    public Pointer<K> getPointer() {
        return pointer;
    }

    protected abstract T getItem(Pointer<K> p);

    @Override
    public int size() {
        return (int)  size;
    }

    @Override
    public boolean isEmpty() {
        return size > 0;
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                T s = getItem(pointer.next(index));
                index = index + 1;
                return s;
            }
        };
    }

    @Override
    public T get(int index) {
        if (0 <= index && index < size) {
            return getItem(pointer.next(index));
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public Object[] toArray() {
        return pointer.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return pointer.toArray(a);
    }

    @Override
    public int indexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new ListIterator<T>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                T s = getItem(pointer.next(index));
                index = index + 1;
                return s;
            }

            @Override
            public boolean hasPrevious() {
                return 0 < index;
            }

            @Override
            public T previous() {
                T s = getItem(pointer.next(index));
                index = index - 1;
                return s;
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(T e) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void add(T e) {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean comma = false;
        for (T my : this) {
            if (comma) {
                sb.append(", ");
            } else {
                sb.append(" ");
            }
            sb.append(my);
            comma = true;
        }
        sb.append(" }");
        return sb.toString();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(T e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

}
