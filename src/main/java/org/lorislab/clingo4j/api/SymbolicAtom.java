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
public class SymbolicAtom {
    
    private final Pointer<clingo_symbolic_atoms> atoms;
    
    private final long iterator;

    public SymbolicAtom(Pointer<clingo_symbolic_atoms> atoms, long iterator) {
        this.atoms = atoms;
        this.iterator = iterator;
    }

    public Pointer<clingo_symbolic_atoms> getAtoms() {
        return atoms;
    }

    public long getIterator() {
        return iterator;
    }
    
    public Symbol getSymbol() throws  ClingoException {
        Pointer<Long> symbol = Pointer.allocateLong();
        handleError(LIB.clingo_symbolic_atoms_symbol(atoms, iterator, symbol), "Error readint the atoms symbol!");
        return new Symbol(symbol);
    }
    
    public int getLiteral() throws ClingoException {
        Pointer<Integer> literal = Pointer.allocateInt();
        handleError(LIB.clingo_symbolic_atoms_literal(atoms, iterator, literal), "Error reading the atoms litral!");
        return literal.get();
    }
    
    public boolean isFact() throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbolic_atoms_is_fact(atoms, iterator, ret), "Error reading the symbol atom is fact!");
        return ret.get();        
    }
    
    public boolean isExternal() throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbolic_atoms_is_external(atoms, iterator, ret), "Error reading he symbol atom is external!");
        return ret.get();
    }
   
}