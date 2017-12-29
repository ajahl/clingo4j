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

import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.c.clingo_ast_head_literal;

/**
 *
 * @author andrej
 */
public class HeadLiteral {

    private final Location location;

    //Literal, Disjunction, Aggregate, HeadAggregate, TheoryAtom   
    private final HeadLiteralData data;

    public HeadLiteral(Location location, HeadLiteralData data) {
        this.location = location;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public HeadLiteralData getData() {
        return data;
    }

    public clingo_ast_head_literal createHeadLiteral() {
        return ASTToC.convHeadLiteral(this);
    }

    public interface HeadLiteralData {

        public clingo_ast_head_literal createHeadLiteral();
    }

    @Override
    public String toString() {
        return "" + data;
    }

    public static HeadLiteral convHeadLiteral(final clingo_ast_head_literal head) {

        HeadLiteralType type = HeadLiteralType.valueOfInt(head.type());
        if (type != null) {
            Location loc = new Location(head.location());
            HeadLiteralData data = null;
            switch (type) {
                case LITERAL:
                    data = Literal.convLiteral(head.field1().literal().get());
                    break;
                case DISJUNCTION:
                    data = Disjunction.convert(head.field1().disjunction().get());
                    break;
                case AGGREGATE:
                    data = Aggregate.convert(head.field1().aggregate().get());
                    break;
                case HEAD_AGGREGATE:
                    data = HeadAggregate.convert(head.field1().head_aggregate().get());
                    break;
                case THEORY_ATOM:
                    data = TheoryAtom.convert(head.field1().theory_atom().get());
                    break;
            }
            if (data != null) {
                return new HeadLiteral(loc, data);
            }
        }
        throw new RuntimeException("cannot happen!");
    }

}
