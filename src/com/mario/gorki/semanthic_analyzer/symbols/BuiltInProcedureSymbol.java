package com.mario.gorki.semanthic_analyzer.symbols;


import com.mario.gorki.semanthic_analyzer.Symbol;

import java.util.ArrayList;

public class BuiltInProcedureSymbol implements Symbol {
    public String name;
    public ArrayList<Symbol> params;
    public Boolean hasVariableParameters;

    public BuiltInProcedureSymbol(String name, ArrayList<Symbol> params, Boolean hasVariableParameters) {
        this.name = name;
        this.params = params;
        this.hasVariableParameters = hasVariableParameters;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSortOrder() {
        return 1;
    }

    @Override
    public String toString() {
        return "<BuiltInProcedureSymbol(name=" + name + ", parameters=[" + (params != null ? params.toString() : "none") + "})]>";
    }
}
