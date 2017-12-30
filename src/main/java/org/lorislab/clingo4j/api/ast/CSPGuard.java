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

import org.bridj.Pointer;
import org.lorislab.clingo4j.api.SpanList;
import org.lorislab.clingo4j.api.c.clingo_ast_csp_guard;

/**
 *
 * @author andrej
 */
public class CSPGuard {

    private final ComparisonOperator operator;

    private final CSPSum term;

    public CSPGuard(clingo_ast_csp_guard csp) {
        this(ComparisonOperator.valueOfInt(csp.comparison()), new CSPSum(csp.term()));
    }
    
    public CSPGuard(ComparisonOperator operator, CSPSum term) {
        this.operator = operator;
        this.term = term;
    }
    
    public ComparisonOperator getOperator() {
        return operator;
    }

    public CSPSum getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return "$" + operator + term;
    }

    public static class CSPGuardList extends SpanList<CSPGuard, clingo_ast_csp_guard> {

        public CSPGuardList(Pointer<clingo_ast_csp_guard> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected CSPGuard getItem(Pointer<clingo_ast_csp_guard> p) {
            return new CSPGuard(p.get());
        }
    }
    
}
