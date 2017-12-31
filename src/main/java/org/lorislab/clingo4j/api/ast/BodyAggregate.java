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

import org.lorislab.clingo4j.util.EnumValue;
import java.util.List;
import java.util.Optional;
import org.lorislab.clingo4j.api.ast.BodyAggregateElement.BodyAggregateElementList;
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_aggregate;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class BodyAggregate implements BodyLiteralData {

    private final AggregateFunction function;
    private final List<BodyAggregateElement> elements;
    private final Optional<AggregateGuard> leftGuard;
    private final Optional<AggregateGuard> rightGuard;

    public BodyAggregate(clingo_ast_body_aggregate a) {
        this(EnumValue.valueOfInt(AggregateFunction.class, a.function()), new BodyAggregateElementList(a.elements(), a.size()), ClingoUtil.optional(AggregateGuard::new,a.left_guard()), ClingoUtil.optional(AggregateGuard::new, a.right_guard()));
    }
    
    public BodyAggregate(AggregateFunction function, List<BodyAggregateElement> elements, Optional<AggregateGuard> leftGuard, Optional<AggregateGuard> rightGuard) {
        this.function = function;
        this.elements = elements;
        this.leftGuard = leftGuard;
        this.rightGuard = rightGuard;
    }

    public List<BodyAggregateElement> getElements() {
        return elements;
    }

    public AggregateFunction getFunction() {
        return function;
    }

    public Optional<AggregateGuard> getLeftGuard() {
        return leftGuard;
    }

    public Optional<AggregateGuard> getRightGuard() {
        return rightGuard;
    }

    @Override
    public clingo_ast_body_literal createBodyLiteral() {
        return ASTToC.visitBodyLiteral(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (leftGuard.isPresent()) {
            sb.append(leftGuard.get().getTerm()).append(" ").append(leftGuard.get().getOperator()).append(" ");
        }
        sb.append(" { ").append(ClingoUtil.print(elements, "", "; ", "", false)).append(" }");
        if (rightGuard.isPresent()) {
           sb.append(" ").append(rightGuard.get().getOperator()).append(" ").append(rightGuard.get().getTerm());
        }
        return sb.toString();
    }
    
    
}
