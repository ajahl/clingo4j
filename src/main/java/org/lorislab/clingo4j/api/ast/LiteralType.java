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
package org.lorislab.clingo4j.api.ast;

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_literal_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_literal_type.clingo_ast_literal_type_boolean;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_literal_type.clingo_ast_literal_type_comparison;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_literal_type.clingo_ast_literal_type_csp;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_literal_type.clingo_ast_literal_type_symbolic;

/**
 *
 * @author andrej
 */
public enum LiteralType {

    BOOLEAN(clingo_ast_literal_type_boolean),
    SYMBOLIC(clingo_ast_literal_type_symbolic),
    COMPARISON(clingo_ast_literal_type_comparison),
    CSP(clingo_ast_literal_type_csp);

    private final clingo_ast_literal_type type;

    private LiteralType(clingo_ast_literal_type type) {
        this.type = type;
    }

    public clingo_ast_literal_type getType() {
        return type;
    }

    public static LiteralType valueOfInt(int value) {
        LiteralType r = null;
        LiteralType[] values = LiteralType.values();
        for (int i=0; i<values.length && r == null; i++) {
            if (values[i].type.value == value) {
                r = values[i];
            }
        }
        return r;
    }
}
