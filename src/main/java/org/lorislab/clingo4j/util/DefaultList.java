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

import java.util.function.Function;
import org.bridj.Pointer;

/**
 *
 * @author andrej
 * @param <T>
 * @param <K>
 */
public class DefaultList<T, K> extends SpanList<T, K> {

    private Function<K, T> fn;
    
    public DefaultList(Function<K, T> fn, Pointer<K> pointer, long size) {
        super(pointer, size);
        this.fn = fn;
    }

    @Override
    protected T getItem(Pointer<K> p) {
        return fn.apply(p.get());
    }
    
}
