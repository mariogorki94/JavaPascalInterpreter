package com.mario.gorki.semanthic_analyzer.symbols;

import com.mario.gorki.semanthic_analyzer.Symbol;

public class VariableSymbol implements Symbol {
    public String name;
    public Symbol type;

    public VariableSymbol(String name, Symbol type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSortOrder() {
        return 3;
    }

    @Override
    public String toString() {
        return "<VarSymbol(name=" + name + ", type=" + type.getName()+ ">";
    }
}
