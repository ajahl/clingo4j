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
package org.lorislab.clingo4j.api2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.lorislab.clingo4j.c.api.ClingoLibrary;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_control;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_model;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_event_callback_t;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_handle;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_mode;
import org.lorislab.clingo4j.c.api.clingo_location;
import org.lorislab.clingo4j.c.api.clingo_part;

/**
 *
 * @author andrej
 */
public class Clingo implements AutoCloseable {

    public static final ClingoLibrary LIB = new ClingoLibrary();

    public static final int MESSAGE_LIMIT = 20;

    public static final void init() {
        init(null);
    }

    public static final void init(String libraryPath) {

        // add the native library directory
        if (libraryPath != null) {
            BridJ.addLibraryPath(libraryPath);
        }

        // initialize the native library
        BridJ.register(ClingoLibrary.class);

    }

    private final Pointer<Pointer<clingo_control>> control;

    public Clingo() throws ClingoException {
        this(MESSAGE_LIMIT);
    }

    public Clingo(int messageLimit, String... parameters) throws ClingoException {

        // create a control object and pass command line arguments
        control = Pointer.allocatePointer(clingo_control.class);
        if (!LIB.clingo_control_new(null, 0, null, null, messageLimit, control)) {
            throwError("Could not create clingo controller");
        }
    }

    public Clingo(String... parameters) throws ClingoException {
        this(MESSAGE_LIMIT, parameters);
    }

    @Override
    public void close() throws org.lorislab.clingo4j.api.ClingoException {
        if (control != null) {
            LIB.clingo_control_free(control.get());
        }
    }
    
    public String getVersion() throws org.lorislab.clingo4j.api.ClingoException {
        Pointer<Integer> major = Pointer.allocateInt();
        Pointer<Integer> minor = Pointer.allocateInt();
        Pointer<Integer> revision = Pointer.allocateInt();
        LIB.clingo_version(major, minor, revision);
        return "" + major.getInt() + "." + minor.getInt() + "." + revision.getInt();
    }
    
    public void add(String name, String program) throws ClingoException {
        add(name, null, program);
    }
    
    public void add(String name, List<String> parameters, String program) throws ClingoException {

        Pointer<Byte> tmp_name = Pointer.pointerToCString(name);
        Pointer<Byte> tmp_program = Pointer.pointerToCString(program);

        Pointer<Pointer<Byte>> tmp_params = null;
        int tmp_size = 0;
        if (parameters != null && !parameters.isEmpty()) {
            tmp_params = Pointer.pointerToCStrings(parameters.toArray(new String[parameters.size()]));
            tmp_size = parameters.size();
        }

        // add a logic program to the base part
        if (!LIB.clingo_control_add(control.get(), tmp_name, tmp_params, tmp_size, tmp_program)) {
            throwError("Error add the program to controller");
        }
    }

    public void ground(String name) throws ClingoException {
        ground(Arrays.asList(new Part(name)), null);
    }

    public void ground(String name, GroundCallback callback) throws ClingoException {
        ground(Arrays.asList(new Part(name)), callback);
    }

    public void ground(List<Part> parts, GroundCallback callback) throws ClingoException {

        Pointer<clingo_part> p_parts = null;
        int partsSize = 0;

        if (parts != null && !parts.isEmpty()) {
            partsSize = parts.size();
            clingo_part[] ps = new clingo_part[partsSize];
            for (int i = 0; i < partsSize; i++) {
                ps[i] = parts.get(i).getPart();
            }
            p_parts = Pointer.pointerToArray(ps);
        }

        Pointer<ClingoLibrary.clingo_ground_callback_t> p_ground_callback = null;
        if (callback != null) {
            ClingoLibrary.clingo_ground_callback_t ground_callback = new ClingoLibrary.clingo_ground_callback_t() {
                @Override
                public boolean apply(Pointer<clingo_location> clocation, Pointer<Byte> cname, Pointer<Long> carguments, long carguments_size, Pointer<?> cdata, Pointer<ClingoLibrary.clingo_symbol_callback_t> csymbol_callback, Pointer<?> csymbol_callback_data) {

                    String name = cname.getCString();
                    Location loc = new Location(clocation);
                    List<Symbol> symbols = createListOfSymbols(carguments, carguments_size);

                    boolean result = true;
                    try {
                        callback.groundCallback(loc, name, symbols, (List<Symbol> symbols1) -> {

                            if (symbols1 == null) {
                                return;
                            }

                            if (symbols1.isEmpty()) {
                                return;
                            }

                            long v_size = symbols1.size();
                            Pointer<Long> v_symbols = createPointerToSymbols(symbols1);

                            if (!(csymbol_callback.get().apply(v_symbols, v_size, null))) {
                                throw new ClingoException();
                            }
                        });
                    } catch (ClingoException ex) {
                        result = false;
                    }
                    return result;
                }

            };
            p_ground_callback = Pointer.allocate(ClingoLibrary.clingo_ground_callback_t.class);
            p_ground_callback.set(ground_callback);
        }

        // ground the base part
        if (!LIB.clingo_control_ground(control.get(), p_parts, partsSize, p_ground_callback, null)) {
            throwError("Error ground the program");
        }

    }

    public Iterator<Model> solve() throws ClingoException {
        return solve(null, false, true);
    }
    
