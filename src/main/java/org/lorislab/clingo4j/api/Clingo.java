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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.bridj.SizeT;
import org.lorislab.clingo4j.c.api.ClingoLibrary;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_control;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_ground_callback_t;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_model;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_event_callback_t;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_handle;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_mode;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_result.clingo_solve_result_exhausted;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_result.clingo_solve_result_interrupted;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_result.clingo_solve_result_satisfiable;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_solve_result.clingo_solve_result_unsatisfiable;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_statistic;
import org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_callback_t;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_function;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_infimum;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_number;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_string;
import static org.lorislab.clingo4j.c.api.ClingoLibrary.clingo_symbol_type.clingo_symbol_type_supremum;
import org.lorislab.clingo4j.c.api.clingo_location;
import org.lorislab.clingo4j.c.api.clingo_part;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author andrej
 */
public class Clingo implements AutoCloseable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Clingo.class);

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

    private final ClingoLibrary library;

    private final Pointer<Pointer<clingo_control>> control;

    public Clingo() throws ClingoException {
        this(MESSAGE_LIMIT);
    }

    public Clingo(int messageLimit, String... parameters) throws ClingoException {
        library = new ClingoLibrary();

        // create a control object and pass command line arguments
        control = Pointer.allocatePointer(clingo_control.class);
        if (!library.clingo_control_new(null, 0, null, null, messageLimit, control)) {
            throwError("Could not create clingo controller");
        }
    }

    public Clingo(String... parameters) throws ClingoException {
        this(MESSAGE_LIMIT, parameters);
    }

    public String getVersion() throws ClingoException {
        Pointer<Integer> major = Pointer.allocateInt();
        Pointer<Integer> minor = Pointer.allocateInt();
        Pointer<Integer> revision = Pointer.allocateInt();
        library.clingo_version(major, minor, revision);
        return "" + major.getInt() + "." + minor.getInt() + "." + revision.getInt();
    }

    public void getStatistics() {
        Pointer<Pointer<clingo_statistic>> statistics = Pointer.allocatePointer(clingo_statistic.class);;
        
        if (!library.clingo_control_statistics(control.get(), statistics)) {
            throwError("Error reading the clingo statistics!");
        }
        
        Pointer<Long> root = Pointer.allocateLong();
        if (!library.clingo_statistics_root(statistics.get(), root)) {
            throwError("Error reading the clingo statistics root!");
        }
        
        //TODO: implementation
    }
    
    public void add(String name, String program) throws ClingoException {
        add(name, null, program);
    }

    public void add(String name, List<String> parameters, String program) throws ClingoException {

        Pointer<Byte> tmp_name = Pointer.pointerToCString("base");
        Pointer<Byte> tmp_program = Pointer.pointerToCString(program);

        Pointer<Pointer<Byte>> tmp_params = null;
        int tmp_size = 0;
        if (parameters != null && !parameters.isEmpty()) {
            tmp_params = Pointer.pointerToCStrings(parameters.toArray(new String[parameters.size()]));
            tmp_size = parameters.size();
        }

        // add a logic program to the base part
        if (!library.clingo_control_add(control.get(), tmp_name, tmp_params, tmp_size, tmp_program)) {
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

        if (parts != null) {
            partsSize = parts.size();
            clingo_part[] ps = new clingo_part[partsSize];
            for (int i = 0; i < parts.size(); i++) {

                Part part = parts.get(i);

                clingo_part p = new clingo_part();
                p.name(Pointer.pointerToCString(part.getName()));

                List<Symbol> parameters = part.getParameters();
                if (parameters != null && !parameters.isEmpty()) {
                    p.params(createSymbols(parameters));
                    p.size(parameters.size());
                } else {
                    p.params(null);
                    p.size(0);
                }
                ps[i] = p;
            }
            p_parts = Pointer.pointerToArray(ps);
        }

        Pointer<clingo_ground_callback_t> p_ground_callback = null;
        if (callback != null) {
            clingo_ground_callback_t ground_callback = new ClingoLibrary.clingo_ground_callback_t() {
                @Override
                public boolean apply(Pointer<clingo_location> clocation, Pointer<Byte> cname, Pointer<Long> carguments, long carguments_size, Pointer<?> cdata, Pointer<clingo_symbol_callback_t> csymbol_callback, Pointer<?> csymbol_callback_data) {

                    String name = cname.getCString();

                    clingo_location cl = clocation.get();
                    Location loc = new Location(
                            cl.begin_column(),
                            cl.begin_file().getCString(),
                            cl.begin_line(),
                            cl.end_column(),
                            cl.end_file().getCString(),
                            cl.end_line());

                    List<Symbol> symbols = null;
                    if (carguments_size > 0) {
                        symbols = new ArrayList<>();
                        Pointer<Long> iter = carguments;
                        for (int i = 0; i < carguments_size; i++, iter = iter.next()) {
                            symbols.add(loadSymbol(iter.get()));
                        }
                    }

                    boolean result = true;

                    try {
                        callback.groundCallback(loc, name, symbols, (List<Symbol> symbols1) -> {
                            
                            if (symbols1 == null) {
                                return;
                            }
                            
                            Pointer<Long> v_symbols = createSymbols(symbols1);
                            long v_size = symbols1.size();
                            
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
            p_ground_callback = Pointer.allocate(clingo_ground_callback_t.class);
            p_ground_callback.set(ground_callback);
        }

        // ground the base part
        if (!library.clingo_control_ground(control.get(), p_parts, partsSize, p_ground_callback, null)) {
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

        final Pointer<Pointer<clingo_solve_handle>> handle = Pointer.allocatePointer(clingo_solve_handle.class);

        Pointer<clingo_solve_event_callback_t> p_event = null;
        if (handler != null) {
            clingo_solve_event_callback_t event = new clingo_solve_event_callback_t() {
                @Override
                public boolean apply(int type, Pointer<?> event, Pointer<?> data, Pointer<Boolean> goon) {

                    switch (type) {
                        //clingo_solve_event_type_model
                        case 0:
                            Pointer<clingo_model> p_model = (Pointer<clingo_model>) event;
                            Model model = createModel(p_model);
                            boolean tmp = handler.onModel(model);
                            goon.set(tmp);
                            break;
                        //clingo_solve_event_type_finish
                        case 1:
                            Pointer<Integer> p_event = (Pointer<Integer>) event;
                            int eventValue = p_event.get();
                            handler.onFinish(new SolveResult(
                                    (eventValue & ((int) clingo_solve_result_satisfiable.value)) > 0,
                                    (eventValue & ((int) clingo_solve_result_unsatisfiable.value)) != 0,
                                    (eventValue & 3) == 0,
                                    (eventValue & ((int) clingo_solve_result_exhausted.value)) != 0,
                                    (eventValue & ((int) clingo_solve_result_interrupted.value)) != 0
                            ));
                            goon.set(true);
                            return true;
                    }
                    return apply(type, Pointer.getPeer(event), Pointer.getPeer(data), Pointer.getPeer(goon));
                }
            };
            p_event = Pointer.allocate(clingo_solve_event_callback_t.class);
            p_event.set(event);
        }

        // get a solve handle        
        if (!library.clingo_control_solve(control.get(), mode, null, 0, p_event, null, handle)) {
            throwError("Error execute control solve");
        }

        Iterator<Model> iter = new Iterator<Model>() {

            private Model current;

            @Override
            public boolean hasNext() {
                current = getModel();
                // close the solve handle
                if (current == null) {
                    if (!library.clingo_solve_handle_close(handle.get())) {
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

                Pointer<Pointer<clingo_model>> model = Pointer.allocatePointer(clingo_model.class);

                Model result = null;

                if (!library.clingo_solve_handle_resume(handle.get())) {
                    throwError("Error solve handle resume");
                }
                if (!library.clingo_solve_handle_model(handle.get(), model)) {
                    throwError("Error solve handle model");
                }

                if (model.get() != null) {

                    int show = (int) (ClingoLibrary.clingo_show_type.clingo_show_type_shown.value);
                    Pointer<SizeT> atoms_n = Pointer.allocateSizeT();

                    // determine the number of (shown) symbols in the model                
                    if (!library.clingo_model_symbols_size(model.get(), show, atoms_n)) {
                        throwError("Error create symbol size");
                    }

                    // allocate required memory to hold all the symbols
                    Pointer<Long> atoms = Pointer.allocateLongs(atoms_n.getLong());

                    // retrieve the symbols in the model
                    if (!library.clingo_model_symbols(model.get(), show, atoms, atoms_n.getLong())) {
                        throwError("Error create symbol symbols");
                    }

                    List<Symbol> atomsList = new ArrayList<>((int) atoms_n.getLong());
                    result = new Model(atomsList);

                    for (int i = 0; i < atoms_n.getLong(); i++) {
                        Long atom = atoms.get(i);
                        atomsList.add(loadSymbol(atom));
                    }
                }
                return result;
            }
        };
        return iter;
    }

    @Override
    public void close() throws ClingoException {
        if (control != null) {
            library.clingo_control_free(control.get());
        }
    }

    private void throwError(String message) throws ClingoException {
        Pointer<Byte> msg = library.clingo_error_message();
        int error = library.clingo_error_code();
        throw new ClingoException(error, msg.getCString(), message);
    }

    private Model createModel(Pointer<clingo_model> model) {
        Model result = null;
        // TODO: missing implementation
        return result;
    }
    
    private Pointer<Long> createSymbols(List<Symbol> symbols) {
        Pointer<Long> result = null;
        if (symbols != null && !symbols.isEmpty()) {
            
            result = Pointer.allocateLongs(symbols.size());
            Pointer<Long> item = result;
            for (int i=0; i<symbols.size(); i++) {
                createSymbol(symbols.get(i), item);
                item = item.next();
            }
        }
        return result;
    }
    
    private Pointer<Long> createSymbol(Symbol symbol) {
        Pointer<Long> result = Pointer.allocateLong();
        return createSymbol(symbol, result);
    }
    
    private Pointer<Long> createSymbol(Symbol symbol, Pointer<Long> result) {
        switch(symbol.getType()) {
            case NUMBER:
                library.clingo_symbol_create_number(symbol.getNumber(), result);
                break;
            case STRING:
                library.clingo_symbol_create_string(Pointer.pointerToCString(symbol.getString()), result);
                break;
            case FUNCTION:
                Pointer<Byte> name = Pointer.pointerToCString(symbol.getName());
                Pointer<Long> args = null;
                int size = 0;
                if (symbol.getArguments() != null && !symbol.getArguments().isEmpty()) {
                    args = createSymbols(symbol.getArguments());
                    size = symbol.getArguments().size();
                }
                library.clingo_symbol_create_function(name, args, size, symbol.isPositive(), result);
                break;
            case INFIMUM:
                library.clingo_symbol_create_infimum(result);
                break;
            case SUPREMUM:
                library.clingo_symbol_create_supremum(result);
                break;
        }
        
        
        return result;
    }
    
    private Symbol loadSymbol(long atom) {
        SymbolType type = createSymbolType(atom);
        Symbol result = new Symbol(type);

        if (null != type) {
            switch (type) {
                case NUMBER:
                    Pointer<Integer> number = Pointer.allocateInt();
                    if (!library.clingo_symbol_number(atom, number)) {
                        throwError("Error reading the symbol number value!");
                    }
                    result.setNumber(number.get());
                    break;
                case STRING:
                    Pointer<Pointer<Byte>> name_s = Pointer.allocatePointer(Byte.class);
                    if (!library.clingo_symbol_name(atom, name_s)) {
                        throwError("Error reading the symbol name!");
                    }
                    result.setName(name_s.get().getCString());

                    Pointer<Pointer<Byte>> value = Pointer.allocatePointer(Byte.class);
                    if (!library.clingo_symbol_string(atom, value)) {
                        throwError("Error reading the symbol string!");
                    }
                    result.setString(value.get().getCString());
                    break;
                case FUNCTION:
                    Pointer<Pointer<Byte>> name = Pointer.allocatePointer(Byte.class);
                    if (!library.clingo_symbol_name(atom, name)) {
                        throwError("Error reading the symbol name!");
                    }
                    result.setName(name.get().getCString());

                    Pointer<Boolean> negative = Pointer.allocateBoolean();
                    if (!library.clingo_symbol_is_negative(atom, negative)) {
                        throwError("Error reading the symbol negative!");
                    }
                    result.setNegative(negative.get());

                    Pointer<Boolean> positive = Pointer.allocateBoolean();
                    if (!library.clingo_symbol_is_positive(atom, positive)) {
                        throwError("Error reading the symbol positive!");
                    }
                    result.setPositive(positive.get());

                    Pointer<Pointer<Long>> args = Pointer.allocatePointer(Long.class);
                    Pointer<SizeT> args_size = Pointer.allocateSizeT();
                    if (!library.clingo_symbol_arguments(atom, args, args_size)) {
                        throwError("Error reading the symbol arguments!");
                    }
                    for (int i = 0; i < args_size.getLong(); i++) {
                        Long arg = args.get().get(i);
                        Symbol s = loadSymbol(arg);
                        result.addArgument(s);
                    }
                    break;
                case INFIMUM:
                    break;
                case SUPREMUM:
                    break;
                default:
                    break;
            }
        }

        return result;
    }

    private SymbolType createSymbolType(long symbol) {
        SymbolType result = null;
        int type = library.clingo_symbol_type(symbol);

        if (type == clingo_symbol_type_number.value) {
            result = SymbolType.NUMBER;
        } else if (type == clingo_symbol_type_function.value) {
            result = SymbolType.FUNCTION;
        } else if (type == clingo_symbol_type_infimum.value) {
            result = SymbolType.INFIMUM;
        } else if (type == clingo_symbol_type_string.value) {
            result = SymbolType.STRING;
        } else if (type == clingo_symbol_type_supremum.value) {
            result = SymbolType.SUPREMUM;
        }
        return result;
    }

}
