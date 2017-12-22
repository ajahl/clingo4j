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
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_statistic;

/**
 *
 * @author andrej
 */
public class Statistics {

    protected final Pointer<clingo_statistic> pointer;

    protected final long key;

    public Statistics(Pointer<clingo_statistic> pointer, long key) {
        this.pointer = pointer;
        this.key = key;
    }

    public StatisticsList toList() {
        return new StatisticsList(pointer, key);
    }
    
    public StatisticsMap toMap() {
        return new StatisticsMap(pointer, key);
    }

    public double getValue() throws ClingoException {
        Pointer<Double> value = Pointer.allocateDouble();
        handleError(LIB.clingo_statistics_value_get(pointer, key, value), "Error reading the statistic value!");
        return value.getDouble();
    }

    public StatisticsType getType() throws ClingoException {
        Pointer<Integer> value = Pointer.allocateInt();
        handleError(LIB.clingo_statistics_type(pointer, key, value), "Error reading the statistric type!");
        return StatisticsType.createStatisticsType(value.getInt());
    }

    public Pointer<clingo_statistic> getPointer() {
        return pointer;
    }

}
