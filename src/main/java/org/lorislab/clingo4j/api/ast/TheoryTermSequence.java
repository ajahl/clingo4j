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
import org.lorislab.clingo4j.api.ast.TheoryTerm.TheoryTermData;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class TheoryTermSequence implements TheoryTermData {

    private final TheoryTermSequenceType type;
    
    private final List<TheoryTerm> terms;

    public TheoryTermSequence(TheoryTermSequenceType type, List<TheoryTerm> terms) {
        this.type = type;
        this.terms = terms;
    }

    public List<TheoryTerm> getTerms() {
        return terms;
    }

    public TheoryTermSequenceType getType() {
        return type;
    }

    @Override
    public clingo_ast_theory_term createTheoryTerm() {
        return ASTToC.visitTheoryTerm(this);
    }

    @Override
    public String toString() {
        boolean tc = terms != null && terms.size() == 1 && type == TheoryTermSequenceType.TUPLE;
        StringBuilder sb = new StringBuilder();
        sb.append(ClingoUtil.print(terms, type.getLeft(), ",", "", true));
        if (tc) {
            sb.append(",)");
        } else {
            sb.append(type.getRight());
        }
        return sb.toString();
    }
     
}
