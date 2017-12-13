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
package org.lorislab.clingo4j.api2;

import java.util.ArrayList;
import java.util.List;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api2.Clingo.LIB;
import static org.lorislab.clingo4j.api2.Clingo.throwError;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_model;

/**
 *
 * @author andrej
 */
public class Model {

    private Pointer<clingo_model> pointer;

    public Model(Pointer<clingo_model> pointer) {
        this.pointer = pointer;
    }

    public Pointer<clingo_model> getPointer() {
        return pointer;
    }

    public ModelType getType() {
        Pointer<Integer> type = Pointer.allocateInt();
        if (!LIB.clingo_model_type(pointer, type)) {
            throwError("Error reading the model type");
        }
        return ModelType.createModelType(type.get());
    }

    public List<Symbol> getSymbols() {
        return getSymbols(ShowType.SHOWN);
    }
    
    public List<Symbol> getSymbols(ShowType type) {
        List<Symbol> result = null;

        int show = (int) type.getValue();

        // determine the number of (shown) symbols in the model    
        Pointer<SizeT> size = Pointer.allocateSizeT();
        if (!LIB.clingo_model_symbols_size(pointer, show, size)) {
            throwError("Error reading size of symbols of the model");
        }

        if (0 < size.getLong()) {
            // allocate required memory to hold all the symbols
            Pointer<Long> atoms = Pointer.allocateLongs(size.getLong());

            // retrieve the symbols in the model
            if (!LIB.clingo_model_symbols(pointer, show, atoms, size.getLong())) {
                throwError("Error read the model symbols");
            }
            
            result = new ArrayList<>((int) size.getLong());
            for (int i = 0; i < size.getLong(); i++, atoms = atoms.next()) {
                result.add(new Symbol(atoms));
            }
        }
        return result;
    }
}
