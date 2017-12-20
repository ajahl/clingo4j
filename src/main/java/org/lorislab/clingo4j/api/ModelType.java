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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_model_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_model_type.clingo_model_type_brave_consequences;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_model_type.clingo_model_type_cautious_consequences;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_model_type.clingo_model_type_stable_model;

/**
 *
 * @author andrej
 */
public enum ModelType {

    STABLE_MODEL(clingo_model_type_stable_model),
    BRAVE_CONSEQUENCES(clingo_model_type_brave_consequences),
    CAUTIOUS_CONSEQUENCES(clingo_model_type_cautious_consequences);

    private clingo_model_type type;

    private ModelType(clingo_model_type type) {
        this.type = type;
    }

    public clingo_model_type getType() {
        return type;
    }

    public long getValue() {
        return type.value;
    }

    public static ModelType createModelType(long value) {
        ModelType result = null;
        ModelType[] types = ModelType.values();
        for (int i = 0; i < types.length && result == null; i++) {
            if (types[i].getValue() == value) {
                result = types[i];
            }
        }
        return result;
    }

}
