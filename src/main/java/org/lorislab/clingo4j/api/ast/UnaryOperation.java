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

import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.c.clingo_ast_term;

/**
 *
 * @author andrej
 */
public class UnaryOperation implements TermData {
    
    private UnaryOperator unaryOperator;
    
    private Term argument;

    public UnaryOperator getUnaryOperator() {
        return unaryOperator;
    }

    public Term getArgument() {
        return argument;
    }

    @Override
    public clingo_ast_term createTerm() {
        return ASTToC.visitTerm(this);
    }

    @Override
    public String toString() {
        return unaryOperator.getLeft() + argument + unaryOperator.getRight();
    }
    
}
