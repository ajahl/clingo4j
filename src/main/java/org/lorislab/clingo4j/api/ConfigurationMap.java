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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.enums.ConfigurationType;

/**
 *
 * @author andrej
 */
public class ConfigurationMap extends Configuration implements Iterable<Configuration> {

    private static final Set<String> TYPE_HACK = new HashSet<String>() {
        {
            add("solve_limit");
            add("parallel_mode");
            add("global_restarts");
            add("distribute");

            add("integrate");
            add("enum_mode");
            add("project");
            add("models");
            add("opt_mode");
            add("trans_ext");
            add("opt_strategy");
            add("opt_usc_shrink");
            add("opt_heuristic");
            add("lookahead");
            add("heuristic");
            add("score_res");
            add("score_other");
            add("sign_def");
            add("vsids_progress");
            add("dom_mod");
            add("save_progress");
            add("init_watches");
            add("update_mode");
            add("no_lookback");
            add("forget_on_step");
            add("strengthen");
            add("update_lbd");
            add("contraction");
            add("loops");
            add("partial_check");
            add("sign_def_disj");
            add("rand_prob");
            add("restarts");
            add("reset_restarts");
            add("counter_restarts");
            add("block_restarts");
            add("shuffle");
            add("deletion");
            add("del_grow");
            add("del_cfl");
            add("del_init");
            add("del_max");
            add("del_glue");
            add("configuration");
            add("share");
            add("sat_prepro");
            add("parse_ext");
            add("parse_maxsat");
        }
    };

    public ConfigurationMap(Pointer<clingo_configuration> pointer, int key) {
        super(pointer, key);
    }

    public int size() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_configuration_map_size(pointer, key, size), "Error reading the configuration map size!");
        return size.getInt();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public String getKey(int index) {
        Pointer<Pointer<Byte>> name = Pointer.allocatePointer(Byte.class);
        handleRuntimeError(LIB.clingo_configuration_map_subkey_name(pointer, key, index, name), "Error reading the configuration key name!");
        return name.get().getCString();
    }

    public Configuration get(String name) {
        Pointer<Integer> subkey = Pointer.allocateInt();
        handleRuntimeError(LIB.clingo_configuration_map_at(pointer, key, Pointer.pointerToCString(name), subkey), "Error reading the configuration item in map by" + name + " key!");
        return new Configuration(pointer, subkey.getInt());
    }

    public Configuration get(int index) {
        String name = getKey(index);
        return get(name);
    }

    public Iterator<String> keyIterator() {

        final int size = size();

        return new Iterator<String>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public String next() {
                if (index < size) {
                    String result = getKey(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };
    }

    @Override
    public Iterator<Configuration> iterator() {

        final int size = size();

        return new Iterator<Configuration>() {

            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public Configuration next() {
                if (index < size) {
                    Configuration result = get(index);
                    index = index + 1;
                    return result;
                }
                return null;
            }
        };
    }

    @Override
    public String toDescription() {
        StringBuilder sb = new StringBuilder();
        try {
            int size = size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    sb.append("\n");
                }
                String name = getKey(i);
                Configuration c = get(name);
                ConfigurationType tt = c.getType();
                if (TYPE_HACK.contains(name)) {
                    tt = ConfigurationType.MAP;
                }
                sb.append(name).append(" - ").append(c.getDescription());
                if (tt == ConfigurationType.MAP) {
                    sb.append("\n");
                    sb.append(c.toDescription());
                }
            }
        } catch (ClingoException ex) {
            handleRuntimeError(ex);
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append("{");
            int size = size();
            for (int i = 0; i < size; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                String name = getKey(i);
                Configuration c = get(name);
                ConfigurationType tt = c.getType();
                if (TYPE_HACK.contains(name)) {
                    tt = ConfigurationType.MAP;
                }
                sb.append(name).append(":").append(toString(c, tt));
            }
            sb.append("}");
        } catch (ClingoException ex) {
            handleRuntimeError(ex);
        }
        return sb.toString();
    }
}
