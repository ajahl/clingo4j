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

import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_theory_term_type;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_list;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_set;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_theory_term_type.clingo_ast_theory_term_type_tuple;

/**
 *
 * @author andrej
 */
public enum TheoryTermSequenceType {

    TUPLE(0,"(",")", clingo_ast_theory_term_type_tuple),
    LIST(1, "[","]", clingo_ast_theory_term_type_list),
    SET(2, "{","}", clingo_ast_theory_term_type_set);

    private int value;

    private clingo_ast_theory_term_type type;
    
    private String left;
    
    private String right;
    
    private TheoryTermSequenceType(int value, String left, String right, clingo_ast_theory_term_type type) {
        this.value = value;
        this.left = left;
        this.right = right;
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public clingo_ast_theory_term_type getType() {
        return type;
    }

    public String getRight() {
        return right;
    }

    public String getLeft() {
        return left;
    }

    
    
}
