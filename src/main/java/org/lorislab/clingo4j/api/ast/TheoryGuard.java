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

import java.util.Optional;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_guard;

/**
 *
 * @author andrej
 */
public class TheoryGuard {
    
    private final  String operatorName;
    
    private final TheoryTerm term;    

    public TheoryGuard(String operatorName, TheoryTerm term) {
        this.operatorName = operatorName;
        this.term = term;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public TheoryTerm getTerm() {
        return term;
    }

    @Override
    public String toString() {
        return operatorName + " " + term;
    }
    
   public static Optional<TheoryGuard> convert(Pointer<clingo_ast_theory_guard> p)  {
        if (p != null && p.get() != null) {
            return Optional.of(convert(p.get()));
        }
        return Optional.empty();
    }
    
    public static TheoryGuard convert(clingo_ast_theory_guard g) {
        return new TheoryGuard(g.operator_name().getCString(), TheoryTerm.convert(g.term()));
    }
}
