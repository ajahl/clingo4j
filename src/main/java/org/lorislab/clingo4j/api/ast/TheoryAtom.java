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
import java.util.Optional;
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.ast.HeadLiteral.HeadLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_head_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_atom;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class TheoryAtom  implements HeadLiteralData, BodyLiteralData {
    
    private final Term term;
    private final List<TheoryAtomElement> elements;
    private final Optional<TheoryGuard> guard;

    public TheoryAtom(Term term, List<TheoryAtomElement> elements, Optional<TheoryGuard> guard) {
        this.term = term;
        this.elements = elements;
        this.guard = guard;
    }

    public List<TheoryAtomElement> getElements() {
        return elements;
    }

    public Optional<TheoryGuard> getGuard() {
        return guard;
    }

    public Term getTerm() {
        return term;
    }

    @Override
    public clingo_ast_head_literal createHeadLiteral() {
        return ASTToC.visitHeadLiteral(this);
    }

    @Override
    public clingo_ast_body_literal createBodyLiteral() {
        return ASTToC.visitBodyLiteral(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('&').append(term).append(" { ").append(ClingoUtil.print(elements, "", "; ", "", false)).append(" }");
        if (guard.isPresent()) {
            sb.append(" ").append(guard.get());
        }
        return sb.toString();
    }
        
    public static TheoryAtom convert(clingo_ast_theory_atom a) {
        return new TheoryAtom(Term.convTerm(a.term()), new TheoryAtomElement.TheoryAtomElementList(a.elements(), a.size()), TheoryGuard.convert(a.guard()));
    }
}
