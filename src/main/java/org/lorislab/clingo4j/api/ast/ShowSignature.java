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

import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_show_signature;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;

/**
 *
 * @author andrej
 */
public class ShowSignature implements StatementData {

    private final Signature signature;
    private final boolean csp;

    public ShowSignature(clingo_ast_show_signature s) {
        this(new Signature(s.signature()), s.csp());
    }
    
    public ShowSignature(Signature signature, boolean csp) {
        this.signature = signature;
        this.csp = csp;
    }
    
    public Signature getSignature() {
        return signature;
    }

    public boolean isCsp() {
        return csp;
    }

    @Override
    public clingo_ast_statement createStatment() {
        return ASTToC.visit(this);
    }

    @Override
    public String toString() {
        return "#show " + (csp ? "$" : "") + signature + ".";
    }
    
}
