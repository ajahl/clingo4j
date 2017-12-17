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
import org.lorislab.clingo4j.c.api.clingo_part;

/**
 *
 * @author andrej
 */
public class Part {
    
    private clingo_part part;

    public Part(clingo_part part) {
        this.part = part;
    }

    public Part(String name) {
        part = new clingo_part();        
        part.name(Pointer.pointerToCString(name));
        part.params(null);
        part.size(0);        
    }

    public Part(String name, List<Symbol> symbols) {
        this(name);
        if (symbols != null && !symbols.isEmpty()) {
            Pointer<Long> tmp = Clingo.createPointerToSymbols(symbols);
            part.params(tmp);
            part.size(symbols.size());
        }
    }

    public clingo_part getPart() {
        return part;
    }
        
    public String getName() {
        return part.name().getCString();
    }
    
    public List<Symbol> getParameters() {
        return Clingo.createListOfSymbols(part.params(), part.size());
    }
    
}