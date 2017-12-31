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
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.ast.HeadLiteral.HeadLiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_comparison;
import org.lorislab.clingo4j.api.c.clingo_ast_csp_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_head_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_literal;
import org.lorislab.clingo4j.util.ClingoUtil;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author andrej
 */
public class Literal implements BodyLiteralData, HeadLiteralData {

    private final Location location;
    private final Sign sign;

    //Boolean, Term, Comparison, CSPLiteral
    private final LiteralData data;

    public Literal(clingo_ast_literal lit) {
        LiteralType type = EnumValue.valueOfInt(LiteralType.class, lit.type());
        if (type != null) {
            switch (type) {
                case BOOLEAN:
                    data = new Boolean(lit.field1().boolean$());
                    break;
                case SYMBOLIC:
                    data = new Term(lit.field1().symbol().get());
                    break;
                case COMPARISON:
                    clingo_ast_comparison com = lit.field1().comparison().get();
                    data = new Comparison(EnumValue.valueOfInt(ComparisonOperator.class, com.comparison()), new Term(com.left()), new Term(com.right()));
                    break;
                case CSP:
                    clingo_ast_csp_literal csp = lit.field1().csp_literal().get();
                    data = new CSPLiteral(new CSPSum(csp.term()), new CSPGuard.CSPGuardList(csp.guards(), csp.size()));
                    break;
                default:
                    data = null;
            }
            location = new Location(lit.location());
            sign = EnumValue.valueOfInt(Sign.class, lit.sign());
        } else {
            throw new  RuntimeException("cannot happen");
        }
    }

    public Literal(Location location, Sign sign, LiteralData data) {
        this.location = location;
        this.sign = sign;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }

    public Sign getSign() {
        return sign;
    }

    public LiteralData getData() {
        return data;
    }

    @Override
    public clingo_ast_body_literal createBodyLiteral() {
        return ASTToC.visitBodyLiteral(this);
    }

    public clingo_ast_literal createLiteral() {
        return ASTToC.convLiteral(this);
    }

    @Override
    public clingo_ast_head_literal createHeadLiteral() {
        return ASTToC.visitHeadLiteral(this);
    }

    public interface LiteralData {

        public clingo_ast_literal createLiteral();

    }

    @Override
    public String toString() {
        return "" + sign + data;
    }

    public static LiteralIntegerList toLiteralList(List<Integer> list) {
        if (list == null) {
            return null;
        }
        if (list instanceof LiteralIntegerList) {
            return (LiteralIntegerList) list;
        }
        if (list.isEmpty()) {
            return null;
        }
        int size = ClingoUtil.arraySize(list);
        Pointer<Integer> tmp = ClingoUtil.createArray(list, Integer.class);
        return new LiteralIntegerList(tmp, size);
    }

    public static class LiteralIntegerList extends SpanList<Integer, Integer> {

        public LiteralIntegerList(Pointer<Integer> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected Integer getItem(Pointer<Integer> p) {
            return p.getInt();
        }

    }

    public static class LiteralList extends SpanList<Literal, clingo_ast_literal> {

        public LiteralList(Pointer<clingo_ast_literal> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected Literal getItem(Pointer<clingo_ast_literal> p) {
            return new Literal(p.get());
        }

    }

}
