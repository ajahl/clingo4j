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
import org.lorislab.clingo4j.api.ast.Literal.LiteralData;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.c.clingo_ast_binary_operation;
import org.lorislab.clingo4j.api.c.clingo_ast_function;
import org.lorislab.clingo4j.api.c.clingo_ast_interval;
import org.lorislab.clingo4j.api.c.clingo_ast_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_pool;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.api.c.clingo_ast_unary_operation;

/**
 *
 * @author andrej
 */
public class Term implements LiteralData {

    private Location location;

    //Symbol, Variable, UnaryOperation, BinaryOperation, Interval, Function, Pool
    private TermData data;

    public Term(final clingo_ast_term term) {
        TermType type = TermType.valueOfInt(term.type());
        if (type != null) {
            location = new Location(term.location());
            switch (type) {
                case SYMBOL:
                    data = new Symbol(term.field1().symbol());
                case VARIABLE:
                    data = new Variable(term.field1().variable().getCString());
                case UNARY_OPERATION:
                    clingo_ast_unary_operation op = term.field1().unary_operation().get();
                    data = new UnaryOperation(UnaryOperator.valueOfInt(op.unary_operator()), new Term(op.argument()));
                case BINARY_OPERATION:
                    clingo_ast_binary_operation bop = term.field1().binary_operation().get();
                    data = new BinaryOperation(BinaryOperator.valueOfInt(bop.binary_operator()), new Term(bop.left()), new Term(bop.right()));
                case INTERVAL:
                    clingo_ast_interval inter = term.field1().interval().get();
                    data = new Interval(new Term(inter.left()), new Term(inter.right()));
                case FUNCTION:
                    clingo_ast_function fn = term.field1().function().get();
                    data = new Function(fn.name().getCString(), new TermList(fn.arguments(), fn.size()), false);
                case EXTERNAL_FUNCTION:
                    clingo_ast_function efn = term.field1().external_function().get();
                    data = new Function(efn.name().getCString(), new TermList(efn.arguments(), efn.size()), true);
                case POOL:
                    clingo_ast_pool p = term.field1().pool().get();
                    data = new Pool(new TermList(p.arguments(), p.size()));
            }
        } else {
            throw new RuntimeException("cannot happen");
        }
    }

    public Term(Location location, TermData data) {
        this.location = location;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public clingo_ast_term createTerm() {
        return ASTToC.convTerm(this);
    }

    public TermData getData() {
        return data;
    }

    @Override
    public clingo_ast_literal createLiteral() {
        return ASTToC.visit(this);
    }

    public interface TermData {

        public clingo_ast_term createTerm();

    }

    @Override
    public String toString() {
        return "" + data;
    }

    public static class TermList extends SpanList<Term, clingo_ast_term> {

        public TermList(Pointer<clingo_ast_term> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected Term getItem(Pointer<clingo_ast_term> p) {
            return new Term(p.get());
        }

    }

}
