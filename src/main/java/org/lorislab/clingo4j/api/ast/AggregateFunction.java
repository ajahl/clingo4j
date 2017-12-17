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

import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_aggregate_function;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_aggregate_function.clingo_ast_aggregate_function_count;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_aggregate_function.clingo_ast_aggregate_function_max;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_aggregate_function.clingo_ast_aggregate_function_min;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_aggregate_function.clingo_ast_aggregate_function_sum;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ast_aggregate_function.clingo_ast_aggregate_function_sump;

/**
 *
 * @author andrej
 */
public enum AggregateFunction {
    
    COUNT(clingo_ast_aggregate_function_count,"#count"),
    SUM(clingo_ast_aggregate_function_sum,"#sum"),
    SUMPLUS(clingo_ast_aggregate_function_sump,"#sum+"),
    MIN(clingo_ast_aggregate_function_min,"#min"),
    MAX(clingo_ast_aggregate_function_max,"#max");
            
    private clingo_ast_aggregate_function function;

    private String string;

    private AggregateFunction(clingo_ast_aggregate_function function, String string) {
        this.function = function;
        this.string = string;
    }

    public clingo_ast_aggregate_function getFunction() {
        return function;
    }

    public int getValue() {
        return (int) function.value;
    }

    @Override
    public String toString() {
        return string;
    }
    
}
