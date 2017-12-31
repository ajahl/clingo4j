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
import org.lorislab.clingo4j.api.ast.Literal.LiteralData;
import org.lorislab.clingo4j.api.c.clingo_ast_csp_guard;
import org.lorislab.clingo4j.api.c.clingo_ast_csp_literal;
import org.lorislab.clingo4j.api.c.clingo_ast_literal;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class CSPLiteral implements LiteralData {

    private final CSPSum term;

    private final List<CSPGuard> guards;

    public CSPLiteral(CSPSum term, List<CSPGuard> guards) {
        this.term = term;
        this.guards = guards;
    }

    public List<CSPGuard> getGuards() {
        return guards;
    }

    public CSPSum getTerm() {
        return term;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(term);
        sb.append(ClingoUtil.print(guards, "", "", "", false));
        return sb.toString();
    }

    @Override
    public void updateLiteral(clingo_ast_literal ret) {
        clingo_ast_csp_literal csp = new clingo_ast_csp_literal();
        csp.term(term.create());
        csp.guards(ClingoUtil.createASTObjectArray(guards, clingo_ast_csp_guard.class));
        csp.size(ClingoUtil.arraySize(guards));
        ret.field1().csp_literal(Pointer.getPointer(csp));
    }

    @Override
    public LiteralType getLiteralType() {
        return LiteralType.CSP;
    }

}
