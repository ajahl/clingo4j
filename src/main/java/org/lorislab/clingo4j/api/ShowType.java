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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type.clingo_show_type_all;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type.clingo_show_type_atoms;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type.clingo_show_type_complement;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type.clingo_show_type_csp;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type.clingo_show_type_extra;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type.clingo_show_type_shown;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_show_type.clingo_show_type_terms;


/**
 *
 * @author andrej
 */
public enum ShowType {
    
    CSP(clingo_show_type_csp),
            
    SHOWN(clingo_show_type_shown),
          
    ATOMS(clingo_show_type_atoms),
    
    TERMS(clingo_show_type_terms),
    
    THEORY(clingo_show_type_extra),
    
    ALL(clingo_show_type_all),
    
    COMPLEMENT(clingo_show_type_complement);
        
    private clingo_show_type type;

    private ShowType(clingo_show_type type) {
        this.type = type;
    }

    public clingo_show_type getType() {
        return type;
    }
    
    public long getValue() {
        return type.value;
    }
    
    public static ShowType createSymbolType(long value) {
        ShowType result = null;
        ShowType[] types = ShowType.values();
        for (int i=0; i<types.length && result == null; i++) {
            if (types[i].getValue() == value) {
                result = types[i];
            }
        }
        return result;
    }        
}
