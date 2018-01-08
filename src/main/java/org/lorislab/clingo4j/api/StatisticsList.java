/*
 * Copyright 2017 Andrej Petras.
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
package org.lorislab.clingo4j.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.c.ClingoLibrary;

/**
 *
 * @author Andrej Petras
 */
public class StatisticsList extends Statistics implements List<Statistics> {

    public StatisticsList(Pointer<ClingoLibrary.clingo_statistic> pointer, long key) {
        super(pointer, key);
    }

    @Override
    public int size() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_statistics_array_size(pointer, key, size), "Error reading the statistic array size!");
        return size.getInt();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Statistics> iterator() {

        final int size = size();

        return new Iterator<Statistics>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Statistics next() {
                if (index < size) {
                    Statistics result = get(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(Statistics e) {
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
    public boolean addAll(Collection<? extends Statistics> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Statistics> c) {
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
    public Statistics get(int index) {
        if (0 <= index && index < size()) {
            Pointer<Long> subkey = Pointer.allocateLong();
            handleRuntimeError(LIB.clingo_statistics_array_at(pointer, key, index, subkey), "Error reading the statistic item in array at " + index + " position!");
            return new Statistics(pointer, subkey.getLong());
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public Statistics set(int index, Statistics element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, Statistics element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Statistics remove(int index) {
        throw new UnsupportedOperationException();
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
    public ListIterator<Statistics> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Statistics> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Statistics> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int size = size();
        if (size == 0) {
            sb.append("[]");
        } else {
            sb.append("[\n");
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(toString(get(i)));
            }
            sb.append("]");
        }
        return sb.toString();
    }
}
