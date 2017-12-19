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
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.c.api.clingo_ast_term;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Pool implements TermData {

    private List<Term> arguments;

    public List<Term> getArguments() {
        return arguments;
    }

    @Override
    public clingo_ast_term createTerm() {
        return ASTToC.visitTerm(this);
    }

    @Override
    public String toString() {
        if (arguments == null || arguments.isEmpty()) {
            return "(1/0)";
        }
        return ClingoUtil.print(arguments, "(", ";", ")", true);
    }

}
