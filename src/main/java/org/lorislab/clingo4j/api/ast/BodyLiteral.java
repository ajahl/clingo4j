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
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;

/**
 *
 * @author andrej
 */
public class BodyLiteral {

    private final Location location;
    private final Sign sign;
    //Literal, ConditionalLiteral, Aggregate, BodyAggregate, TheoryAtom, Disjoint    
    private final BodyLiteralData data;

    public BodyLiteral(Location location, Sign sign, BodyLiteralData data) {
        this.location = location;
        this.sign = sign;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public BodyLiteralData getData() {
        return data;
    }

    public Sign getSign() {
        return sign;
    }

    public clingo_ast_body_literal createBodyLiteral() {
        return ASTToC.convBodyLiteral(this);
    }

    public interface BodyLiteralData {

        public clingo_ast_body_literal createBodyLiteral();

    }

    public static BodyLiteral convert(clingo_ast_body_literal lit) {

        BodyLiteralType type = BodyLiteralType.valueOfInt(lit.type());
        if (type != null) {
            BodyLiteralData data = null;
            switch (type) {
                case LITERAL:
                    data = Literal.convLiteral(lit.field1().literal().get());
                    break;
                case CONDITIONAL:
                    data = ConditionalLiteral.convert(lit.field1().conditional().get());
                    break;
                case AGGREGATE:
                    data = Aggregate.convert(lit.field1().aggregate().get());
                    break;
                case BODY_AGGREGATE:
                    data = BodyAggregate.convert(lit.field1().body_aggregate().get());
                    break;
                case THEORY_ATOM:
                    data = TheoryAtom.convert(lit.field1().theory_atom().get());
                    break;
                case DISJOINT:
                    data = Disjoint.convert(lit.field1().disjoint().get());
                    break;
            }
            if (data != null) {
                return new BodyLiteral(new Location(lit.location()), Sign.valueOfInt(lit.sign()), data);
            }
        }
        throw new RuntimeException("cannot happen");
    }

    
    @Override
    public String toString() {
        return "" + sign + data;
    }

    public static class BodyLiteralList extends SpanList<BodyLiteral, clingo_ast_body_literal> {

        public BodyLiteralList(Pointer<clingo_ast_body_literal> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected BodyLiteral getItem(Pointer<clingo_ast_body_literal> p) {
            return convert(p.get());
        }

    }
}