    public Iterator<Model> solve(SolveEventHandler handler, boolean asynchronous, boolean yield) throws ClingoException {

        int mode = 0;
        if (asynchronous) {
            mode |= (int) clingo_solve_mode.clingo_solve_mode_async.value;
        }
        if (yield) {
            mode |= (int) clingo_solve_mode.clingo_solve_mode_yield.value;
        }

        final Pointer<Pointer<clingo_solve_handle>> handle = Pointer.allocatePointer(ClingoLibrary.clingo_solve_handle.class);

        Pointer<clingo_solve_event_callback_t> p_event = null;
        if (handler != null) {
            clingo_solve_event_callback_t event = new clingo_solve_event_callback_t() {
                @Override
                public boolean apply(int type, Pointer<?> event, Pointer<?> data, Pointer<Boolean> goon) {

                    switch (type) {
                        //clingo_solve_event_type_model
                        case 0:
                            Model model = new Model((Pointer<clingo_model>) event);
                            boolean tmp = handler.onModel(model);
                            goon.set(tmp);
                            break;
                        //clingo_solve_event_type_finish
                        case 1:
                            Pointer<Integer> p_event = (Pointer<Integer>) event;
                            handler.onFinish(new SolveResult(p_event.get()));
                            goon.set(true);
                            return true;
                    }
                    return apply(type, Pointer.getPeer(event), Pointer.getPeer(data), Pointer.getPeer(goon));
                }
            };
            p_event = Pointer.allocate(ClingoLibrary.clingo_solve_event_callback_t.class);
            p_event.set(event);
        }

        // get a solve handle        
        if (!LIB.clingo_control_solve(control.get(), mode, null, 0, p_event, null, handle)) {
            throwError("Error execute control solve");
        }

        Iterator<Model> iter = new Iterator<Model>() {

            private Model current;

            @Override
            public boolean hasNext() {
                current = getModel();
                // close the solve handle
                if (current == null) {
                    if (!LIB.clingo_solve_handle_close(handle.get())) {
                        throwError("Error close the handle.");
                    }
                }
                return current != null;
            }

            @Override
            public Model next() {
                if (current == null) {
                    if (!hasNext()) {
                        throw new NoSuchElementException("No more models in the solution.");
                    }
                }
                return current;
            }

            private Model getModel() {

                Pointer<Pointer<ClingoLibrary.clingo_model>> model = Pointer.allocatePointer(ClingoLibrary.clingo_model.class);

                if (!LIB.clingo_solve_handle_resume(handle.get())) {
                    throwError("Error solve handle resume");
                }
                if (!LIB.clingo_solve_handle_model(handle.get(), model)) {
                    throwError("Error solve handle model");
                }

                if (model.get() != null) {
                    return new Model(model.get());
                }
                return null;
            }
        };
        return iter;
    }
    
    public static void throwError(String message) throws ClingoException {
        Pointer<Byte> msg = LIB.clingo_error_message();
        int error = LIB.clingo_error_code();
        throw new ClingoException(error, msg.getCString(), message);
    }

    public static Symbol createId(String name, boolean positive) {
        Pointer<Long> pointer = Pointer.allocateLong();
        if (!LIB.clingo_symbol_create_id(Pointer.pointerToCString(name), positive, pointer)) {
            throwError("Error creating the ID!"); 
        }
        return new Symbol(pointer);
    }

    public static Symbol createString(String string) {
        Pointer<Long> pointer = Pointer.allocateLong();
        if (!LIB.clingo_symbol_create_string(Pointer.pointerToCString(string), pointer)) {
            throwError("Error creating the string!"); 
        }
        return new Symbol(pointer);
    }

    public static Symbol createNumber(int number) {
        Pointer<Long> pointer = Pointer.allocateLong();
        LIB.clingo_symbol_create_number(number, pointer);
        return new Symbol(pointer);
    }

    public static Symbol createInfimum() {
        Pointer<Long> pointer = Pointer.allocateLong();
        LIB.clingo_symbol_create_infimum(pointer);
        return new Symbol(pointer);
    }

    public static Symbol createSupremum() {
        Pointer<Long> pointer = Pointer.allocateLong();
        LIB.clingo_symbol_create_supremum(pointer);
        return new Symbol(pointer);
    }
    
    public static Symbol createFunction(String name, List<Symbol> symbols, boolean positive) {
        Pointer<Long> pointer = Pointer.allocateLong();
        
        int size = 0;
        Pointer<Long> arguments = createPointerToSymbols(symbols);
        if (symbols != null && !symbols.isEmpty()) {
            size = symbols.size();
        }
        
        if (!LIB.clingo_symbol_create_function(Pointer.pointerToCString(name), arguments, size, positive, pointer)) {
            throwError("Error creating the function!");            
        }
        
        return new Symbol(pointer);
    }

    public static Pointer<Long> createPointerToSymbols(List<Symbol> symbols) {
        Pointer<Long> result = null;
        if (symbols != null && !symbols.isEmpty()) {
            int size = symbols.size();
            Pointer<Long> v_symbols = Pointer.allocateLongs(size);
            Pointer<Long> item = v_symbols;
            for (int i = 0; i < size; i++, item = item.next()) {
                item.set(symbols.get(i).getPointer().get());
            }
        }
        return result;
    }

    public static List<Symbol> createListOfSymbols(final Pointer<Long> symbols, final long size) {
        List<Symbol> result = null;
        if (0 < size) {
            result = new ArrayList<>((int) size);
            Pointer<Long> iter = symbols;
            for (int i = 0; i < size; i++, iter = iter.next()) {
                result.add(new Symbol(iter));
            }
        }
        return result;
    }
}
