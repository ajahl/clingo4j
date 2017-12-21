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
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_statistic;

/**
 *
 * @author andrej
 */
public class Statistics {

    private final Pointer<clingo_statistic> pointer;

    private final long key;

    public Statistics(Pointer<clingo_statistic> pointer, long key) {
        this.pointer = pointer;
        this.key = key;
    }

    public Statistics getArrayAt(int index) {
        Pointer<Long> subkey = Pointer.allocateLong();
        if (!LIB.clingo_statistics_array_at(pointer, key, index, subkey)) {
            Clingo.throwError("Error reading the statistic item in array at " + index + " position!");
        }
        return new Statistics(pointer, subkey.getLong());
    }

    public int getArraySize() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        if (!LIB.clingo_statistics_array_size(pointer, key, size)) {
            Clingo.throwError("Error reading the statistic array size!");
        }
        return size.getInt();
    }

    public boolean isArrayEmpty() {
        return getArraySize() == 0;
    }
    
    public Iterator<Statistics> getArrayIterator() {

        final int size = getArraySize();

        return new Iterator<Statistics>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Statistics next() {
                if (index < size) {
                    Statistics result = getArrayAt(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };
    }

    public int getMapSize() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        if (!LIB.clingo_statistics_map_size(pointer, key, size)) {
            Clingo.throwError("Error reading the statistic map size!");
        }
        return size.getInt();
    }

    public boolean isMapEmpty() {
        return getMapSize() == 0;
    }
    
    public String getMapKey(int index) {
        Pointer<Byte> name = Pointer.allocateByte();
        if (!LIB.clingo_statistics_map_subkey_name(pointer, key, index, name.getReference())) {
            Clingo.throwError("Error reading the statistic key name!");
        }
        return name.getCString();
    }

    public Statistics getMapByKey(String name) {
        Pointer<Byte> tmp = Pointer.pointerToCString(name);
        Pointer<Long> subkey = Pointer.allocateLong();
        if (!LIB.clingo_statistics_map_at(pointer, key, tmp, subkey)) {
            Clingo.throwError("Error reading the statistic item in map by" + tmp + " key!");
        }
        return new Statistics(pointer, subkey.getLong());
    }
    
    public Statistics getMapAt(int index) {
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
    
    public Iterator<Statistics> getMapIterator() {

        final int size = getMapSize();

        return new Iterator<Statistics>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Statistics next() {
                if (index < size) {
                    Statistics result = getMapAt(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };
    }

    public double getValue() {
        Pointer<Double> value = Pointer.allocateDouble();
        if (!LIB.clingo_statistics_value_get(pointer, key, value)) {
            Clingo.throwError("Error reading the statistic value!");
        }
        return value.getDouble();
    }

    public StatisticsType getType() {
        Pointer<Integer> value = Pointer.allocateInt();
        if (!LIB.clingo_statistics_type(pointer, key, value)) {
            Clingo.throwError("Error reading the statistric type!");
        }
        return StatisticsType.createStatisticsType(value.getInt());
    }

    public Pointer<clingo_statistic> getPointer() {
        return pointer;
    }

}
