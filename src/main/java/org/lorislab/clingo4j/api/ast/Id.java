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
import org.lorislab.clingo4j.api.c.clingo_ast_id;

/**
 *
 * @author andrej
 */
public class Id {

    private final Location location;

    private final String id;

    public Id(Location location, String id) {
        this.location = location;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public String toString() {
        return id;
    }
    
    public static Id convert(clingo_ast_id d) {
        return new Id(new Location(d.location()), d.id().getCString());
    }
    
    public static class IdList extends SpanList<Id, clingo_ast_id> {

        public IdList(Pointer<clingo_ast_id> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected Id getItem(Pointer<clingo_ast_id> p) {
            return convert(p.get());
        }
        
    }
}
