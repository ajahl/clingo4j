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
import org.lorislab.clingo4j.api.ast.Id.IdList;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_id;
import org.lorislab.clingo4j.api.c.clingo_ast_program;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ASTObject;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Program implements ASTObject<clingo_ast_program>, StatementData {

    private final String name;
    private final List<Id> parameters;

    public Program(clingo_ast_program p) {
        this(p.name().getCString(), new IdList(p.parameters(), p.size()));
    }
    
    public Program(String name, List<Id> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public List<Id> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "#program " + name + ClingoUtil.print(parameters, "(", ",", ")", false) + ".";
    }

    @Override
    public clingo_ast_program create() {
        clingo_ast_program ret = new  clingo_ast_program();
        ret.name(Pointer.pointerToCString(name));
        ret.parameters(ASTObject.array(parameters, clingo_ast_id.class));
        ret.size(ClingoUtil.arraySize(parameters));
        return ret;
    }

    @Override
    public void updateStatement(clingo_ast_statement ret) {
        ret.field1().program(createPointer());
    }

    @Override
    public StatementType getStatementType() {
        return StatementType.PROGRAM;
    }

}
