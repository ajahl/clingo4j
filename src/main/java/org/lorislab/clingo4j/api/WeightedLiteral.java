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
package org.lorislab.clingo4j.api;

import java.util.List;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.c.clingo_weighted_literal;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class WeightedLiteral extends clingo_weighted_literal {
    
    private final clingo_weighted_literal literal;

    public WeightedLiteral(clingo_weighted_literal literal) {
        this.literal = literal;
    }
    
    public WeightedLiteral(int literal, int weight) {
        this.literal = new clingo_weighted_literal();
        this.literal.weight(weight);
        this.literal.literal(literal);
    }

    public int weight() {
        return literal.weight();
    }
    
    public int literal() {
        return literal.literal();
    }

    public static clingo_weighted_literal get(WeightedLiteral lit) {
        return lit.literal;
    }
    
    public static Pointer<clingo_weighted_literal> toArray(List<WeightedLiteral> list) {
        return ClingoUtil.createArray(list, clingo_weighted_literal.class, WeightedLiteral::get);
    }
    
    public static class WeightedLiteralList extends SpanList<WeightedLiteral, clingo_weighted_literal> {

        public WeightedLiteralList(Pointer<clingo_weighted_literal> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected WeightedLiteral getItem(Pointer<clingo_weighted_literal> p) {
            return new WeightedLiteral(p.get());
        }
        
    }
}
