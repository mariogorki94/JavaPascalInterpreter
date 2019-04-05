package com.mario.gorki.interpreter;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Value;
import com.mario.gorki.abstract_tree.data.Variable;
import com.mario.gorki.abstract_tree.operations.*;
import com.mario.gorki.abstract_tree.types.*;
import com.mario.gorki.exceptions.InterpreterException;
import com.sun.org.apache.bcel.internal.classfile.Constant;

public interface Evaluator {
    Value eval(AST node)throws InterpreterException;
    Value eval(BoolType bool) throws InterpreterException;
    Value eval(StringType type) throws InterpreterException;
    Value eval(IntegerType number)throws InterpreterException;
    Value eval(RealType number)throws InterpreterException;
    Value eval(ConstantDeclaration constDec) throws InterpreterException;
    Value eval(UnaryOperation unaryOperation)throws InterpreterException;
    Value eval(BinaryOperation binaryOperation)throws InterpreterException;
    Value eval(Compound compound)throws InterpreterException;
    Value eval(Assignment assignment)throws InterpreterException;
    Value eval(Variable variable)throws InterpreterException;
    Value eval(Block block)throws InterpreterException;
    Value eval(Program program)throws InterpreterException;
    Value eval(FunctionCall call)throws InterpreterException;
    Value eval(Condition condition)throws InterpreterException;
    Value eval(IfElse ifElse)throws InterpreterException;
    Value eval(RepeatUntil repeatUntil)throws InterpreterException;
    Value eval(While whileLoop)throws InterpreterException;
    Value eval(For forLoop) throws InterpreterException;
}
