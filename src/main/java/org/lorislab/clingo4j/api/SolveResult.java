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

/**
 *
 * @author andrej
 */
public class SolveResult {
    
    private final boolean satisfiable;
    
    private final boolean unsatisfiable;
    
    private final boolean unknown;
    
    private final boolean exhausted;
    
    private final boolean interrupted;

    public SolveResult(boolean satisfiable, boolean unsatisfiable, boolean unknown, boolean exhausted, boolean interrupted) {
        this.satisfiable = satisfiable;
        this.unsatisfiable = unsatisfiable;
        this.unknown = unknown;
        this.exhausted = exhausted;
        this.interrupted = interrupted;
    }

    public boolean isExhausted() {
        return exhausted;
    }

    public boolean isInterrupted() {
        return interrupted;
    }

    public boolean isSatisfiable() {
        return satisfiable;
    }

    public boolean isUnknown() {
        return unknown;
    }

    public boolean isUnsatisfiable() {
        return unsatisfiable;
    }
    
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isSatisfiable()) {
            sb.append("SATISFIABLE");
            if (isExhausted()) {
                sb.append('+');
            }
        } else if (isUnsatisfiable()) {
            sb.append("UNSATISFIABLE");
        } else {
            sb.append("UNKNOWN");
        }
        if (isInterrupted()) {
            sb.append("/INTERRUPTED");
        }
        return sb.toString();
    }
 
}
