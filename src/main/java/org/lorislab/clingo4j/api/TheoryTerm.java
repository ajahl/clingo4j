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

import org.lorislab.clingo4j.api.enums.TheoryTermType;
import org.lorislab.clingo4j.util.SpanList;
import java.util.List;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_atoms;
import org.lorislab.clingo4j.util.AbstractPointerObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author andrej
 */
public class TheoryTerm extends AbstractPointerObject<clingo_theory_atoms> {

    private final int id;

    public TheoryTerm(Pointer<clingo_theory_atoms> atoms, int id) {
        super(atoms);
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public TheoryTermType type() throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_theory_atoms_term_type(pointer, id, ret), "Error reading the theory term type!");
        return EnumValue.valueOfInt(TheoryTermType.class, ret.getInt());
    }

    public int number() throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_theory_atoms_term_number(pointer, id, ret), "Error reading theory term number!");
        return ret.get();
    }

    public String name() throws ClingoException {
        Pointer<Pointer<Byte>> ret = Pointer.allocatePointer(Byte.class);
        handleError(LIB.clingo_theory_atoms_term_name(pointer, id, ret), "Error reading the theory term name!");
        return ret.get().getCString();
    }

    public List<TheoryTerm> arguments() throws ClingoException {
        Pointer<Pointer<Integer>> ret = Pointer.allocatePointer(Integer.class);
        Pointer<SizeT> n = Pointer.allocateSizeT();
        handleError(LIB.clingo_theory_atoms_term_arguments(pointer, id, ret, n), "Error reading the theory teram arguments!");
        return list(pointer, ret.get(), id);
    }

    public static List<TheoryTerm> list(final Pointer<clingo_theory_atoms> atoms, Pointer<Integer> pointer, long size) {
        return new SpanList<TheoryTerm, Integer>(pointer, size) {
            @Override
            protected TheoryTerm getItem(Pointer<Integer> p) {
                return new TheoryTerm(atoms, p.getInt());
            }
        };
    }
    
    @Override
    public String toString() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_theory_atoms_term_to_string_size(pointer, id, size), "Error reading to string size!");
        Pointer<Byte> string = Pointer.allocateByte();
        handleRuntimeError(LIB.clingo_theory_atoms_term_to_string(pointer, id, string, size.getLong()), "Error reading the theory term string");
        return string.getCString();
    }

}
