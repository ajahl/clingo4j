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

import java.util.Optional;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_ast_aggregate_guard;

/**
 *
 * @author andrej
 */
public class AggregateGuard {
    
    private final ComparisonOperator operator;
    
    private final Term term;    

    public AggregateGuard(clingo_ast_aggregate_guard g) {
        this(ComparisonOperator.valueOfInt(g.comparison()), new Term(g.term()));
    }
    
    public AggregateGuard(ComparisonOperator comparison, Term term) {
        this.operator = comparison;
        this.term = term;
    }
    
    public ComparisonOperator getOperator() {
        return operator;
    }

    public Term getTerm() {
        return term;
    }
    
    public static Optional<AggregateGuard> convert(Pointer<clingo_ast_aggregate_guard> p)  {
        if (p != null && p.get() != null) {
            return Optional.of(new AggregateGuard(p.get()));
        }
        return Optional.empty();
    }
    
}
