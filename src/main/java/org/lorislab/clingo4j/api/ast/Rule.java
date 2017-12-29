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
import org.lorislab.clingo4j.api.c.clingo_ast_rule;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Rule implements StatementData {

    private final HeadLiteral head;
    private final List<BodyLiteral> body;

    public Rule(HeadLiteral head, List<BodyLiteral> body) {
        this.head = head;
        this.body = body;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    public HeadLiteral getHead() {
        return head;
    }

    @Override
    public clingo_ast_statement createStatment() {
        return ASTToC.visit(this);
    }

    @Override
    public String toString() {
        return "" + head + ClingoUtil.printBody(body, " :- ");
    }

    public static Rule convert(clingo_ast_rule r) {
        return new Rule(HeadLiteral.convHeadLiteral(r.head()), new BodyLiteral.BodyLiteralList(r.body(), r.size()));
    }
}
