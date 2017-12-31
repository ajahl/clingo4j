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
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.SpanList;
import org.lorislab.clingo4j.api.Symbol;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_function;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term_array;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_unparsed_term;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author andrej
 */
public class TheoryTerm {

    private final Location location;

    //Symbol, Variable, TheoryTermSequence, TheoryFunction, TheoryUnparsedTerm
    private final TheoryTermData data;

    public TheoryTerm(clingo_ast_theory_term t) {
        TheoryTermType type = EnumValue.valueOfInt(TheoryTermType.class, t.type());
        if (type != null) {
            location = new Location(t.location());
            switch (type) {
                case SYMBOL:
                    data = new Symbol(t.field1().symbol());
                    break;
                case VARIABLE:
                    data = new Variable(t.field1().variable().getCString());
                    break;
                case LIST:
                    clingo_ast_theory_term_array a = t.field1().list().get();
                    data = new TheoryTermSequence(TheoryTermSequenceType.LIST, new TheoryTermList(a.terms(), a.size()));
                    break;
                case SET:
                    clingo_ast_theory_term_array s = t.field1().set().get();
                    data = new TheoryTermSequence(TheoryTermSequenceType.SET, new TheoryTermList(s.terms(), s.size()));
                    break;
                case TUPLE:
                    clingo_ast_theory_term_array x = t.field1().tuple().get();
                    data = new TheoryTermSequence(TheoryTermSequenceType.TUPLE, new TheoryTermList(x.terms(), x.size()));
                    break;
                case FUNCTIONS:
                    clingo_ast_theory_function fn = t.field1().function().get();
                    data = new TheoryFunction(fn.name().getCString(), new TheoryTermList(fn.arguments(), fn.size()));
                    break;
                case UNPARSED_TERM:
                    clingo_ast_theory_unparsed_term un = t.field1().unparsed_term().get();
                    data = new TheoryUnparsedTerm(new TheoryUnparsedTermElement.TheoryUnparsedTermElementList(un.elements(), un.size()));
                    break;
                default:
                    data = null;
            }
        } else {
            throw new RuntimeException("cannot happen");
        }
    }

    public TheoryTerm(Location location, TheoryTermData data) {
        this.location = location;
        this.data = data;
    }

    public clingo_ast_theory_term createTheoryTerm() {
        return ASTToC.convTheoryTerm(this);
    }

    public TheoryTermData getData() {
        return data;
    }

    public Location getLocation() {
        return location;
    }

    public interface TheoryTermData {

        public clingo_ast_theory_term createTheoryTerm();
    }

    @Override
    public String toString() {
        return "" + data;
    }

    public static class TheoryTermList extends SpanList<TheoryTerm, clingo_ast_theory_term> {

        public TheoryTermList(Pointer<clingo_ast_theory_term> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected TheoryTerm getItem(Pointer<clingo_ast_theory_term> p) {
            return new TheoryTerm(p.get());
        }

    }

}
