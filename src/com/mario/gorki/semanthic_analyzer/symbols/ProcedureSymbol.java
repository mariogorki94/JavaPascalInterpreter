package com.mario.gorki.semanthic_analyzer.symbols;

import com.mario.gorki.abstract_tree.operations.Procedure;
import com.mario.gorki.semanthic_analyzer.Symbol;

import java.util.ArrayList;

public class ProcedureSymbol implements Symbol {
    public String name;
    public ArrayList<Symbol> params;
    public Procedure body;

    public ProcedureSymbol(String name, ArrayList<Symbol> params, Procedure body) {
        this.name = name;
        this.params = params;
        this.body = body;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getSortOrder() {
        return 5;
    }

    @Override
    public String toString() {
        return "<ProcedureSymbol(name="+name+", parameters=["+(params != null ? params.toString() : "none")+"})]>";
    }
}
