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
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_disjoint;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Disjoint implements BodyLiteralData {
    
    private final List<DisjointElement> elements;

    public Disjoint(List<DisjointElement> elements) {
        this.elements = elements;
    }

    public List<DisjointElement> getElements() {
        return elements;
    }

    @Override
    public clingo_ast_body_literal createBodyLiteral() {
        return ASTToC.visitBodyLiteral(this);
    }

    @Override
    public String toString() {
        return "#disjoint { " + ClingoUtil.print(elements, "", "; ", "", false) + " }";
    }
    
    public static Disjoint convert(clingo_ast_disjoint d)  {
        return new Disjoint(new DisjointElement.DisjointElementList(d.elements(), d.size()));
    }
    
}
