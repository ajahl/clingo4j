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

import java.util.List;
import org.bridj.Pointer;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.ast.Literal;
import org.lorislab.clingo4j.api.ast.Literal.LiteralIntegerList;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_backend;
import org.lorislab.clingo4j.api.c.clingo_weighted_literal;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Backend {

    private final Pointer<clingo_backend> pointer;

    public Backend(Pointer<clingo_backend> pointer) {
        this.pointer = pointer;
    }

    public Pointer<clingo_backend> getPointer() {
        return pointer;
    }

    public void rule(boolean choice, List<Integer> head, List<Integer> body) throws ClingoException {
        LiteralIntegerList tmp = Literal.toLiteralList(body);
        Pointer<Integer> h = ClingoUtil.createArray(head, Integer.class);
        handleError(LIB.clingo_backend_rule(pointer, choice, h, head.size(), tmp.getPointer(), tmp.size()), "Error rule to the backend!");
    }

    public void weightRule(boolean choice, List<Integer> head, int lower, List<WeightedLiteral> body) throws ClingoException {
        Pointer<Integer> h = ClingoUtil.createArray(head, Integer.class);
        Pointer<clingo_weighted_literal> t = WeightedLiteral.toArray(body);
        handleError(LIB.clingo_backend_weight_rule(pointer, choice, h, head.size(), lower, t, body.size()), "Error weight rule to the backend!");
    }

    public void minimize(int prio, List<WeightedLiteral> body) throws ClingoException {
        Pointer<clingo_weighted_literal> t = WeightedLiteral.toArray(body);
        handleError(LIB.clingo_backend_minimize(pointer, prio, t, body.size()), "Error minimize the backend!");
    }

    public void project(List<Integer> atoms) throws ClingoException {
        Pointer<Integer> a = ClingoUtil.createArray(atoms, Integer.class);
        handleError(LIB.clingo_backend_project(pointer, a, atoms.size()), "Error project to the backend!");
    }

    public void external(int atom, ExternalType type) throws ClingoException {
        handleError(LIB.clingo_backend_external(pointer, atom, type.getInt()), "Error extarnal backend!");
    }

    public void assume(List<Integer> lits) throws ClingoException {
        LiteralIntegerList tmp = Literal.toLiteralList(lits);
        handleError(LIB.clingo_backend_assume(pointer, tmp.getPointer(), tmp.size()), "Error assume to the backend!");
    }

    public void heuristic(int atom, HeuristicType type, int bias, int priority, List<Integer> condition) throws ClingoException {
        LiteralIntegerList tmp = Literal.toLiteralList(condition);
        handleError(LIB.clingo_backend_heuristic(pointer, atom, type.getInt(), bias, priority, tmp.getPointer(), tmp.size()), "Error heuristic to the backend!");
    }

    public void acycEdge(int node_u, int node_v, List<Integer> condition) throws ClingoException {
        LiteralIntegerList tmp = Literal.toLiteralList(condition);
        handleError(LIB.clingo_backend_acyc_edge(pointer, node_u, node_v, tmp.getPointer(), tmp.size()), "Error acyc edge on the backend!");
    }

    public int addAtom() throws ClingoException {
        Pointer<Integer> ret = Pointer.allocateInt();
        handleError(LIB.clingo_backend_add_atom(pointer, ret), "Error add the atom to the backend!");
        return ret.getInt();
    }

}
