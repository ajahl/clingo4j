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

import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_sign;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_sign.clingo_ast_sign_double_negation;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_sign.clingo_ast_sign_negation;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_sign.clingo_ast_sign_none;

/**
 *
 * @author andrej
 */
public enum Sign {
 
    NONE(clingo_ast_sign_none,""),
    
    NEGATION(clingo_ast_sign_negation,"not"),
    
    DOUBLENEGATION(clingo_ast_sign_double_negation,"not not");
    
    private clingo_ast_sign sign;

    private String string;
    
    private Sign(clingo_ast_sign sign, String string) {
        this.sign = sign;
        this.string = string;
    }

    public clingo_ast_sign getSign() {
        return sign;
    }

    public int getValue() {
        return (int) sign.value;
    }
    

}
