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
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_function;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_infimum;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_number;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_string;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_supremum;

/**
 *
 * @author andrej
 */
public enum SymbolType {
    // clingo_symbol_type_infimum,
    INFIMUM(clingo_symbol_type_infimum),
    
    // clingo_symbol_type_number,
    NUMBER(clingo_symbol_type_number),
    
    // clingo_symbol_type_string,
    STRING(clingo_symbol_type_string),
    
    // clingo_symbol_type_function,
    FUNCTION(clingo_symbol_type_function),
    
    // clingo_symbol_type_supremum
    SUPREMUM(clingo_symbol_type_supremum);
    
    private clingo_symbol_type type;
        
    private SymbolType(clingo_symbol_type type) {
        this.type = type;
    }

    public clingo_symbol_type getType() {
        return type;
    }
    
    public long getValue() {
        return type.value;
    }
    
    public static SymbolType createSymbolType(long value) {
        SymbolType result = null;
        SymbolType[] types = SymbolType.values();
        for (int i=0; i<types.length && result == null; i++) {
            if (types[i].getValue() == value) {
                result = types[i];
            }
        }
        return result;
    }
    
}
