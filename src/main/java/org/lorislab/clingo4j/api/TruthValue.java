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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_truth_value;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_truth_value.clingo_truth_value_false;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_truth_value.clingo_truth_value_free;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_truth_value.clingo_truth_value_true;

/**
 *
 * @author andrej
 */
public enum TruthValue {

    FREE(clingo_truth_value_free,"Free"),
    TRUE(clingo_truth_value_true,"True"),
    FALSE(clingo_truth_value_false,"False");

    private clingo_truth_value type;

    private String string;
    
    private TruthValue(clingo_truth_value type, String string) {
        this.type = type;
        this.string = string;
    }

    public clingo_truth_value getType() {
        return type;
    }

    public int getValue() {
        return (int) type.value;
    }

    @Override
    public String toString() {
        return string;
    }
   
}
