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

import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbolic_atoms;

/**
 *
 * @author andrej
 */
public class SymbolicAtoms {

    private final Pointer<clingo_symbolic_atoms> pointer;

    public SymbolicAtoms(Pointer<clingo_symbolic_atoms> pointer) {
        this.pointer = pointer;
    }

    public Pointer<clingo_symbolic_atoms> getPointer() {
        return pointer;
    }

    public int size() throws ClingoException {
        Pointer<SizeT> ret = Pointer.allocateSizeT();
        handleError(LIB.clingo_symbolic_atoms_size(pointer, ret), "Error reading the symbolic atoms size!");
        return ret.getInt();
    }
}
