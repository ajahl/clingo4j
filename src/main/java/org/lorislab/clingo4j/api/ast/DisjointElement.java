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

import java.util.List;
import org.lorislab.clingo4j.api.Location;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class DisjointElement {

    private Location location;
    private List<Term> tuple;
    private CSPSum term;
    private List<Literal> condition;

    public List<Literal> getCondition() {
        return condition;
    }

    public Location getLocation() {
        return location;
    }

    public CSPSum getTerm() {
        return term;
    }

    public List<Term> getTuple() {
        return tuple;
    }

    @Override
    public String toString() {
        return ClingoUtil.print(tuple, "", ",", "", false) + " : " + term + " : " + ClingoUtil.print(condition, "", ",", "", false);
    }

    
}
