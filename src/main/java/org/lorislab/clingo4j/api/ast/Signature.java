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
package org.lorislab.clingo4j.api.ast;

import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.ClingoException;
import org.lorislab.clingo4j.api.SpanList;

/**
 *
 * @author andrej
 */
public class Signature {

    private final Pointer<Long> pointer;

    public Signature(Pointer<Long> pointer) {
        this.pointer = pointer;
    }
    
    public Signature(String name, int arity, boolean positive) throws ClingoException {
       pointer = Pointer.allocateLong();
       handleError(LIB.clingo_signature_create(Pointer.pointerToCString(name), arity, positive, pointer), "Error creating the signature!");
    }

    public Pointer<Long> getPointer() {
        return pointer;
    }

    public String getName() {
        Pointer<Byte> tmp = LIB.clingo_signature_name(pointer.get());
        return tmp.getCString();
    }

    public int getArity() {
        return LIB.clingo_signature_arity(pointer.get());
    }

    public boolean isPositive() {
        return LIB.clingo_signature_is_positive(pointer.get());
    }

    public boolean isNegative() {
        return LIB.clingo_signature_is_negative(pointer.get());
    }

    public long getHash() {
        return LIB.clingo_signature_hash(pointer.get());
    }

    @Override
    public String toString() {
        return (isNegative() ? "-" : "") + getName() + "/" + getArity();
    }

    public boolean isEqual(Signature s) {
        return LIB.clingo_signature_is_equal_to(pointer.get(), s.pointer.get());
    }
    
    public boolean isNotEqual(Signature s) {
        return !isEqual(s);
    }
    
    public boolean isLessThan(Signature s) {
        return LIB.clingo_signature_is_less_than(pointer.get(), s.pointer.get());
    }
    
    public boolean isLessEqualThan(Signature s) {
        return !LIB.clingo_signature_is_less_than(s.pointer.get(), pointer.get());
    }
    
    public boolean isMoreThan(Signature s) {
        return LIB.clingo_signature_is_less_than(s.pointer.get(), pointer.get());
    }
    
    public boolean isMoreEqualThan(Signature s) {
        return !LIB.clingo_signature_is_less_than(pointer.get(), s.pointer.get());
    }
    
    
    
    public static class SignatureList extends SpanList<Signature, Long> {

        public SignatureList(Pointer<Long> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected Signature getItem(Pointer<Long> p) {
            return new Signature(p);
        }
        
    }
}
