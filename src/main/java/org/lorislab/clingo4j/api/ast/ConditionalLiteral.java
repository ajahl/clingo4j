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

import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.util.SpanList;
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_conditional_literal;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class ConditionalLiteral implements BodyLiteralData {

    private final Literal literal;
    private final List<Literal> condition;

    public ConditionalLiteral(clingo_ast_conditional_literal lit) {
        this(new Literal(lit.literal()), new Literal.LiteralList(lit.condition(), lit.size()));
    }
    
    public ConditionalLiteral(Literal literal, List<Literal> condition) {
        this.literal = literal;
        this.condition = condition;
    }

    public List<Literal> getCondition() {
        return condition;
    }

    public Literal getLiteral() {
        return literal;
    }

    @Override
    public clingo_ast_body_literal createBodyLiteral() {
        return ASTToC.visitBodyLiteral(this);
    }

    @Override
    public String toString() {
        return "" + literal + ClingoUtil.print(condition, " : ", ", ", "", true);
    }
    
    public static class ConditionalLiteralList extends SpanList<ConditionalLiteral, clingo_ast_conditional_literal> {

        public ConditionalLiteralList(Pointer<clingo_ast_conditional_literal> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected ConditionalLiteral getItem(Pointer<clingo_ast_conditional_literal> p) {
            return new ConditionalLiteral(p.get());
        }
        
    }

}
