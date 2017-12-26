/*
 * Copyright 2017 andrej.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"){}
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

/**
 *
 * @author andrej
 */
public class GroundProgramObserver {

    public void initProgram(boolean incremental) {
    }

    public void beginStep() {
    }

    public void endStep() {
    }

    public void rule(boolean choice, List<Integer> head, List<Integer> body) {
    }

    public void weightRule(boolean choice, List<Integer> head, int lower_bound, List<WeightedLiteral> body) {
    }

    public void minimize(int priority, List<WeightedLiteral> literals) {
    }

    public void project(List<Integer> atoms) {
    }

    public void outputAtom(Symbol symbol, int atom) {
    }

    public void outputTerm(Symbol symbol, List<Integer> condition) {
    }

    public void outputCsp(Symbol symbol, int value, List<Integer> condition) {
    }

    public void external(int atom, ExternalType type) {
    }

    public void assume(List<Integer> literals) {
    }

    public void heuristic(int atom, HeuristicType type, int bias, int priority, List<Integer> condition) {
    }

    public void acycEdge(int nodeU, int nodeV, List<Integer> condition) {
    }

    public void theoryTermNumber(int termId, int number) {
    }

    public void theoryTermString(int termId, String name) {
    }

    public void theoryTermCompound(int termId, int nameIdOrType, List<Integer> arguments) {
    }

    public void theoryElement(int elementId, List<Integer> terms, List<Integer> condition) {
    }

    public void theoryAtom(int atomIdOrZero, int termId, List<Integer> elements) {
    }

    public void theoryAtomWithGuard(int atomIdOrZero, int termId, List<Integer> elements, int operatorId, int rightHandSideId) {
    }
}
