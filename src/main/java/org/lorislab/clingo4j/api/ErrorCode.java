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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error.clingo_error_bad_alloc;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error.clingo_error_logic;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error.clingo_error_runtime;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_error.clingo_error_unknown;

/**
 *
 * @author andrej
 */
public enum ErrorCode {

    RUNTIME(clingo_error_runtime),
    LOGIC(clingo_error_logic),
    BAD_ALLOC(clingo_error_bad_alloc),
    UNKNOWN(clingo_error_unknown);
    
    private final clingo_error code;

    private ErrorCode(clingo_error code) {
        this.code = code;
    }

    public clingo_error getCode() {
        return code;
    }

    public int getValue() {
        return (int) code.value;
    }

    @Override
    public String toString() {
        return this.name() + "[" + getValue() + "]";
    }
    
    
    public static final ErrorCode createErrorCode(int value) {
        ErrorCode r = null;
        ErrorCode[] values = ErrorCode.values();
        for (int i=0; i<values.length && r == null; i++) {
            if (values[i].getValue() == value) {
                r = values[i];
            }
        }
        return r;
    }
    
}
