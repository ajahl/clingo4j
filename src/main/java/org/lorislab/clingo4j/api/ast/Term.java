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
import org.lorislab.clingo4j.util.SpanList;
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
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author andrej
 */
public class Term implements ASTObject<clingo_ast_term>, LiteralData {

    private final Location location;

    //Symbol, Variable, UnaryOperation, BinaryOperation, Interval, Function, Pool
    private final TermData data;

    public Term(final clingo_ast_term term) {
        TermType type = EnumValue.valueOfInt(TermType.class, term.type());
        if (type != null) {
            location = new Location(term.location());
            switch (type) {
                case SYMBOL:
                    data = new Symbol(term.field1().symbol());
                    break;
                case VARIABLE:
                    data = new Variable(term.field1().variable().getCString());
                    break;
                case UNARY_OPERATION:
                    clingo_ast_unary_operation op = term.field1().unary_operation().get();
                    data = new UnaryOperation(EnumValue.valueOfInt(UnaryOperator.class, op.unary_operator()), new Term(op.argument()));
                    break;
                case BINARY_OPERATION:
                    clingo_ast_binary_operation bop = term.field1().binary_operation().get();
                    data = new BinaryOperation(EnumValue.valueOfInt(BinaryOperator.class, bop.binary_operator()), new Term(bop.left()), new Term(bop.right()));
                    break;
                case INTERVAL:
                    clingo_ast_interval inter = term.field1().interval().get();
                    data = new Interval(new Term(inter.left()), new Term(inter.right()));
                    break;
                case FUNCTION:
                    clingo_ast_function fn = term.field1().function().get();
                    data = new Function(fn.name().getCString(), new TermList(fn.arguments(), fn.size()), false);
                    break;
                case EXTERNAL_FUNCTION:
                    clingo_ast_function efn = term.field1().external_function().get();
                    data = new Function(efn.name().getCString(), new TermList(efn.arguments(), efn.size()), true);
                    break;
                case POOL:
                    clingo_ast_pool p = term.field1().pool().get();
                    data = new Pool(new TermList(p.arguments(), p.size()));
                    break;
                default:
                    data = null;
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

    @Override
    public clingo_ast_term create() {
        clingo_ast_term ret = new clingo_ast_term();
        ret.type(data.getTermType().getInt());
        ret.location(location);
        data.updateTerm(ret);
        return ret;
    }

    @Override
    public void updateLiteral(clingo_ast_literal ret) {
        ret.field1().symbol(Pointer.getPointer(create()));
    }

    @Override
    public LiteralType getLiteralType() {
        return LiteralType.SYMBOLIC;
    }

    public interface TermData {

        public void updateTerm(clingo_ast_term ret);

        public TermType getTermType();
        
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
