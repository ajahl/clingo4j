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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagator_check_mode;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagator_check_mode.clingo_propagator_check_mode_fixpoint;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagator_check_mode.clingo_propagator_check_mode_none;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagator_check_mode.clingo_propagator_check_mode_total;

/**
 *
 * @author andrej
 */
public enum PropagatorCheckMode {

    NONE(clingo_propagator_check_mode_none),
    TOTAL(clingo_propagator_check_mode_total),
    PARTIAL(clingo_propagator_check_mode_fixpoint);

    private clingo_propagator_check_mode mode;

    private PropagatorCheckMode(clingo_propagator_check_mode mode) {
        this.mode = mode;
    }

    public clingo_propagator_check_mode getMode() {
        return mode;
    }

    public int getValue() {
        return (int) mode.value;
    }
    
    public static PropagatorCheckMode createPropagatorCheckMode(int value) {
        PropagatorCheckMode result = null;
        PropagatorCheckMode[] values = PropagatorCheckMode.values();
        for (int i=0; i<values.length && result == null; i++) {
            if (values[i].getValue() == value) {
                result = values[i];
            }
        }
        return result;
    }
}
