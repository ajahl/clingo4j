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

import org.lorislab.clingo4j.api.ast.StatementCallback;
import java.util.Arrays;
import java.util.List;
import org.bridj.BridJ;
import org.bridj.Pointer;
import org.lorislab.clingo4j.api.Symbol.SymbolList;
import org.lorislab.clingo4j.api.WeightedLiteral.WeightedLiteralList;
import org.lorislab.clingo4j.api.ast.Literal;
import org.lorislab.clingo4j.api.ast.Literal.LiteralIntegerList;
import org.lorislab.clingo4j.api.c.ClingoLibrary;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_ast_callback_t;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_backend;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_configuration;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_control;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_logger_t;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_model;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_program_builder;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagate_control;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_propagate_init;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_event_callback_t;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_handle;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_solve_mode;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_statistic;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_symbolic_atoms;
import org.lorislab.clingo4j.api.c.ClingoLibrary.clingo_theory_atoms;
import org.lorislab.clingo4j.api.c.clingo_ast_statement;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.acyc_edge_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.assume_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.begin_step_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.end_step_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.external_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.heuristic_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.init_program_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.minimize_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.output_atom_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.output_csp_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.output_term_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.project_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.rule_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.theory_atom_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.theory_atom_with_guard_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.theory_element_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.theory_term_compound_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.theory_term_number_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.theory_term_string_callback;
import org.lorislab.clingo4j.api.c.clingo_ground_program_observer.weight_rule_callback;
import org.lorislab.clingo4j.api.c.clingo_location;
import org.lorislab.clingo4j.api.c.clingo_part;
import org.lorislab.clingo4j.api.c.clingo_propagator;
import org.lorislab.clingo4j.api.c.clingo_propagator.check_callback;
import org.lorislab.clingo4j.api.c.clingo_propagator.init_callback;
import org.lorislab.clingo4j.api.c.clingo_propagator.propagate_callback;
import org.lorislab.clingo4j.api.c.clingo_propagator.undo_callback;
import org.lorislab.clingo4j.api.c.clingo_weighted_literal;
import org.lorislab.clingo4j.util.ClingoUtil;

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
        this(MESSAGE_LIMIT, null);
    }

    public Clingo(int messageLimit, final ClingoLogger logger, String... parameters) throws ClingoException {

        Pointer<ClingoLibrary.clingo_logger_t> p_logger = null;
        if (logger != null) {
            clingo_logger_t log = new clingo_logger_t() {
                public void apply(int code, Pointer<Byte> message, Pointer<?> data) {
                    try {
                        logger.warn(WarningCode.createWarningCode(code), message.getCString());
                    } catch (Exception e) {
                        // ignore
                    }
                }
            };
            p_logger = Pointer.getPointer(log);
        }

        // create a control object and pass command line arguments
        control = Pointer.allocatePointer(clingo_control.class);
        handleError(LIB.clingo_control_new(null, 0, p_logger, null, messageLimit, control), "Could not create clingo controller");
    }

    public Clingo(String... parameters) throws ClingoException {
        this(MESSAGE_LIMIT, null, parameters);
    }

    @Override
    public void close() {
        if (control != null) {
            LIB.clingo_control_free(control.get());
        }
    }

    public String getVersion() {
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

        Pointer<Pointer<Byte>> tmp_params = ClingoUtil.createStringArray(parameters);
        int tmp_size = ClingoUtil.arraySize(parameters);

        // add a logic program to the base part
        handleError(LIB.clingo_control_add(control.get(), tmp_name, tmp_params, tmp_size, tmp_program), "Error add the program to controller");
    }

    public void ground(String name) throws ClingoException {
        ground(Arrays.asList(new Part(name)), null);
    }

    public void ground(String name, GroundCallback callback) throws ClingoException {
        ground(Arrays.asList(new Part(name)), callback);
    }

    public void ground(List<Part> parts, GroundCallback callback) throws ClingoException {

        Pointer<ClingoLibrary.clingo_ground_callback_t> p_ground_callback = null;
        if (callback != null) {
            ClingoLibrary.clingo_ground_callback_t ground_callback = new ClingoLibrary.clingo_ground_callback_t() {
                @Override
                public boolean apply(Pointer<clingo_location> clocation, Pointer<Byte> cname, Pointer<Long> carguments, long carguments_size, Pointer<?> cdata, Pointer<ClingoLibrary.clingo_symbol_callback_t> csymbol_callback, Pointer<?> csymbol_callback_data) {

                    String name = cname.getCString();
                    Location loc = new Location(clocation);
                    List<Symbol> symbols = new SymbolList(carguments, carguments_size);

                    boolean result = true;
                    try {
                        callback.groundCallback(loc, name, symbols, (List<Symbol> symbols1) -> {

                            long v_size = ClingoUtil.arraySize(symbols1);

                            if (v_size == 0) {
                                return;
                            }

                            Pointer<Long> v_symbols = Symbol.toArray(symbols1);

                            handleError((csymbol_callback.get().apply(v_symbols, v_size, csymbol_callback_data)), "Error symbol callback apply!");
                        });
                    } catch (ClingoException ex) {
                        result = false;
                    }
                    return result;
                }

            };
            p_ground_callback = Pointer.getPointer(ground_callback);
        }

        Pointer<clingo_part> p_parts = Part.toArray(parts);
        int partsSize = ClingoUtil.arraySize(parts);

        // ground the base part
        handleError(LIB.clingo_control_ground(control.get(), p_parts, partsSize, p_ground_callback, null), "Error ground the program");

    }

    public void withBuilder(ProgramBuilderCallback callback) throws ClingoException {
        if (callback != null) {
            ProgramBuilder builder = builer();
            builder.begin();
            callback.run(builder);
            builder.end();
        }
    }

    public ProgramBuilder builer() throws ClingoException {
        Pointer<Pointer<clingo_program_builder>> tmp = Pointer.allocatePointer(clingo_program_builder.class);;
        handleError(LIB.clingo_control_program_builder(control.get(), tmp), "Error creating the program builder!");
        return new ProgramBuilder(tmp.get());
    }

    public SolveHandle solve() throws ClingoException {
        return solve(null, false, true);
    }

    public SolveHandle solve(SolveEventHandler handler, boolean asynchronous, boolean yield) throws ClingoException {

        int mode = 0;
        if (asynchronous) {
            mode |= (int) clingo_solve_mode.clingo_solve_mode_async.value;
        }
        if (yield) {
            mode |= (int) clingo_solve_mode.clingo_solve_mode_yield.value;
        }

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
        final Pointer<Pointer<clingo_solve_handle>> handle = Pointer.allocatePointer(ClingoLibrary.clingo_solve_handle.class);
        handleError(LIB.clingo_control_solve(control.get(), mode, null, 0, p_event, null, handle), "Error execute control solve");
        return new SolveHandle(handle.get());

    }

    public Statistics getStatistics() throws ClingoException {
        Pointer<Pointer<clingo_statistic>> statistics = Pointer.allocatePointer(clingo_statistic.class);
        handleError(LIB.clingo_control_statistics(control.get(), statistics), "Error reading the statistics!");
        Pointer<Long> key = Pointer.allocateLong();
        handleError(LIB.clingo_statistics_root(statistics.get(), key), "Error reading the statistics root key!");
        return new Statistics(statistics.get(), key.getLong());
    }

    public Configuration getConfiguration() throws ClingoException {
        Pointer<Pointer<clingo_configuration>> config = Pointer.allocatePointer(clingo_configuration.class);
        handleError(LIB.clingo_control_configuration(control.get(), config), "Error reading the configuration!");
        Pointer<Integer> key = Pointer.allocateInt();
        handleError(LIB.clingo_configuration_root(config.get(), key), "Error reading the configuration root key!");
        return new Configuration(config.get(), key.getInt());
    }

    public void assignExternal(Symbol atom, TruthValue value) throws ClingoException {
        handleError(LIB.clingo_control_assign_external(control.get(), atom.getSymbol(), value.getValue()), "Error clingo assign external!");
    }

    public void releaseExternal(Symbol atom) throws ClingoException {
        handleError(LIB.clingo_control_release_external(control.get(), atom.getSymbol()), "Error clingo release external!");
    }

    public static void handleError(boolean value, String message) throws ClingoException {
        if (!value) {
            Pointer<Byte> msg = LIB.clingo_error_message();
            int error = LIB.clingo_error_code();
            throw new ClingoException(ErrorCode.createErrorCode(error), msg.getCString(), message);
        }
    }

    public static void handleError(boolean ret, String message, ClingoException exc) throws ClingoException {
        if (!ret) {
            if (exc != null) {
                Exception p = exc;
                exc = null;
                throw exc;
            }
            handleError(false, message);
        }
    }

    public static void handleRuntimeError(boolean value, String message) {
        if (!value) {
            try {
                handleError(value, message);
            } catch (ClingoException ex) {
                throw new RuntimeException(ex.getMessage(), ex);
            }
        }
    }

    public static void handleRuntimeError(ClingoException ex) {
        throw new RuntimeException(ex.getMessage(), ex);
    }

    public SymbolicAtoms getSymbolicAtoms() throws ClingoException {
        Pointer<Pointer<clingo_symbolic_atoms>> ret = Pointer.allocatePointer(clingo_symbolic_atoms.class);
        handleError(LIB.clingo_control_symbolic_atoms(control.get(), ret), "Error reading the symbolic atoms!");
        return new SymbolicAtoms(ret.get());
    }

    public TheoryAtoms getTheoryAtoms() throws ClingoException {
        Pointer<Pointer<clingo_theory_atoms>> ret = Pointer.allocatePointer(clingo_theory_atoms.class);
        handleError(LIB.clingo_control_theory_atoms(control.get(), ret), "Error reading the theory atoms!");
        return new TheoryAtoms(ret.get());
    }

    public void cleanup() throws ClingoException {
        handleError(LIB.clingo_control_cleanup(control.get()), "Error clean up control!");
    }

    public boolean hasConst(String name) throws ClingoException {
        Pointer<Boolean> ret = Pointer.allocateBoolean();
        handleError(LIB.clingo_control_has_const(control.get(), Pointer.pointerToCString(name), ret), "Error has const " + name);
        return ret.get();
    }

    public Symbol getConst(String name) throws ClingoException {
        Pointer<Long> ret = Pointer.allocateLong();
        handleError(LIB.clingo_control_get_const(control.get(), Pointer.pointerToCString(name), ret), "Error get const " + name);
        return new Symbol(ret);
    }

    public void interrupt() {
        LIB.clingo_control_interrupt(control.get());
    }

    public Pointer<Pointer<?>> claspFacade() throws ClingoException {
        Pointer<Pointer<?>> ret = Pointer.allocatePointer();
        handleError(LIB.clingo_control_clasp_facade(control.get(), ret), "Error clasp facede!");
        return ret;
    }

    public void load(String file) throws ClingoException {
        handleError(LIB.clingo_control_load(control.get(), Pointer.pointerToCString(file)), "Error loading the file " + file);
    }

    public void useEnumerationAssumption(boolean value) throws ClingoException {
        handleError(LIB.clingo_control_use_enumeration_assumption(control.get(), value), "Error use enumeration assumption!");
    }

    public Backend getBackend() throws ClingoException {
        Pointer<Pointer<clingo_backend>> ret = Pointer.allocatePointer(clingo_backend.class);
        handleError(LIB.clingo_control_backend(control.get(), ret), "Error get backend!");
        return new Backend(ret.get());
    }

    void registerObserver(final GroundProgramObserver observer) throws ClingoException {
        registerObserver(observer, false);

    }

    void registerObserver(final GroundProgramObserver observer, boolean replace) throws ClingoException {
        if (observer == null) {
            return;
        }
        clingo_ground_program_observer tmp = new clingo_ground_program_observer();
        tmp.init_program(Pointer.getPointer(new init_program_callback() {
            @Override
            public boolean apply(boolean incremental, Pointer<?> data) {
                observer.initProgram(incremental);
                return true;
            }
        }));
        tmp.begin_step(Pointer.getPointer(new begin_step_callback() {
            @Override
            public boolean apply(Pointer<?> data) {
                observer.beginStep();
                return true;
            }
        }));
        tmp.end_step(Pointer.getPointer(new end_step_callback() {
            @Override
            public boolean apply(Pointer<?> data) {
                observer.endStep();
                return true;
            }
        }));
        tmp.rule(Pointer.getPointer(new rule_callback() {
            @Override
            public boolean apply(boolean choice, Pointer<Integer> head, long head_size, Pointer<Integer> body, long body_size, Pointer<?> data) {
                observer.rule(choice, new LiteralIntegerList(head, head_size), new LiteralIntegerList(body, body_size));
                return true;
            }
        }));
        tmp.weight_rule(Pointer.getPointer(new weight_rule_callback() {
            @Override
            public boolean apply(boolean choice, Pointer<Integer> head, long head_size, int lower_bound, Pointer<clingo_weighted_literal> body, long body_size, Pointer<?> data) {
                observer.weightRule(choice, new LiteralIntegerList(head, head_size), lower_bound, new WeightedLiteralList(body, body_size));
                return true;
            }
        }));
        tmp.minimize(Pointer.getPointer(new minimize_callback() {
            @Override
            public boolean apply(int priority, Pointer<clingo_weighted_literal> literals, long size, Pointer<?> data) {
                observer.minimize(priority, new WeightedLiteralList(literals, size));
                return true;
            }
        }));
        tmp.project(Pointer.getPointer(new project_callback() {
            @Override
            public boolean apply(Pointer<Integer> atoms, long size, Pointer<?> data) {
                observer.project(new LiteralIntegerList(atoms, size));
                return true;
            }
        }));
        tmp.output_atom(Pointer.getPointer(new output_atom_callback() {
            @Override
            public boolean apply(long symbol, int atom, Pointer<?> data) {
                observer.outputAtom(new Symbol(symbol), atom);
                return true;
            }
        }));
        tmp.output_term(Pointer.getPointer(new output_term_callback() {
            @Override
            public boolean apply(long symbol, Pointer<Integer> condition, long size, Pointer<?> data) {
                observer.outputTerm(new Symbol(symbol), new LiteralIntegerList(condition, size));
                return true;
            }
        }));
        tmp.output_csp(Pointer.getPointer(new output_csp_callback() {
            @Override
            public boolean apply(long symbol, int value, Pointer<Integer> condition, long size, Pointer<?> data) {
                observer.outputCsp(new Symbol(symbol), value, new LiteralIntegerList(condition, size));
                return true;
            }
        }));
        tmp.external(Pointer.getPointer(new external_callback() {
            @Override
            public boolean apply(int atom, int type, Pointer<?> data) {
                observer.external(atom, ExternalType.createExternalType(type));
                return true;
            }
        }));
        tmp.assume(Pointer.getPointer(new assume_callback() {
            @Override
            public boolean apply(Pointer<Integer> literals, long size, Pointer<?> data) {
                observer.assume(new LiteralIntegerList(literals, size));
                return true;
            }
        }));
        tmp.heuristic(Pointer.getPointer(new heuristic_callback() {
            @Override
            public boolean apply(int atom, int type, int bias, int priority, Pointer<Integer> condition, long size, Pointer<?> data) {
                observer.heuristic(atom, HeuristicType.createHeuristicType(type), bias, priority, new LiteralIntegerList(condition, size));
                return true;
            }
        }));
        tmp.acyc_edge(Pointer.getPointer(new acyc_edge_callback() {
            @Override
            public boolean apply(int node_u, int node_v, Pointer<Integer> condition, long size, Pointer<?> data) {
                observer.acycEdge(node_u, node_v, new LiteralIntegerList(condition, size));
                return true;
            }
        }));
        tmp.theory_atom(Pointer.getPointer(new theory_atom_callback() {
            @Override
            public boolean apply(int atom_id_or_zero, int term_id, Pointer<Integer> elements, long size, Pointer<?> data) {
                observer.theoryAtom(atom_id_or_zero, term_id, new LiteralIntegerList(elements, size));
                return true;
            }
        }));
        tmp.theory_atom_with_guard(Pointer.getPointer(new theory_atom_with_guard_callback() {
            @Override
            public boolean apply(int atom_id_or_zero, int term_id, Pointer<Integer> elements, long size, int operator_id, int right_hand_side_id, Pointer<?> data) {
                observer.theoryAtomWithGuard(atom_id_or_zero, term_id, new LiteralIntegerList(elements, size), operator_id, right_hand_side_id);
                return true;
            }
        }));
        tmp.theory_element(Pointer.getPointer(new theory_element_callback() {
            @Override
            public boolean apply(int element_id, Pointer<Integer> terms, long terms_size, Pointer<Integer> condition, long condition_size, Pointer<?> data) {
                observer.theoryElement(element_id, new LiteralIntegerList(terms, terms_size), new LiteralIntegerList(condition, condition_size));
                return true;
            }
        }));
        tmp.theory_term_compound(Pointer.getPointer(new theory_term_compound_callback() {
            @Override
            public boolean apply(int term_id, int name_id_or_type, Pointer<Integer> arguments, long size, Pointer<?> data) {
                observer.theoryTermCompound(term_id, name_id_or_type, new LiteralIntegerList(arguments, size));
                return true;
            }
        }));
        tmp.theory_term_number(Pointer.getPointer(new theory_term_number_callback() {
            @Override
            public boolean apply(int term_id, int number, Pointer<?> data) {
                observer.theoryTermNumber(term_id, number);
                return true;
            }
        }));
        tmp.theory_term_string(Pointer.getPointer(new theory_term_string_callback() {
            @Override
            public boolean apply(int term_id, Pointer<Byte> name, Pointer<?> data) {
                observer.theoryTermString(term_id, name.getCString());
                return true;
            }
        }));
        handleError(LIB.clingo_control_register_observer(control.get(), Pointer.getPointer(tmp), replace, null), "Error register observer!");
    }

    void registerPropagator(final Propagator propagator) throws ClingoException {
        registerPropagator(propagator, false);
    }

    void registerPropagator(final Propagator propagator, boolean sequential) throws ClingoException {
        if (propagator == null) {
            return;
        }
        clingo_propagator tmp = new clingo_propagator();
        tmp.init(Pointer.getPointer(new init_callback() {
            @Override
            public boolean apply(Pointer<clingo_propagate_init> init, Pointer<?> data) {
                propagator.init(new PropagateInit(init));
                return true;
            }
        }));
        tmp.propagate(Pointer.getPointer(new propagate_callback() {
            @Override
            public boolean apply(Pointer<clingo_propagate_control> control, Pointer<Integer> changes, long size, Pointer<?> data) {
                propagator.propagate(new PropagateControl(control), new Literal.LiteralIntegerList(changes, size));
                return true;
            }
        }));
        tmp.undo(Pointer.getPointer(new undo_callback() {
            @Override
            public boolean apply(Pointer<clingo_propagate_control> control, Pointer<Integer> changes, long size, Pointer<?> data) {
                propagator.undo(new PropagateControl(control), new Literal.LiteralIntegerList(changes, size));
                return true;
            }
        }));
        tmp.check(Pointer.getPointer(new check_callback() {
            @Override
            public boolean apply(Pointer<clingo_propagate_control> control, Pointer<?> data) {
                propagator.check(new PropagateControl(control));
                return true;
            }
        }));
        handleError(LIB.clingo_control_register_propagator(control.get(), Pointer.getPointer(tmp), null, sequential), "Error register propagator!");
    }

    public static Symbol parseTerm(String term, ClingoLogger logger, int messageLimit) throws ClingoException {
        Pointer<Long> ret = Pointer.allocateLong();

        Pointer<ClingoLibrary.clingo_logger_t> p_logger = null;
        if (logger != null) {
            clingo_logger_t log = new clingo_logger_t() {
                public void apply(int code, Pointer<Byte> message, Pointer<?> data) {
                    try {
                        logger.warn(WarningCode.createWarningCode(code), message.getCString());
                    } catch (Exception e) {
                        // ignore
                    }
                }
            };
            p_logger = Pointer.getPointer(log);
        }
        handleError(LIB.clingo_parse_term(Pointer.pointerToCString(term), p_logger, null, messageLimit, ret), "Error parse term!");
        return new Symbol(ret.get());
    }

    public String addString(String str) throws ClingoException {
        Pointer<Pointer<Byte>> ret = Pointer.allocatePointer(Byte.class);
        handleError(LIB.clingo_add_string(Pointer.pointerToCString(str), ret), "Error add the string!");
        return ret.get().getCString();
    }

    public static void parseProgram(String program, StatementCallback cb, ClingoLogger logger, int messageLimit) throws ClingoException {

        Pointer<ClingoLibrary.clingo_logger_t> p_logger = null;
        if (logger != null) {
            clingo_logger_t log = new clingo_logger_t() {
                public void apply(int code, Pointer<Byte> message, Pointer<?> data) {
                    try {
                        logger.warn(WarningCode.createWarningCode(code), message.getCString());
                    } catch (Exception e) {
                        // ignore
                    }
                }
            };
            p_logger = Pointer.getPointer(log);
        }

        Pointer<clingo_ast_callback_t> p_callback = null;
        if (cb != null) {
            clingo_ast_callback_t call = new clingo_ast_callback_t() {
                @Override
                public boolean apply(Pointer<clingo_ast_statement> clingo_ast_statement_tPtr1, Pointer<?> voidPtr1) {
                    //TODO: missing implementation
//                    cb.callback(statement);
                    return true;
                }
            };
            p_callback = Pointer.getPointer(call);
        }
        handleError(LIB.clingo_parse_program(Pointer.pointerToCString(program), p_callback, null, p_logger, null, messageLimit), "Error parse program!");
    }

}
