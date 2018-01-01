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

import java.util.List;
import java.util.Optional;
import org.bridj.NativeObject;
import org.bridj.Pointer;

/**
 *
 * @author andrej
 * @param <T>
 */
public interface ASTObject<T extends NativeObject> {
    
    public abstract T create();
    
    default Pointer<T> createPointer() {
        return Pointer.getPointer(create());
    }
    
    public static <K extends NativeObject> K optional(Optional<? extends ASTObject<K>> item) {
        if (item.isPresent()) {
            return item.get().create();
        }
        return null;
    }
    
    public static <K extends NativeObject> Pointer<K> optionalPointer(Optional<? extends ASTObject<K>> item) {
        if (item.isPresent()) {
            return item.get().createPointer();
        }
        return null;
    }
    
    public static <T extends NativeObject, E extends ASTObject<T>> Pointer<T> array(List<E> data, Class<T> clazz) {
        Pointer<T> result = null;
        if (data != null && !data.isEmpty()) {
            result = Pointer.allocateArray(clazz, data.size());
            Pointer<T> iter = result;
            for (E item : data) {
                iter.set(item.create());
                iter = iter.next();
            }
        }
        return result;
    }    
    
}
