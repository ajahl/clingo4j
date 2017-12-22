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

import java.util.ArrayList;
import java.util.List;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_model;
import static org.lorislab.clingo4j.api.Clingo.handleError;

/**
 *
 * @author andrej
 */
public class Model {

    private final Pointer<clingo_model> pointer;

    public Model(Pointer<clingo_model> pointer) {
        this.pointer = pointer;
    }

    public Pointer<clingo_model> getPointer() {
        return pointer;
    }

    public ModelType getType() throws ClingoException {
        Pointer<Integer> type = Pointer.allocateInt();
        handleError(LIB.clingo_model_type(pointer, type), "Error reading the model type");
        return ModelType.createModelType(type.get());
    }

    public List<Symbol> getSymbols() throws ClingoException {
        return getSymbols(ShowType.SHOWN);
    }
    
    public List<Symbol> getSymbols(ShowType type) throws ClingoException {
        List<Symbol> result = null;

        int show = (int) type.getValue();

        // determine the number of (shown) symbols in the model    
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleError(LIB.clingo_model_symbols_size(pointer, show, size), "Error reading size of symbols of the model");

        if (0 < size.getLong()) {
            // allocate required memory to hold all the symbols
            Pointer<Long> atoms = Pointer.allocateLongs(size.getLong());

            // retrieve the symbols in the model
            handleError(LIB.clingo_model_symbols(pointer, show, atoms, size.getLong()), "Error read the model symbols");
            
            result = new Symbol.SymbolList(atoms, size.getLong());
        }
        return result;
    }
}
