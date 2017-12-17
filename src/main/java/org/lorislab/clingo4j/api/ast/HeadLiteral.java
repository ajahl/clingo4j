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
import org.lorislab.clingo4j.c.api.clingo_ast_head_literal;

/**
 *
 * @author andrej
 */
public class HeadLiteral {

    private Location location;
    
    //Literal, Disjunction, Aggregate, HeadAggregate, TheoryAtom   
    private HeadLiteralData data;

    public Location getLocation() {
        return location;
    }

    public HeadLiteralData getData() {
        return data;
    }

    public clingo_ast_head_literal createHeadLiteral() {
        return ASTToC.convHeadLiteral(this);
    }

    public interface HeadLiteralData {

        public clingo_ast_head_literal createHeadLiteral();
    }
}
