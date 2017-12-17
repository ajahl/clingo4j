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

import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_comparison_operator;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_equal;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_greater_equal;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_greater_than;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_less_equal;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_less_than;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_comparison_operator.clingo_ast_comparison_operator_not_equal;

/**
 *
 * @author andrej
 */
public enum ComparisonOperator {
 
    GREATER_THAN(clingo_ast_comparison_operator_greater_than,">"),
    
    LESS_THAN(clingo_ast_comparison_operator_less_than,"<"),
    
    LESS_EQUAL(clingo_ast_comparison_operator_less_equal,"<="),
    
    GREATEREQUAL(clingo_ast_comparison_operator_greater_equal,">="),
    
    NOTEQUAL(clingo_ast_comparison_operator_not_equal, "!="),

    EQUAL(clingo_ast_comparison_operator_equal, "=");
    
    private clingo_ast_comparison_operator operator;

    private String string;
    
    private ComparisonOperator(clingo_ast_comparison_operator operator, String string) {
        this.operator = operator;
        this.string = string;
    }

    public clingo_ast_comparison_operator getOperator() {
        return operator;
    }

    public int getValue() {
        return (int) operator.value;
    }
    
}
