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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_atom_definition_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_atom_definition_type.clingo_ast_theory_atom_definition_type_any;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_atom_definition_type.clingo_ast_theory_atom_definition_type_body;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_atom_definition_type.clingo_ast_theory_atom_definition_type_directive;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_theory_atom_definition_type.clingo_ast_theory_atom_definition_type_head;

/**
 *
 * @author andrej
 */
public enum TheoryAtomDefinitionType {

    HEAD(clingo_ast_theory_atom_definition_type_head, "head"),
    BODY(clingo_ast_theory_atom_definition_type_body, "body"),
    ANY(clingo_ast_theory_atom_definition_type_any, "any"),
    DIRECTIVE(clingo_ast_theory_atom_definition_type_directive, "directive");

    private clingo_ast_theory_atom_definition_type type;

    private String string;

    private TheoryAtomDefinitionType(clingo_ast_theory_atom_definition_type type, String string) {
        this.type = type;
        this.string = string;
    }

    public clingo_ast_theory_atom_definition_type getType() {
        return type;
    }

    public int getValue() {
        return (int) type.value;
    }

    @Override
    public String toString() {
        return string;
    }

}
