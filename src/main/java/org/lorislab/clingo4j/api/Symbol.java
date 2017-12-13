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
import java.util.List;
import org.bridj.Pointer;
import org.bridj.SizeT;
import static org.lorislab.clingo4j.api.Clingo.LIB;
import static org.lorislab.clingo4j.api.Clingo.throwError;

/**
 *
 * @author andrej
 */
public class Symbol {

    private Pointer<Long> pointer;

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

    public boolean isNegative() {
        Pointer<Boolean> negative = Pointer.allocateBoolean();
        if (!LIB.clingo_symbol_is_negative(pointer.get(), negative)) {
            throwError("Error reading the symbol negative!");
        }
        return negative.get();
    }

    public boolean isPositive() {
        Pointer<Boolean> positive = Pointer.allocateBoolean();
        if (!LIB.clingo_symbol_is_positive(pointer.get(), positive)) {
            throwError("Error reading the symbol positive!");
        }
        return positive.get();
    }

    public int getNumber() {
        Pointer<Integer> number = Pointer.allocateInt();
        if (!LIB.clingo_symbol_number(pointer.get(), number)) {
            throwError("Error reading the symbol number!");
        }
        return number.get();
    }

    public String getName() {
        Pointer<Pointer<Byte>> name = Pointer.allocatePointer(Byte.class);
        if (!LIB.clingo_symbol_name(pointer.get(), name)) {
            throwError("Error reading the symbol name!");
        }
        return name.get().getCString();
    }

    public List<Symbol> getArguments() {
        List<Symbol> result = null;

        Pointer<Pointer<Long>> args = Pointer.allocatePointer(Long.class);
        Pointer<SizeT> args_size = Pointer.allocateSizeT();
        if (!LIB.clingo_symbol_arguments(pointer.get(), args, args_size)) {
            throwError("Error reading the symbol arguments!");
        }

        if (0 < args_size.getLong()) {
            result = new ArrayList<>((int) args_size.getLong());
            for (int i = 0; i < args_size.getLong(); i++) {
                result.add(new Symbol(args.get(i)));
            }
        }
        return result;
    }
    
    public long getHash() {
        return LIB.clingo_symbol_hash(pointer.get());
    }
    
    @Override
    public String toString() {
        Pointer<SizeT> size = Pointer.allocateSizeT();
        if (!LIB.clingo_symbol_to_string_size(pointer.get(), size)) {
            throwError("Error reading the symbol string size!");
        }
        
        Pointer<Byte> string = Pointer.allocateBytes(size.getLong());
        if (!LIB.clingo_symbol_to_string(pointer.get(), string, size.getLong())) {
            throwError("Error reading the symbol string value!");
        }
        
        return string.getCString();
    }    
}
