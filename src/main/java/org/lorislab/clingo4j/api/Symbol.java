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

import org.lorislab.clingo4j.util.SpanList;
import java.util.List;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import org.lorislab.clingo4j.api.ast.Term.TermData;
import org.lorislab.clingo4j.api.ast.TheoryTerm.TheoryTermData;
import org.lorislab.clingo4j.api.c.clingo_ast_term;
import org.lorislab.clingo4j.api.c.clingo_ast_theory_term;
import static org.lorislab.clingo4j.api.Clingo.handleError;
import static org.lorislab.clingo4j.api.Clingo.handleRuntimeError;
import org.lorislab.clingo4j.api.ast.TermType;
import org.lorislab.clingo4j.api.ast.TheoryTermType;
import org.lorislab.clingo4j.util.ClingoUtil;
import org.lorislab.clingo4j.util.EnumValue;

/**
 *
 * @author andrej
 */
public class Symbol implements TermData, TheoryTermData {

    private final Long symbol;

    public Symbol(Long symbol) {
        this.symbol = symbol;
    }
    
    public Symbol(Pointer<Long> pointer) {
        this.symbol = pointer.get();
    }

    public Long getSymbol() {
        return symbol;
    }

    public SymbolType getType() {
        int type = LIB.clingo_symbol_type(symbol);
        return EnumValue.valueOfInt(SymbolType.class, type);
    }

    public boolean isNegative() throws ClingoException {
        Pointer<Boolean> negative = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbol_is_negative(symbol, negative), "Error reading the symbol negative!");
        return negative.get();
    }

    public boolean isPositive() throws ClingoException {
        Pointer<Boolean> positive = Pointer.allocateBoolean();
        handleError(LIB.clingo_symbol_is_positive(symbol, positive), "Error reading the symbol positive!");
        return positive.get();
    }

    public int getNumber() throws ClingoException {
        Pointer<Integer> number = Pointer.allocateInt();
        handleError(LIB.clingo_symbol_number(symbol, number), "Error reading the symbol number!");
        return number.get();
    }

    public String getName() throws ClingoException {
        Pointer<Pointer<Byte>> name = Pointer.allocatePointer(Byte.class);
        handleError(LIB.clingo_symbol_name(symbol, name), "Error reading the symbol name!");
        return name.getCString();
    }

    public List<Symbol> getArguments() throws ClingoException {
        Pointer<Pointer<Long>> args = Pointer.allocatePointer(Long.class);
        Pointer<SizeT> args_size = Pointer.allocateSizeT();
        handleError(LIB.clingo_symbol_arguments(symbol, args, args_size), "Error reading the symbol arguments!");
        return new SymbolList(args.get(), args_size.getInt());
    }

    public long getHash() {
        return LIB.clingo_symbol_hash(symbol);
    }

    @Override
    public String toString() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        handleRuntimeError(LIB.clingo_symbol_to_string_size(symbol, size), "Error reading the symbol string size!");
        Pointer<Byte> string = Pointer.allocateBytes(size.getLong());
        handleRuntimeError(LIB.clingo_symbol_to_string(symbol, string, size.getLong()), "Error reading the symbol string value!");
        return string.getCString();
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
        return LIB.clingo_symbol_is_equal_to(symbol, b.symbol);
    }

    public boolean isNotEqual(Symbol b) {
        return !LIB.clingo_symbol_is_equal_to(symbol, b.symbol);
    }

    public boolean isLessThan(Symbol b) {
        return LIB.clingo_symbol_is_less_than(symbol, b.symbol);
    }

    public boolean isLessEqualThan(Symbol b) {
        return !LIB.clingo_symbol_is_less_than(b.symbol, symbol);
    }

    public boolean isMoreThan(Symbol b) {
        return LIB.clingo_symbol_is_less_than(b.symbol, symbol);
    }

    public boolean isMoreEqualThan(Symbol b) {
        return !LIB.clingo_symbol_is_less_than(symbol, b.symbol);
    }

    public static Pointer<Long> toArray(List<Symbol> symbols) {
        return ClingoUtil.createArray(symbols, Long.class, Symbol::getPointerFromSymbol);
    }
    
    private static Long getPointerFromSymbol(Symbol symbol) {
        return symbol.symbol;
    }

    @Override
    public void updateTerm(clingo_ast_term ret) {
        ret.field1().symbol(symbol);
    }

    @Override
    public TermType getTermType() {
        return TermType.SYMBOL;
    }

    @Override
    public void updateTheoryTerm(clingo_ast_theory_term ret) {
        ret.field1().symbol(symbol);
    }

    @Override
    public TheoryTermType getTheoryTermType() {
        return TheoryTermType.SYMBOL;
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
