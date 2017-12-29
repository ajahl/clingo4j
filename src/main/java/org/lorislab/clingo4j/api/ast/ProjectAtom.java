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
import org.lorislab.clingo4j.api.c.clingo_ast_project;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class ProjectAtom implements StatementData {

    private final Term atom;
    private final List<BodyLiteral> body;

    public ProjectAtom(Term atom, List<BodyLiteral> body) {
        this.atom = atom;
        this.body = body;
    }

    public Term getAtom() {
        return atom;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    @Override
    public clingo_ast_statement createStatment() {
        return ASTToC.visit(this);
    }

    @Override
    public String toString() {
        return "#project " + atom + ClingoUtil.printBody(body);
    }

    public static ProjectAtom convert(clingo_ast_project p) {
        return new ProjectAtom(Term.convTerm(p.atom()), new BodyLiteral.BodyLiteralList(p.body(), p.size()));
    }
}
