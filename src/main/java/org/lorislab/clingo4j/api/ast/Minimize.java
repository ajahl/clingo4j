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
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_body_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_minimize;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Minimize implements ASTObject<clingo_ast_minimize>, StatementData {

    private final Term weight;
    private final Term priority;
    private final List<Term> tuple;
    private final List<BodyLiteral> body;

    public Minimize(clingo_ast_minimize m) {
        this(new Term(m.weight()), new Term(m.priority()), new  Term.TermList(m.tuple(), m.tuple_size()), new BodyLiteral.BodyLiteralList(m.body(), m.body_size()));
    }
        
    public Minimize(Term weight, Term priority, List<Term> tuple, List<BodyLiteral> body) {
        this.weight = weight;
        this.priority = priority;
        this.tuple = tuple;
        this.body = body;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    public Term getPriority() {
        return priority;
    }

    public List<Term> getTuple() {
        return tuple;
    }

    public Term getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return ClingoUtil.printBody(body, ":~ ") + " [" + weight + "@" + priority + ClingoUtil.print(tuple, ",", ",", "", false) + "]";
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().minimize(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.MINIMIZE;
    }

    @Override
    public clingo_ast_minimize create() {
        clingo_ast_minimize ret = new clingo_ast_minimize();
        ret.weight(weight.create());
        ret.priority(priority.create());
        ret.tuple(ClingoUtil.createASTObjectArray(tuple, clingo_ast_term.class));
        ret.tuple_size(ClingoUtil.arraySize(tuple));
        ret.body(ClingoUtil.createASTObjectArray(body, clingo_ast_body_literal.class));
        ret.body_size(ClingoUtil.arraySize(body));
        return ret;
    }

}
