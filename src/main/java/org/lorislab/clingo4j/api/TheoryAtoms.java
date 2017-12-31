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
package org.lorislab.clingo4j.api;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_atoms;
import org.lorislab.clingo4j.util.AbstractPointerObject;

/**
 *
 * @author andrej
 */
public class TheoryAtoms extends AbstractPointerObject<clingo_theory_atoms> implements List<TheoryAtom> {

    public TheoryAtoms(Pointer<clingo_theory_atoms> pointer) {
        super(pointer);
    }

    @Override
    public Iterator<TheoryAtom> iterator() {
        return new Iterator<TheoryAtom>() {
            private int index = 0;
            private final int size = size();

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public TheoryAtom next() {
                TheoryAtom tmp = new TheoryAtom(pointer, index);
                index = index + 1;
                return tmp;
            }

        };
    }

    @Override
    public int size() {
        Pointer<SizeT> ret = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_theory_atoms_size(pointer, ret), "Error reading the theory atoms size!");
        return ret.getInt();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public TheoryAtom get(int index) {
        if (0 <= index && index < size()) {
            return new TheoryAtom(pointer, index);
        }
        throw new IndexOutOfBoundsException();
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
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
    public boolean add(TheoryAtom e) {
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
    public boolean addAll(Collection<? extends TheoryAtom> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends TheoryAtom> c) {
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
    public TheoryAtom set(int index, TheoryAtom element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, TheoryAtom element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public TheoryAtom remove(int index) {
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
    public ListIterator<TheoryAtom> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<TheoryAtom> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TheoryAtom> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}
