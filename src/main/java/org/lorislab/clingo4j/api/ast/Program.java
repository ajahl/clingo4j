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
import org.lorislab.clingo4j.api.ast.Id.IdList;
import org.lorislab.clingo4j.api.ast.Statement.StatementData;
import org.lorislab.clingo4j.api.c.clingo_ast_program;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Program implements StatementData {

    private final String name;
    private final List<Id> parameters;

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
    public clingo_ast_statement createStatment() {
        return ASTToC.visit(this);
    }

    @Override
    public String toString() {
        return "#program " + name + ClingoUtil.print(parameters, "(", ",", ")", false) + ".";
    }

    public static Program convert(clingo_ast_program p) {
        return new Program(p.name().getCString(), new IdList(p.parameters(), p.size()));
    }
}
