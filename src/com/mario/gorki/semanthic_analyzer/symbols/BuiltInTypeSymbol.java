package com.mario.gorki.semanthic_analyzer.symbols;

import com.mario.gorki.abstract_tree.enums.Type;
import com.mario.gorki.semanthic_analyzer.Symbol;

public class BuiltInTypeSymbol implements Symbol {
    public Type type;

    public BuiltInTypeSymbol(Type type) {
        this.type = type;
    }

    @Override
    public String getName() {
        return type.toString();
    }

    @Override
    public int getSortOrder() {
        return 0;
    }

    @Override
    public String toString() {
        return "<BuiltInTypeSymbol(name ="+type.toString()+")>";
    }
}
