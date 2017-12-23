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
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import org.lorislab.clingo4j.api.ast.ASTToC;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.ast.TheoryTerm.TheoryTermData;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.util.ClingoUtil;

/**
 *
 * @author andrej
 */
public class Symbol implements TermData, TheoryTermData {

    private final Pointer<Long> pointer;

    public Symbol(Pointer<Long> pointer) {
        this.pointer = pointer;
    }

    public Pointer<Long> getPointer() {
        return pointer;
    }

    public SymbolType getType() {
        int type = LIB.clingo_symbol_type(pointer.get());
        return SymbolType.createSymbolType(type);
    }

    public boolean isNegative() throws ClingoException {
        Pointer<Boolean> negative = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbol_is_negative(pointer.get(), negative), "Error reading the symbol negative!");
        return negative.get();
    }

    public boolean isPositive() throws ClingoException {
        Pointer<Boolean> positive = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbol_is_positive(pointer.get(), positive), "Error reading the symbol positive!");
        return positive.get();
    }

    public int getNumber() throws ClingoException {
        Pointer<Integer> number = Pointer.allocateInt();
        handleError(LIB.clingo_symbol_number(pointer.get(), number), "Error reading the symbol number!");
        return number.get();
    }

    public String getName() throws ClingoException {
        Pointer<Pointer<Byte>> name = Pointer.allocatePointer(Byte.class);
        handleError(LIB.clingo_symbol_name(pointer.get(), name), "Error reading the symbol name!");
        return name.get().getCString();
    }

    public List<Symbol> getArguments() throws ClingoException {
        Pointer<Pointer<Long>> args = Pointer.allocatePointer(Long.class);
        Pointer<SizeT> args_size = Pointer.allocateSizeT();
        handleError(LIB.clingo_symbol_arguments(pointer.get(), args, args_size), "Error reading the symbol arguments!");
        return new SymbolList(args.get(), args_size.getInt());
    }

    public long getHash() {
        return LIB.clingo_symbol_hash(pointer.get());
    }

    @Override
    public String toString() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_symbol_to_string_size(pointer.get(), size), "Error reading the symbol string size!");
        Pointer<Byte> string = Pointer.allocateBytes(size.getLong());
        handleRuntimeError(LIB.clingo_symbol_to_string(pointer.get(), string, size.getLong()), "Error reading the symbol string value!");
        return string.getCString();
    }

    @Override
    public clingo_ast_term createTerm() {
        return ASTToC.visitTerm(this);
    }

    @Override
    public clingo_ast_theory_term createTheoryTerm() {
        return ASTToC.visitTheoryTerm(this);
    }

    public static Symbol createId(String name, boolean positive) throws ClingoException {
        Pointer<Long> pointer = Pointer.allocateLong();
        handleError(LIB.clingo_symbol_create_id(Pointer.pointerToCString(name), positive, pointer), "Error creating the ID!");
        return new Symbol(pointer);
    }

    public static Symbol createString(String string) throws ClingoException {
        Pointer<Long> pointer = Pointer.allocateLong();
        handleError(LIB.clingo_symbol_create_string(Pointer.pointerToCString(string), pointer), "Error creating the string!");
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

    public static Symbol createFunction(String name, List<Symbol> symbols, boolean positive) throws ClingoException {
        Pointer<Long> pointer = Pointer.allocateLong();

        int size = 0;
        Pointer<Long> arguments = null;
        if (symbols != null && !symbols.isEmpty()) {
            arguments = Symbol.toArray(symbols);
            size = symbols.size();
        }

        handleError(LIB.clingo_symbol_create_function(Pointer.pointerToCString(name), arguments, size, positive, pointer), "Error creating the function!");
        return new Symbol(pointer);
    }

    public boolean isEqual(Symbol b) {
        return LIB.clingo_symbol_is_equal_to(pointer.get(), b.pointer.get());
    }

    public boolean isNotEqual(Symbol b) {
        return !LIB.clingo_symbol_is_equal_to(pointer.get(), b.pointer.get());
    }

    public boolean isLessThan(Symbol b) {
        return LIB.clingo_symbol_is_less_than(pointer.get(), b.pointer.get());
    }

    public boolean isLessEqualThan(Symbol b) {
        return !LIB.clingo_symbol_is_less_than(b.pointer.get(), pointer.get());
    }

    public boolean isMoreThan(Symbol b) {
        return LIB.clingo_symbol_is_less_than(b.pointer.get(), pointer.get());
    }

    public boolean isMoreEqualThan(Symbol b) {
        return !LIB.clingo_symbol_is_less_than(pointer.get(), b.pointer.get());
    }

    public static Pointer<Long> toArray(List<Symbol> symbols) {
        return ClingoUtil.createArray(symbols, Long.class, Symbol::getPointerFromSymbol);
    }
    
    private static Long getPointerFromSymbol(Symbol symbol) {
        return symbol.getPointer().get();
    }
    
    public static class SymbolList extends SpanList<Symbol, Long> {

        public SymbolList(Pointer<Long> pointer, long size) {
            super(pointer, size);
        }

        @Override
        protected Symbol getItem(Pointer<Long> p) {
            return new Symbol(p);
        }

    }

}
