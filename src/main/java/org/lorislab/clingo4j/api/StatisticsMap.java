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
import java.util.Map;
import java.util.Set;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.c.ClingoLibrary;

/**
 *
 * @author andrej
 */
public class StatisticsMap extends Statistics implements Iterable<Statistics>, Map<String, Statistics> {

    public StatisticsMap(Pointer<ClingoLibrary.clingo_statistic> pointer, long key) {
        super(pointer, key);
    }
    
    public String getKey(int index) {
        Pointer<Byte> name = Pointer.allocateByte();
        handleRuntimeError(LIB.clingo_statistics_map_subkey_name(pointer, key, index, name.getReference()), "Error reading the statistic key name!");
        return name.getCString();
    }
    
    public Statistics get(int index) {
        String name = getKey(index);
        return get(name);
    }

    public Iterator<String> keyIterator() {
        
        final int size = size();
        
        return new Iterator<String>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public String next() {
                if (index < size) {
                    String result = getKey(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };        
    }
    
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
    public int size() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_statistics_map_size(pointer, key, size), "Error reading the statistic map size!");
        return size.getInt();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Statistics get(Object name) {
        Pointer<Byte> tmp = Pointer.pointerToCString((String) name);
        Pointer<Long> subkey = Pointer.allocateLong();
        handleRuntimeError(LIB.clingo_statistics_map_at(pointer, key, tmp, subkey), "Error reading the statistic item in map by" + tmp + " key!");
        return new Statistics(pointer, subkey.getLong());
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Statistics put(String key, Statistics value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Statistics remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Statistics> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Statistics> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<String, Statistics>> entrySet() {
        throw new UnsupportedOperationException();
    }

    
}
