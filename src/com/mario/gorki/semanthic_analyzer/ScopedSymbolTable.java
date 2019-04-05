package com.mario.gorki.semanthic_analyzer;

import com.mario.gorki.abstract_tree.enums.Type;
import com.mario.gorki.semanthic_analyzer.symbols.BuiltInProcedureSymbol;
import com.mario.gorki.semanthic_analyzer.symbols.BuiltInTypeSymbol;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ScopedSymbolTable {
    private HashMap<String, Symbol> symbols = new HashMap<>();
    private String name;
    private Integer level;
    private ScopedSymbolTable enclosingScope;

    public ScopedSymbolTable(String name, Integer level, ScopedSymbolTable enclosingScope) {
        this.name = name;
        this.level = level;
        this.enclosingScope = enclosingScope;

        insertBuiltInTypes();
        insertBuildInProcedures();
    }

    private void insertBuiltInTypes() {
        insert(new BuiltInTypeSymbol(Type.INT));
        insert(new BuiltInTypeSymbol(Type.REAL));
        insert(new BuiltInTypeSymbol(Type.BOOL));
        insert(new BuiltInTypeSymbol(Type.STRING));
    }

    private void insertBuildInProcedures() {
        insert(new BuiltInProcedureSymbol("writeln", null, true));
        insert(new BuiltInProcedureSymbol("write", null, true));
        insert(new BuiltInProcedureSymbol("read", null, true));
        insert(new BuiltInProcedureSymbol("readln", null, true));
        ArrayList<Symbol> data = new ArrayList<>();
        data.add(new BuiltInTypeSymbol(Type.INT));
        insert(new BuiltInProcedureSymbol("random", data, false));
        insert(new BuiltInProcedureSymbol("length", null, true));
    }

    public String getName() {
        return name;
    }

    public Integer getLevel() {
        return level;
    }

    public ScopedSymbolTable getEnclosingScope() {
        return enclosingScope;
    }

    public HashMap<String, Symbol> getSymbols() {
        return symbols;
    }

    public void insert(Symbol symbol) {
        symbols.put(symbol.getName().toLowerCase(), symbol);
    }

    public Symbol lookup(String name){
       return lookup(name, false);
    }

    public Symbol lookup(String name, boolean currentScopeOnly) {
        if (symbols.containsKey(name.toLowerCase()))
            return symbols.get(name.toLowerCase());


        if (currentScopeOnly || enclosingScope == null)
            return null;

        return enclosingScope.lookup(name);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("------");
        builder.append("SCOPE \n");
        builder.append(String.format("Scope name    : %s \n", name));
        builder.append(String.format("Scope level   : %d \n", level));
        builder.append("Scope (Scoped symbol table) contents\n");
        ArrayList<Symbol> data = new ArrayList<>(symbols.values());
        data.sort(Comparator.comparingInt(Symbol::getSortOrder));
        for (Symbol s : data)
            builder.append(String.format("%s \n", s.toString()));

        return builder.toString();
    }
}
