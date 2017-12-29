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

import java.util.List;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.ast.Literal.LiteralIntegerList;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_atoms;

/**
 *
 * @author andrej
 */
public class TheoryElement {

    private Pointer<clingo_theory_atoms> atoms;

    private int id;

    public TheoryElement(Pointer<clingo_theory_atoms> atoms, int id) {
        this.atoms = atoms;
        this.id = id;
    }

    public Pointer<clingo_theory_atoms> getAtoms() {
        return atoms;
    }

    public int getId() {
        return id;
    }

    public List<TheoryTerm> tuple() throws ClingoException {
        Pointer<Pointer<Integer>> ret = Pointer.allocatePointer(Integer.class);
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleError(LIB.clingo_theory_atoms_element_tuple(atoms, id, ret, size), "Error reading theory element tuple!");
        return new TheoryTerm.TheoryTermAtomList(atoms, ret.get(), size.getInt());
    }

    public List<Integer> condition() throws ClingoException {
        Pointer<Pointer<Integer>> ret = Pointer.allocatePointer(Integer.class);
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleError(LIB.clingo_theory_atoms_element_condition(atoms, id, ret, size), "Error reading the theory elements condition!");
        return new LiteralIntegerList(ret.get(), size.getInt());
    }

    public int conditionId() throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_theory_atoms_element_condition_id(atoms, id, ret), "Error reading the theory element condition id!");
        return ret.get();
    }

    @Override
    public String toString() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_theory_atoms_element_to_string_size(atoms, id, size), "Error reading to string size!");
        Pointer<Byte> string = Pointer.allocateByte();
        handleRuntimeError(LIB.clingo_theory_atoms_element_to_string(atoms, id, string, size.getLong()), "Error reading the theory element string");
        return string.getCString();
    }

    public static class TheoryElementList extends SpanList<TheoryElement, Integer> {

        private Pointer<clingo_theory_atoms> atoms;

        public TheoryElementList(Pointer<clingo_theory_atoms> atoms, Pointer<Integer> pointer, long size) {
            super(pointer, size);
            this.atoms = atoms;
        }

        @Override
        protected TheoryElement getItem(Pointer<Integer> p) {
            return new TheoryElement(atoms, p.getInt());
        }

    }    
}
