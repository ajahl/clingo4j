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
import org.lorislab.clingo4j.c.api.clingo_ast_statement;

/**
 *
 * @author andrej
 */
public class Script implements StatementData {

    private ScriptType type;
    private String code;

    public String getCode() {
        return code;
    }

    public ScriptType getType() {
        return type;
    }

    @Override
    public clingo_ast_statement createStatment() {
        return ASTToC.visit(this);
    }
    
    
}
