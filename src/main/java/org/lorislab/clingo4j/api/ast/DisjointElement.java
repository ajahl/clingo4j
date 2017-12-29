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
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.SpanList;
import org.lorislab.clingo4j.api.ast.Literal.LiteralList;
import org.lorislab.clingo4j.api.c.clingo_ast_disjoint_element;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class DisjointElement {

    private final Location location;
    private final List<Term> tuple;
    private final CSPSum term;
    private final List<Literal> condition;

    public DisjointElement(Location location, List<Term> tuple, CSPSum term, List<Literal> condition) {
        this.location = location;
        this.tuple = tuple;
        this.term = term;
        this.condition = condition;
    }

    public List<Literal> getCondition() {
        return condition;
    }

    public Location getLocation() {
        return location;
    }

    public CSPSum getTerm() {
        return term;
    }

    public List<Term> getTuple() {
        return tuple;
    }

    @Override
    public String toString() {
        return ClingoUtil.print(tuple, "", ",", "", false) + " : " + term + " : " + ClingoUtil.print(condition, "", ",", "", false);
    }

    public static DisjointElement convert(clingo_ast_disjoint_element e) {
        return new DisjointElement(new Location(e.location()), new Term.TermList(e.tuple(), e.tuple_size()), CSPSum.convCSPAdd(e.term()), new LiteralList(e.condition(), e.condition_size()));
    }
    
    public static class DisjointElementList extends SpanList<DisjointElement, clingo_ast_disjoint_element> {

        public DisjointElementList(Pointer<clingo_ast_disjoint_element> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected DisjointElement getItem(Pointer<clingo_ast_disjoint_element> p) {
            return convert(p.get());
        }
        
    }
    
}
