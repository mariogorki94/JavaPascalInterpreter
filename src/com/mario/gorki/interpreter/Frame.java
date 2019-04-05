package com.mario.gorki.interpreter;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Value;
import com.mario.gorki.abstract_tree.types.*;
import com.mario.gorki.exceptions.InterpreterException;
import com.mario.gorki.semanthic_analyzer.ScopedSymbolTable;
import com.mario.gorki.semanthic_analyzer.Symbol;
import com.mario.gorki.semanthic_analyzer.symbols.ArraySymbol;
import com.mario.gorki.semanthic_analyzer.symbols.BuiltInTypeSymbol;
import com.mario.gorki.semanthic_analyzer.symbols.ConstantSymbol;
import com.mario.gorki.semanthic_analyzer.symbols.VariableSymbol;

import java.util.HashMap;

public class Frame {
    private HashMap<String, Value> scalars = new HashMap<>();
    private HashMap<String, Value> constants = new HashMap<>();
    private HashMap<String, Value[]> arrays = new HashMap<>();

    private ScopedSymbolTable scope;
    private Frame previousFrame;
    private Value returnValue = null;

    public Frame(ScopedSymbolTable scope, Frame previousFrame) {
        this.scope = scope;
        this.previousFrame = previousFrame;

        for (Symbol symbol : scope.getSymbols().values()) {
            ArraySymbol arraySymbol = null;
            BuiltInTypeSymbol type = null;
            if (symbol instanceof ArraySymbol) {
                arraySymbol = (ArraySymbol) symbol;

                if (arraySymbol.type instanceof BuiltInTypeSymbol)
                    type = (BuiltInTypeSymbol) arraySymbol.type;
            }

            if (arraySymbol == null || type == null)
                continue;

            String name = arraySymbol.name.toLowerCase();
            int size = arraySymbol.endIndex - arraySymbol.startIndex + 1;
            switch (type.type) {
                case STRING:
                    arrays.put(name, new StringType[size]);
                    break;
                case REAL:
                    arrays.put(name, new RealType[size]);
                    break;
                case INT:
                    arrays.put(name, new IntegerType[size]);
                    break;
                case BOOL:
                    arrays.put(name, new BoolType[size]);
                    break;
            }
        }
    }

    public HashMap<String, Value> getScalars() {
        return scalars;
    }

    public HashMap<String, Value[]> getArrays() {
        return arrays;
    }

    public ScopedSymbolTable getScope() {
        return scope;
    }

    public Frame getPreviousFrame() {
        return previousFrame;
    }

    public Value getReturnValue() {
        return returnValue;
    }

    public void remove(String variable) {
        scalars.remove(variable.toLowerCase());
    }

    public void set(String variable, Value value, int index) throws InterpreterException {
        try {
            Symbol s = scope.lookup(variable, true);
            ArraySymbol arraySymbol = (ArraySymbol) s;
            BuiltInTypeSymbol type = (BuiltInTypeSymbol) arraySymbol.type;

            int computedIndex = index - arraySymbol.startIndex;
            try {
                if (!value.typeEquals(type.type))
                    throw new Exception();
                arrays.get(variable.toLowerCase())[computedIndex] = value;
            } catch (Exception e) {
                throw new InterpreterException("Cannot assign " + value + " to " + type);
            }
        } catch (NullPointerException | ClassCastException e) {
            e.printStackTrace();
            previousFrame.set(variable, value, index);
        }
    }

    public void set(String variable, Value value) throws InterpreterException {
        if (variable.toLowerCase().equals(scope.getName().toLowerCase()) && scope.getLevel() > 1) {
            returnValue = value;
            return;
        }

        try {
            Symbol s = scope.lookup(variable, true);
            VariableSymbol variableSymbol = (VariableSymbol) s;
            BuiltInTypeSymbol type = (BuiltInTypeSymbol) variableSymbol.type;
            try {
                if (!value.typeEquals(type.type))
                    throw new Exception();

                scalars.put(variable.toLowerCase(), value);
            } catch (Exception e) {
                throw new InterpreterException("Cannot assign " + value + " to " + type);
            }
        } catch (NullPointerException | ClassCastException e) {
            if (previousFrame == null)
                throw new InterpreterException("Variable " + variable + " not found");

            previousFrame.set(variable, value);
        }
    }

    public void setConstant(String variable, Value value) throws InterpreterException {
        Symbol s = scope.lookup(variable, true);
        if (s == null || !(s instanceof ConstantSymbol))
            throw new InterpreterException("Constant symbol " + variable + " not found in scope");

        constants.put(variable.toLowerCase(), value);
    }

    public Value getValue(String variable) {
        Symbol s = scope.lookup(variable, true);
        if (s == null)
            return previousFrame.getValue(variable);

        if (s instanceof VariableSymbol)
            return scalars.get(variable.toLowerCase());
        if (s instanceof ConstantSymbol)
            return constants.get(variable.toLowerCase());

        return previousFrame.getValue(variable);
    }

    public Value getValue(String variable, int index) {
        Symbol s = (scope.lookup(variable, true));
        if (s != null && s instanceof ArraySymbol) {
            int computedIndex = index - ((ArraySymbol) s).startIndex;
            return arrays.get(variable.toLowerCase())[computedIndex];
        }
        return previousFrame.getValue(variable, index);
    }


}
