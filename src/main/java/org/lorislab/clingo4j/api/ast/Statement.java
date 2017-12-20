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
import org.lorislab.clingo4j.api.c.clingo_ast_statement;

/**
 *
 * @author andrej
 */
public class Statement {

    private Location location;

    //Rule, Definition, ShowSignature, ShowTerm, Minimize, Script, Program, External, Edge, Heuristic, ProjectAtom, ProjectSignature, TheoryDefinition
    private StatementData data;

    public Statement(Location location, StatementData data) {
        this.location = location;
        this.data = data;
    }

    public StatementData getData() {
        return data;
    }

    public Location getLocation() {
        return location;
    }

    public clingo_ast_statement createStatment() {
        return ASTToC.convStatement(this);
    }

    public interface StatementData {

        public clingo_ast_statement createStatment();

    }

    @Override
    public String toString() {
        return "" + data;
    }

    
}
