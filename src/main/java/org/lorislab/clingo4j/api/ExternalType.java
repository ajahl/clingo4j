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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_external_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_external_type.clingo_external_type_false;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_external_type.clingo_external_type_free;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_external_type.clingo_external_type_release;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_external_type.clingo_external_type_true;

/**
 *
 * @author andrej
 */
public enum ExternalType {
    
    FREE(clingo_external_type_free, "Free"),
    TRUE(clingo_external_type_true, "True"),
    FALSE(clingo_external_type_false, "False"),
    RELEASE(clingo_external_type_release, "Release");
    
    private final clingo_external_type type;
    
    private final String string;

    private ExternalType(clingo_external_type type, String string) {
        this.type = type;
        this.string = string;
    }

    public clingo_external_type getType() {
        return type;
    }

    public int getValue() {
        return (int) type.value;
    }

    @Override
    public String toString() {
        return string;
    }
    
    public static ExternalType createExternalType(int value) {
        ExternalType r = null;
        ExternalType[] values = ExternalType.values();
        for (int i=0; i<values.length && r == null; i++) {
            if (values[i].getValue() == value) {
                r = values[i];
            }
        }
        return r;
    }
}