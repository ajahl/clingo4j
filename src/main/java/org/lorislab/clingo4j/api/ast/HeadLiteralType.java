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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_head_literal_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_head_literal_type.clingo_ast_head_literal_type_aggregate;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_head_literal_type.clingo_ast_head_literal_type_disjunction;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_head_literal_type.clingo_ast_head_literal_type_head_aggregate;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_head_literal_type.clingo_ast_head_literal_type_literal;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_head_literal_type.clingo_ast_head_literal_type_theory_atom;

/**
 *
 * @author andrej
 */
public enum HeadLiteralType {
    
    LITERAL(clingo_ast_head_literal_type_literal),
    DISJUNCTION(clingo_ast_head_literal_type_disjunction),
    AGGREGATE(clingo_ast_head_literal_type_aggregate),
    HEAD_AGGREGATE(clingo_ast_head_literal_type_head_aggregate),
    THEORY_ATOM(clingo_ast_head_literal_type_theory_atom);
    
    private clingo_ast_head_literal_type type;

    private HeadLiteralType(clingo_ast_head_literal_type type) {
        this.type = type;
    }

    public clingo_ast_head_literal_type getType() {
        return type;
    }

    public int getValue() {
        return (int) type.value;
    }
    
    public static HeadLiteralType valueOfInt(int value) {
        HeadLiteralType r = null;
        HeadLiteralType[] values = HeadLiteralType.values();
        for (int i=0; i<values.length && r == null; i++) {
            if (values[i].getValue() == value) {
                r = values[i];
            }
        }
        return r;
    }
    
}
