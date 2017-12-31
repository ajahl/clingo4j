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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;

/**
 *
 * @author andrej
 */
public class ConfigurationList extends Configuration implements List<Configuration> {
    
    public ConfigurationList(Pointer<clingo_configuration> pointer, int key) {
        super(pointer, key);
    }

    @Override
    public int size() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_configuration_array_size(pointer, key, size), "Error reading the configuration array size!");
        return size.getInt();
    }    
    
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    @Override
    public Configuration get(int index) {
        Pointer<Integer> subkey = Pointer.allocateInt();
        handleRuntimeError(LIB.clingo_configuration_array_at(pointer, key, index, subkey), "Error reading the configuration item in array at " + index + " position!");
        return new Configuration(pointer, subkey.getInt());
    }  
    
    public Iterator<Configuration> iterator() {

        final int size = size();

        return new Iterator<Configuration>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Configuration next() {
                if (index < size) {
                    Configuration result = get(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };
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
    public boolean add(Configuration e) {
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
    public boolean addAll(Collection<? extends Configuration> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(int index, Collection<? extends Configuration> c) {
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
    public Configuration set(int index, Configuration element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void add(int index, Configuration element) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Configuration remove(int index) {
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
    public ListIterator<Configuration> listIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ListIterator<Configuration> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Configuration> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
    
}
