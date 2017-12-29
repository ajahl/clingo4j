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
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_edge;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Edge implements StatementData {

    private final Term u;
    private final Term v;
    private final List<BodyLiteral> body;

    public Edge(Term u, Term v, List<BodyLiteral> body) {
        this.u = u;
        this.v = v;
        this.body = body;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    public Term getU() {
        return u;
    }

    public Term getV() {
        return v;
    }

    @Override
    public clingo_ast_statement createStatment() {
        return ASTToC.visit(this);
    }

    @Override
    public String toString() {
        return "#edge (" + u + "," + v + ")" + ClingoUtil.printBody(body);
    }

    public static Edge convert(clingo_ast_edge e) {
        return new Edge(Term.convTerm(e.u()), Term.convTerm(e.v()), new BodyLiteral.BodyLiteralList(e.body(), e.size()));
    }
    
}
