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

/**
 *
 * @author andrej
 */
public class Symbol {

    private Integer number;

    private String name;

    private String string;

    private boolean positive;

    private boolean negative;

    private int hash;

    private SymbolType type;

    private List<Symbol> arguments;

    public Symbol(SymbolType type) {
        this.type = type;
    }

    public Symbol(String name, SymbolType type) {
        this(name, type, null, true);
    }

    public Symbol(String name, SymbolType type, List<Symbol> arguments, boolean positive) {
        this.name = name;
        this.type = type;
        this.arguments = arguments;
        this.positive = positive;
        this.negative = false;
    }

    public SymbolType getType() {
        return type;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public List<Symbol> getArguments() {
        return arguments;
    }

    public void addArgument(Symbol symbol) {
        if (arguments == null) {
            arguments = new ArrayList<>();
        }
        arguments.add(symbol);
    }

    public boolean isNegative() {
        return negative;
    }

    public void setNegative(boolean negative) {
        this.negative = negative;
    }

    public boolean isPositive() {
        return positive;
    }

    public void setPositive(boolean positive) {
        this.positive = positive;
    }

    public int getHash() {
        return hash;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(",type:").append(type);
        switch (type) {
            case NUMBER: sb.append(",number:").append(number); break;
            case STRING: sb.append(",string:").append(string); break;
        }
        if (arguments != null) {
            sb.append(",arguments:");
            for (Symbol arg : arguments) {
                sb.append("\n\t").append(arg.toString());
            }
        }
        return sb.toString();
    }

}
