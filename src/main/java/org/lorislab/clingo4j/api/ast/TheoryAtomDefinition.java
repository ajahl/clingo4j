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
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.api.SpanList;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_atom_definition;

/**
 *
 * @author andrej
 */
public class TheoryAtomDefinition {

    private final Location location;
    private final TheoryAtomDefinitionType type;
    private final String name;
    private final int arity;
    private final String elements;
    private final Optional<TheoryGuardDefinition> guard;    

    public TheoryAtomDefinition(Location location, TheoryAtomDefinitionType type, String name, int arity, String elements, Optional<TheoryGuardDefinition> guard) {
        this.location = location;
        this.type = type;
        this.name = name;
        this.arity = arity;
        this.elements = elements;
        this.guard = guard;
    }

    public int getArity() {
        return arity;
    }

    public String getElements() {
        return elements;
    }

    public Optional<TheoryGuardDefinition> getGuard() {
        return guard;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public TheoryAtomDefinitionType getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("&").append(name).append("/").append(arity).append(" : ").append(elements);
        if (guard.isPresent()) {
            sb.append(", ").append(guard.get());
        }
        sb.append(", ").append(type);
        return sb.toString();
    }
    
    public static TheoryAtomDefinition convert(clingo_ast_theory_atom_definition d) {
        return new TheoryAtomDefinition(new Location(d.location()), TheoryAtomDefinitionType.valueOfInt(d.type()), d.name().getCString(), d.arity(), d.elements().getCString(), TheoryGuardDefinition.convert(d.guard()));
    }
    
    public static class TheoryAtomDefinitionList extends SpanList<TheoryAtomDefinition, clingo_ast_theory_atom_definition> {

        public TheoryAtomDefinitionList(Pointer<clingo_ast_theory_atom_definition> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected TheoryAtomDefinition getItem(Pointer<clingo_ast_theory_atom_definition> p) {
            return convert(p.get());
        }
        
    }
}
