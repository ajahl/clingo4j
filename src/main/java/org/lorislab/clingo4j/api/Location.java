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

import org.bridj.Pointer;
import org.lorislab.clingo4j.c.api.clingo_location;

/**
 *
 * @author andrej
 */
public class Location {
    
    private Pointer<clingo_location> pointer;

    public Location(Pointer<clingo_location> pointer) {
        this.pointer = pointer;
    }
    
    public Pointer<clingo_location> getPointer() {
        return pointer;
    }
    
    public long getBeginColumn() {
        return pointer.get().begin_column();
    }
    
    public String getBeginFile() {
        return pointer.get().begin_file().getCString();
    }
    
    public long getBeginLine() {
        return pointer.get().begin_line();
    }
    
    public long getEndColumn() {
        return pointer.get().end_column();
    }
    
    public String getEndFile() {
        return pointer.get().end_file().getCString();
    }
    
    public long getEndLine() {
        return pointer.get().end_line();
    }
    
}
