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
import org.lorislab.clingo4j.api.ast.Literal.LiteralData;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.c.api.clingo_ast_literal;
import org.lorislab.clingo4j.c.api.clingo_ast_term;

/**
 *
 * @author andrej
 */
public class Term implements LiteralData {
    
    private Location location;
    
    //Symbol, Variable, UnaryOperation, BinaryOperation, Interval, Function, Pool
    private TermData data;

    public Term(Location location, TermData data) {
        this.location = location;
        this.data = data;
    }

    public Location getLocation() {
        return location;
    }
     
    public clingo_ast_term createTerm() {
         return ASTToC.convTerm(this);
    }

    public TermData getData() {
        return data;
    }

    @Override
    public clingo_ast_literal createLiteral() {
        return ASTToC.visit(this);
    }
     
    public interface TermData {
        
        public clingo_ast_term createTerm();
        
    }
}
