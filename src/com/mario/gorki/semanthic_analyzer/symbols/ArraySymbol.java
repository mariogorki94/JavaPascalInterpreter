package com.mario.gorki.semanthic_analyzer.symbols;

import com.mario.gorki.semanthic_analyzer.Symbol;

public class ArraySymbol extends VariableSymbol {
    public Integer startIndex;
    public Integer endIndex;

    public ArraySymbol(String name, Symbol type, Integer startIndex, Integer endIndex) {
        super(name, type);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }

    @Override
    public String toString() {
        return "<ArrayVarSymbol(name=" + name + ", type=" + type.getName() +
                ", start=" + startIndex + ", end=" + endIndex + ")>";
    }
}
