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

import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.SpanList;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_operator_definition;

/**
 *
 * @author andrej
 */
public class TheoryOperatorDefinition {
 
    private final Location location;
    private final String name;
    private final int priority;
    private final TheoryOperatorType type;

    public TheoryOperatorDefinition(Location location, String name, int priority, TheoryOperatorType type) {
        this.location = location;
        this.name = name;
        this.priority = priority;
        this.type = type;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public TheoryOperatorType getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " : " + priority + ", " + type;
    }
    
    public static TheoryOperatorDefinition convert(clingo_ast_theory_operator_definition d) {
        return new TheoryOperatorDefinition(new Location(d.location()), d.name().getCString(), d.priority(), TheoryOperatorType.valueOfInt(d.type()));
    }
    
    public static class TheoryOperatorDefinitionList extends SpanList<TheoryOperatorDefinition, clingo_ast_theory_operator_definition> {

        public TheoryOperatorDefinitionList(Pointer<clingo_ast_theory_operator_definition> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected TheoryOperatorDefinition getItem(Pointer<clingo_ast_theory_operator_definition> p) {
            return convert(p.get());
        }
    
    }
}
