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

import java.util.Iterator;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.throwError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration;

/**
 *
 * @author andrej
 */
public class Configuration {
    
    private final Pointer<clingo_configuration> pointer;
    
    private final int key;

    public Configuration(Pointer<clingo_configuration> pointer, int key) {
        this.pointer = pointer;
        this.key = key;
    }
    
    public ConfigurationType getType() {
        Pointer<Integer > type = Pointer.allocateInt();
        throwError(LIB.clingo_configuration_type(pointer, key, type), "Error reading the configuration type!");
        return ConfigurationType.createConfigurationType(type.getInt());
    }
    
    public boolean isAssigned() {
        Pointer<Boolean> result = Pointer.allocateBoolean();
        throwError(LIB.clingo_configuration_value_is_assigned(pointer, key, result), "Error reading the configureation assigned.");
        return result.getBoolean();
    }
   
    public int getArraySize() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        throwError(LIB.clingo_configuration_array_size(pointer, key, size), "Error reading the configuration array size!");
        return size.getInt();
    }    
    
    public boolean isArrayEmpty() {
        return getArraySize() == 0;
    }
    
    public Configuration getArrayAt(int index) {
        Pointer<Integer> subkey = Pointer.allocateInt();
        throwError(LIB.clingo_configuration_array_at(pointer, key, index, subkey), "Error reading the configuration item in array at " + index + " position!");
        return new Configuration(pointer, subkey.getInt());
    }  
    
    public Iterator<Configuration> getArrayIterator() {

        final int size = getArraySize();

        return new Iterator<Configuration>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Configuration next() {
                if (index < size) {
                    Configuration result = getArrayAt(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };
    }
    
    

    public int getMapSize() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        throwError(LIB.clingo_configuration_map_size(pointer, key, size), "Error reading the configuration map size!");
        return size.getInt();
    }

    public boolean isMapEmpty() {
        return getMapSize() == 0;
    }
    
    public String getMapKey(int index) {
        Pointer<Byte> name = Pointer.allocateByte();
        throwError(LIB.clingo_configuration_map_subkey_name(pointer, key, index, name.getReference()),"Error reading the configuration key name!");
        return name.getCString();
    }

    public Configuration getMapByKey(String name) {
        Pointer<Byte> tmp = Pointer.pointerToCString(name);
        Pointer<Integer> subkey = Pointer.allocateInt();
        throwError(LIB.clingo_configuration_map_at(pointer, key, tmp, subkey), "Error reading the configuration item in map by" + tmp + " key!");
        return new Configuration(pointer, subkey.getInt());
    }
    
    public Configuration getMapAt(int index) {
        String name = getMapKey(index);
        return getMapByKey(name);
    }

    public Iterator<String> getMapKeyIterator() {
        
        final int size = getMapSize();
        
        return new Iterator<String>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public String next() {
                if (index < size) {
                    String result = getMapKey(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };        
    }
    
    public Iterator<Configuration> getMapIterator() {

        final int size = getMapSize();

        return new Iterator<Configuration>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Configuration next() {
                if (index < size) {
                    Configuration result = getMapAt(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };
    }  
    
    public String getValue() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        throwError(LIB.clingo_configuration_value_get_size(pointer, key, size), "Error reading the configuration value size!");
        Pointer<Byte> value = Pointer.allocateByte();
        throwError(LIB.clingo_configuration_value_get(pointer, key, value, size.getInt()), "Error reading the configuration value!");
        return value.getCString();
    }

    public void setValue(String value) {
        Pointer<Byte> p = Pointer.pointerToCString(value);
        throwError(LIB.clingo_configuration_value_set(pointer, key, p), "Error settings the configuration value " + value + "!");
    }
    
    public String getDescription() {
       Pointer<Pointer<Byte>> description = Pointer.allocatePointer(Byte.class);
        throwError(LIB.clingo_configuration_description(pointer, key, description), "Error reading the configuration description!");
        return description.getCString();
    }
    
}
