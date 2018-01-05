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

import org.lorislab.clingo4j.api.enums.ConfigurationType;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import org.lorislab.clingo4j.util.PointerObject;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author andrej
 */
public class Configuration extends PointerObject<clingo_configuration> {

    protected final int key;

    public Configuration(Pointer<clingo_configuration> pointer, int key) {
        super(pointer);
        this.key = key;
    }

    public ConfigurationMap toMap() {
        return new ConfigurationMap(pointer, key);
    }

    public ConfigurationList toList() {
        return new ConfigurationList(pointer, key);
    }

    public ConfigurationType getType() throws ClingoException {
        Pointer<Integer> tmp = Pointer.allocateInt();
        handleError(LIB.clingo_configuration_type(pointer, key, tmp), "Error reading the configuration type!");
        if (tmp.getInt() == 3) {
            return ConfigurationType.VALUE;
        }
        if (tmp.getInt() == 5) {
            return ConfigurationType.VALUE;
        }
        if (tmp.getInt() == 6) {
            return ConfigurationType.MAP;
        }
        return EnumValue.valueOfInt(ConfigurationType.class, tmp.getInt());
    }

    public boolean isAssigned() throws ClingoException {
        Pointer<Boolean> result = Pointer.allocateBoolean();
        handleError(LIB.clingo_configuration_value_is_assigned(pointer, key, result), "Error reading the configureation assigned.");
        return result.getBoolean();
    }

    public String getValue() throws ClingoException {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleError(LIB.clingo_configuration_value_get_size(pointer, key, size), "Error reading the configuration value size!");
        Pointer<Byte> value = Pointer.allocateByte();
        handleError(LIB.clingo_configuration_value_get(pointer, key, value, size.getInt()), "Error reading the configuration value!");
        return value.getCString();
    }

    public void setValue(String value) throws ClingoException {
        handleError(LIB.clingo_configuration_value_set(pointer, key, Pointer.pointerToCString(value)), "Error settings the configuration value " + value + "!");
    }

    public String getDescription() throws ClingoException {
        Pointer<Pointer<Byte>> description = Pointer.allocatePointer(Byte.class);
        handleError(LIB.clingo_configuration_description(pointer, key, description), "Error reading the configuration description!");
        return description.get().getCString();
    }

    public String toDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(description(this, null));
        return sb.toString();        
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(toString(this, null));
        return sb.toString();
    }

    protected static String description(Configuration conf, ConfigurationType type) {
        StringBuilder sb = new StringBuilder();
        try {
            if (type == null) {
                type = conf.getType();
            }
            if (null != type) switch (type) {
                case MAP:
                    sb.append(conf.toMap().toDescription());
                    break;
                case ARRAY:
                    sb.append(conf.toList().toDescription());
                    break;
                case VALUE:
                    sb.append(conf.getDescription());
                    break;
                default:
                    break;
            }
        } catch (ClingoException ex) {
            Clingo.handleRuntimeError(ex);
        }
        return sb.toString();
    }
    
    protected static String toString(Configuration conf, ConfigurationType type) {
        StringBuilder sb = new StringBuilder();
        try {
            if (type == null) {
                type = conf.getType();
            }
            switch (type) {
                case VALUE:
                    if (conf.isAssigned()) {
                        sb.append(conf.getValue());
                    } else {
                        sb.append("null");
                    }
                    break;
                case ARRAY:
                    sb.append(conf.toList());
                    break;
                case MAP:
                    sb.append(conf.toMap());
                    break;
            }
        } catch (ClingoException ex) {
            Clingo.handleRuntimeError(ex);
        }
        return sb.toString();
    }
}
