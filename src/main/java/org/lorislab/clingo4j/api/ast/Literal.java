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
import org.lorislab.clingo4j.api.ast.BodyLiteral.BodyLiteralData;
import org.lorislab.clingo4j.api.ast.HeadLiteral.HeadLiteralData;
import org.lorislab.clingo4j.c.api.clingo_ast_body_literal;
import org.lorislab.clingo4j.c.api.clingo_ast_head_literal;
import org.lorislab.clingo4j.c.api.clingo_ast_literal;

/**
 *
 * @author andrej
 */
public class Literal implements BodyLiteralData, HeadLiteralData {
    
    private Location location;
    private Sign sign;
    
    //Boolean, Term, Comparison, CSPLiteral
    private LiteralData data;

    public Location getLocation() {
        return location;
    }

    public Sign getSign() {
        return sign;
    }

    public LiteralData getData() {
        return data;
    }

    @Override
    public clingo_ast_body_literal createBodyLiteral() {
        return ASTToC.visitBodyLiteral(this);
    }
    
    public clingo_ast_literal createLiteral() {
        return ASTToC.convLiteral(this);
    }

    @Override
    public clingo_ast_head_literal createHeadLiteral() {
        return ASTToC.visitHeadLiteral(this);
    }
    
    public interface LiteralData {
        
        public clingo_ast_literal createLiteral();
        
    }
    
    
}
