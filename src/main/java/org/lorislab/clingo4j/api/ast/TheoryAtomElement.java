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
import org.lorislab.clingo4j.api.SpanList;
import org.lorislab.clingo4j.api.ast.Literal.LiteralList;
import org.lorislab.clingo4j.api.ast.TheoryTerm.TheoryTermList;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_atom_element;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class TheoryAtomElement {

    private final List<TheoryTerm> tuple;
    private final List<Literal> condition;

    public TheoryAtomElement(List<TheoryTerm> tuple, List<Literal> condition) {
        this.tuple = tuple;
        this.condition = condition;
    }

    public List<Literal> getCondition() {
        return condition;
    }

    public List<TheoryTerm> getTuple() {
        return tuple;
    }

    @Override
    public String toString() {
        return ClingoUtil.print(tuple, "", ",", "", false) + " : " + ClingoUtil.print(condition, "", ",", "", false);
    }

    public static TheoryAtomElement convert(clingo_ast_theory_atom_element e) {
        return new TheoryAtomElement(new TheoryTermList(e.tuple(), e.tuple_size()), new LiteralList(e.condition(), e.condition_size()));
    }
    
    public static class TheoryAtomElementList extends SpanList<TheoryAtomElement, clingo_ast_theory_atom_element> {

        public TheoryAtomElementList(Pointer<clingo_ast_theory_atom_element> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected TheoryAtomElement getItem(Pointer<clingo_ast_theory_atom_element> p) {
            return convert(p.get());
        }
        
    }
}
