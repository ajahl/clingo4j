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
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbolic_atoms;

/**
 *
 * @author andrej
 */
public class SymbolicAtomIterator {

    private Pointer<clingo_symbolic_atoms> atoms;

    private long range;

    public SymbolicAtomIterator next() throws ClingoException {
        Pointer<Long> next = Pointer.allocateLong();
        handleError(LIB.clingo_symbolic_atoms_next(atoms, range, next), "Error readint the next atoms!");
        range = next.get();
        return this;
    }

    public boolean isValid() throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbolic_atoms_is_valid(atoms, range, ret), "Error check the atoms is valid function!");
        return ret.getBoolean();
    }

    public boolean isNotEqual(SymbolicAtomIterator it) throws ClingoException {
        return !isEqual(it);
    }

    public boolean isEqual(SymbolicAtomIterator it) throws ClingoException {
        boolean ret = atoms.equals(it.atoms);
        if (ret) {
            Pointer<Boolean> value = Pointer.allocateBoolean();
            handleError(LIB.clingo_symbolic_atoms_iterator_is_equal_to(atoms, range, it.range, value), "Error check atoms iterator is equal!");
            ret = value.get();
        }
        return ret;
    }

}
