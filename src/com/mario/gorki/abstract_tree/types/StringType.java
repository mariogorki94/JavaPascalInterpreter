package com.mario.gorki.abstract_tree.types;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Value;
import com.mario.gorki.abstract_tree.enums.Type;
import com.mario.gorki.exceptions.InterpreterException;

public class StringType implements AST, Value {
    private String string;

    public StringType(String value) {
        this.string = value;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return "STRING(" + string + ")";
    }

    @Override
    public String getValue() {
        return string;
    }

    @Override
    public AST[] getChildren() {
        return new AST[0];
    }


    @Override
    public boolean typeEquals(Type type) {
        return type == Type.STRING;
    }

    @Override
    public boolean equals(Value v) {
        return v instanceof StringType && this.string.equals(((StringType) v).getString());
    }

    @Override
    public boolean greaterThan(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot compare string type");
    }

    @Override
    public boolean lessThan(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot compare string type");
    }

    @Override
    public Value plus(Value v) throws InterpreterException {
        if (v instanceof StringType)
            return new StringType(this.getValue() + ((StringType) v).getValue());

        throw new InterpreterException("Cannot apply + to " + this.toString() + " and " + v.toString());
    }

    @Override
    public Value minus(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply - on string type");
    }

    @Override
    public Value multiplication(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply * on string type");
    }

    @Override
    public Value floatDivision(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply / on string type");
    }

    @Override
    public Value integerDivision(Value v) throws InterpreterException {
        throw new InterpreterException("Cannot apply integer / on string type");
    }
}
