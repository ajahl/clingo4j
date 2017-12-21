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

import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration_type;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration_type.clingo_configuration_type_array;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration_type.clingo_configuration_type_map;
import static org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration_type.clingo_configuration_type_value;

/**
 *
 * @author andrej
 */
public enum ConfigurationType {
    
    VALUE(clingo_configuration_type_value),
    
    ARRAY(clingo_configuration_type_array),
    
    MAP(clingo_configuration_type_map);
    		
    private clingo_configuration_type type;

    private ConfigurationType(clingo_configuration_type type) {
        this.type = type;
    }

    public clingo_configuration_type getType() {
        return type;
    }
    
    public int getValue() {
        return (int) type.value;
    }
    
    public static ConfigurationType createConfigurationType(int value) {
        ConfigurationType result = null;
        ConfigurationType[] values = ConfigurationType.values();
        for (int i=0; i<values.length && result == null; i++) {
            if (values[i].type.value == value) {
                result = values[i];
            }
        }
        return result;
    }
    
}
