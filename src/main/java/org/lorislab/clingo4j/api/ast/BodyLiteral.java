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
import org.lorislab.clingo4j.c.api.clingo_ast_body_literal;

/**
 *
 * @author andrej
 */
public class BodyLiteral {

    private Location location;
    private Sign sign;
    //Literal, ConditionalLiteral, Aggregate, BodyAggregate, TheoryAtom, Disjoint    
    private BodyLiteralData data;

    public Location getLocation() {
        return location;
    }

    public BodyLiteralData getData() {
        return data;
    }

    public Sign getSign() {
        return sign;
    }

    public clingo_ast_body_literal createBodyLiteral() {
        return ASTToC.convBodyLiteral(this);
    }
    
    public interface BodyLiteralData {

        public clingo_ast_body_literal createBodyLiteral();

    }

    @Override
    public String toString() {
        return "" + sign + data;
    }
    
    
}
