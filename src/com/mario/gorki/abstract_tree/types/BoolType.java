package com.mario.gorki.abstract_tree.types;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Value;
import com.mario.gorki.abstract_tree.enums.Type;
import com.mario.gorki.exceptions.InterpreterException;

public class BoolType implements AST, Value {
    private Boolean bool;

    public BoolType(Boolean value) {
        this.bool = value;
    }

    public Boolean getBool() {
        return bool;
    }

    public void setBool(Boolean bool) {
        this.bool = bool;
    }

    @Override
    public String toString() {
        return "BOOL(" + bool.toString() + ")";
    }

    @Override
    public String getValue() {
        return String.valueOf(bool);
    }

    @Override
    public AST[] getChildren() {
        return new AST[0];
    }

    @Override
    public boolean typeEquals(Type type) {
        return type == Type.BOOL;
    }

    @Override
    public boolean equals(Value v) {
        return v instanceof BoolType && this.bool.equals(((BoolType) v).getBool());
    }

    @Override
    public boolean greaterThan(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot compare bool type");
    }

    @Override
    public boolean lessThan(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot compare bool type");
    }

    @Override
    public Value plus(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply + on bool type");
    }

    @Override
    public Value minus(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply - on bool type");
    }

    @Override
    public Value multiplication(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply * on bool type");
    }

    @Override
    public Value floatDivision(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply / on bool type");
    }

    @Override
    public Value integerDivision(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply integer / on bool type");
    }
}
