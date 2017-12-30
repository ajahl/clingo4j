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
import org.lorislab.clingo4j.api.ast.HeadLiteral.HeadLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_disjunction;
import org.lorislab.clingo4j.api.c.clingo_ast_head_literal;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Disjunction implements HeadLiteralData {
    
    private final List<ConditionalLiteral> elements;

    public Disjunction(clingo_ast_disjunction dis) {
        this(new ConditionalLiteral.ConditionalLiteralList(dis.elements(), dis.size()));
    }
    
    public Disjunction(List<ConditionalLiteral> elements) {
        this.elements = elements;
    }

    public List<ConditionalLiteral> getElements() {
        return elements;
    }

    @Override
    public clingo_ast_head_literal createHeadLiteral() {
        return ASTToC.visitHeadLiteral(this);
    }

    @Override
    public String toString() {
        return ClingoUtil.print(elements, "", "; ", "", false);
    }
    
}
