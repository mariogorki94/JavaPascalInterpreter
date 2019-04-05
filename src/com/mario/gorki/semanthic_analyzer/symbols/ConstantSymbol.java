package com.mario.gorki.semanthic_analyzer.symbols;

import com.mario.gorki.semanthic_analyzer.Symbol;

public class ConstantSymbol implements Symbol {
    public String name;

    public ConstantSymbol(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSortOrder() {
        return 2;
    }

    @Override
    public String toString() {
        return "<ConstantSymbol(name=" + name + ")>";
    }
}
