package com.mario.gorki.semanthic_analyzer.symbols;

import com.mario.gorki.abstract_tree.operations.Procedure;
import com.mario.gorki.semanthic_analyzer.Symbol;

import java.util.ArrayList;

public class FunctionSymbol extends ProcedureSymbol {
    Symbol returnType;

    public FunctionSymbol(String name, ArrayList<Symbol> params, Procedure body, Symbol returnType) {
        super(name, params, body);
        this.returnType = returnType;
    }

    @Override
    public int getSortOrder() {
        return 4;
    }

    @Override
    public String toString() {
        return "<FunctionSymbol(name=" + name + ", parameters=[" + params.toString() + "}, returnType=" + returnType.toString() + ")]>";
    }
}
