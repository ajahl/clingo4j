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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;

/**
 *
 * @author andrej
 */
public class ConfigurationMap extends Configuration implements Iterable<Configuration>, Map<String, Configuration> {
    
    public ConfigurationMap(Pointer<clingo_configuration> pointer, int key) {
        super(pointer, key);
    }
    
    @Override
    public int size() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_configuration_map_size(pointer, key, size), "Error reading the configuration map size!");
        return size.getInt();
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }
    
    public String getKey(int index) {
        Pointer<Byte> name = Pointer.allocateByte();
        handleRuntimeError(LIB.clingo_configuration_map_subkey_name(pointer, key, index, name.getReference()),"Error reading the configuration key name!");
        return name.getCString();
    }

    @Override
    public Configuration get(Object name) {
        Pointer<Byte> tmp = Pointer.pointerToCString((String) name);
        Pointer<Integer> subkey = Pointer.allocateInt();
        handleRuntimeError(LIB.clingo_configuration_map_at(pointer, key, tmp, subkey), "Error reading the configuration item in map by" + tmp + " key!");
        return new Configuration(pointer, subkey.getInt());
    }
    
    public Configuration get(int index) {
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
    
    @Override
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
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Configuration put(String key, Configuration value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Configuration remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends Configuration> m) {
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
    public Collection<Configuration> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<String, Configuration>> entrySet() {
        throw new UnsupportedOperationException();
    }
 
}
