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

    private final Location location;

    //Rule, Definition, ShowSignature, ShowTerm, Minimize, Script, Program, External, Edge, Heuristic, ProjectAtom, ProjectSignature, TheoryDefinition
    private final StatementData data;

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

    public static Statement convStatement(clingo_ast_statement stm) {

        StatementType type = StatementType.valueOfInt(stm.type());
        if (type != null) {
            StatementData data = null;
            switch (type) {
                case RULE:
                    data = Rule.convert(stm.field1().rule().get());
                    break;
                case CONST:
                    data = Definition.convert(stm.field1().definition().get());
                    break;
                case SHOW_SIGNATURE:
                    data = ShowSignature.convert(stm.field1().show_signature().get());
                    break;
                case SHOW_TERM:
                    data = ShowTerm.convert(stm.field1().show_term().get());
                    break;
                case MINIMIZE:
                    data = Minimize.convert(stm.field1().minimize().get());
                    break;
                case SCRIPT:
                    data = Script.convert(stm.field1().script().get());
                    break;
                case PROGRAM:
                    data = Program.convert(stm.field1().program().get());
                    break;
                case EXTERNAL:
                    data = External.convert(stm.field1().external().get());
                    break;
                case EDGE:
                    data = Edge.convert(stm.field1().edge().get());
                    break;
                case HEURISTIC:
                    data = Heuristic.convert(stm.field1().heuristic().get());
                    break;
                case PROJECT_ATOM:
                    data = ProjectAtom.convert(stm.field1().project_atom().get());
                    break;
                case PROJECT_ATOM_SIGNATURE:
                    data = new ProjectSignature(new Signature(stm.field1().project_signature()));
                    break;
                case THEORY_DEFINITION:
                    data = TheoryDefinition.convert(stm.field1().theory_definition().get());
                    break;
            }
            if (data != null) {
                return new Statement(new Location(stm.location()), data);
            }
        }
        return null;
    }

}
