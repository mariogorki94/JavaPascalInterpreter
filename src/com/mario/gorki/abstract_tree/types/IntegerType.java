package com.mario.gorki.abstract_tree.types;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Value;
import com.mario.gorki.abstract_tree.enums.Type;
import com.mario.gorki.exceptions.InterpreterException;

public class IntegerType implements AST, Value {
    private Integer number;

    public IntegerType(Integer value) {
        this.number = value;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "INTEGER("+number.toString()+")";
    }

    @Override
    public String getValue() {
        return String.valueOf(number);
    }

    @Override
    public AST[] getChildren() {
        return new AST[0];
    }

    @Override
    public boolean typeEquals(Type type) {
        return type == Type.INT;
    }

    @Override
    public boolean equals(Value v) {
        if (v instanceof IntegerType){
            return this.number.intValue() == ((IntegerType) v).getNumber().intValue();
        }
        if (v instanceof RealType){
            return this.number.doubleValue() == ((RealType) v).getNumber();
        }

        return false;
    }

    @Override
    public boolean greaterThan(Value v) throws InterpreterException {
        if (v instanceof IntegerType){
            return this.number > ((IntegerType) v).getNumber();
        }
        if (v instanceof RealType){
            return this.number > ((RealType) v).getNumber();
        }

        throw new InterpreterException("Cannot compare " + this.toString() + " with " + v.toString());
    }

    @Override
    public boolean lessThan(Value v) throws InterpreterException {
        if (v instanceof IntegerType){
            return this.number < ((IntegerType) v).getNumber();
        }
        if (v instanceof RealType){
            return this.number < ((RealType) v).getNumber();
        }

        throw new InterpreterException("Cannot compare " + this.toString() + " with " + v.toString());
    }

    @Override
    public Value plus(Value v) throws InterpreterException {
        if (v instanceof IntegerType)
            return new IntegerType(this.getNumber() + ((IntegerType) v).getNumber());
        if (v instanceof RealType)
            return new RealType(this.getNumber() + ((RealType) v).getNumber());

        throw new InterpreterException("Cannot apply + on " + this.toString() + " and " + v.toString());
    }

    @Override
    public Value minus(Value v) throws InterpreterException {
        if (v instanceof IntegerType)
            return new IntegerType(this.getNumber() - ((IntegerType) v).getNumber());
        if (v instanceof RealType)
            return new RealType(this.getNumber() - ((RealType) v).getNumber());

        throw new InterpreterException("Cannot apply - on " + this.toString() + " and " + v.toString());
    }

    @Override
    public Value multiplication(Value v) throws InterpreterException {
        if (v instanceof IntegerType)
            return new IntegerType(this.getNumber() * ((IntegerType) v).getNumber());
        if (v instanceof RealType)
            return new RealType(this.getNumber() * ((RealType) v).getNumber());

        throw new InterpreterException("Cannot apply * on " + this.toString() + " and " + v.toString());
    }

    @Override
    public Value floatDivision(Value v) throws InterpreterException {
        if (v instanceof IntegerType)
            return new RealType((double) (this.getNumber() / ((IntegerType) v).getNumber()));
        if (v instanceof RealType)
            return new RealType(this.getNumber() / ((RealType) v).getNumber());

        throw new InterpreterException("Cannot apply / on " + this.toString() + " and " + v.toString());
    }

    @Override
    public Value integerDivision(Value v) throws InterpreterException {
        if (v instanceof IntegerType)
            return new IntegerType(this.getNumber() / ((IntegerType) v).getNumber());

        throw new InterpreterException("Cannot apply integer / on " + this.toString() + " and " + v.toString());
    }
}
