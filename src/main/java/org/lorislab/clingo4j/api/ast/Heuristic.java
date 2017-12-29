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
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralList;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_heuristic;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Heuristic implements StatementData {
    
    private final Term atom;
    private final List<BodyLiteral> body;
    private final Term bias;
    private final Term priority;
    private final Term modifier;    

    public Heuristic(Term atom, List<BodyLiteral> body, Term bias, Term priority, Term modifier) {
        this.atom = atom;
        this.body = body;
        this.bias = bias;
        this.priority = priority;
        this.modifier = modifier;
    }

    public Term getAtom() {
        return atom;
    }

    public Term getBias() {
        return bias;
    }

    public List<BodyLiteral> getBody() {
        return body;
    }

    public Term getModifier() {
        return modifier;
    }

    public Term getPriority() {
        return priority;
    }

    @Override
    public clingo_ast_statement createStatment() {
        return ASTToC.visit(this);
    }

    @Override
    public String toString() {
        return "#heuristic " + atom + ClingoUtil.printBody(body) + " [" + bias+ "@" + priority + "," + modifier + "]";
    }
    
    public static Heuristic convert(clingo_ast_heuristic h) {
        return new Heuristic(Term.convTerm(h.atom()), new BodyLiteralList(h.body(), h.size()), Term.convTerm(h.bias()), Term.convTerm(h.priority()), Term.convTerm(h.modifier()));
    }
    
}
