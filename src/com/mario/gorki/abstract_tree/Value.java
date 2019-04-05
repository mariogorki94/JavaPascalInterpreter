package com.mario.gorki.abstract_tree;

import com.mario.gorki.abstract_tree.enums.Type;
import com.mario.gorki.exceptions.InterpreterException;

public interface Value {
    boolean typeEquals(Type type);
    boolean equals(Value v);
    boolean greaterThan(Value v) throws InterpreterException;
    boolean lessThan(Value v) throws InterpreterException;
    Value plus(Value v) throws InterpreterException;
    Value minus(Value v) throws InterpreterException;
    Value multiplication(Value v) throws InterpreterException;
    Value floatDivision(Value v) throws InterpreterException;
    Value integerDivision(Value v) throws InterpreterException;
}
