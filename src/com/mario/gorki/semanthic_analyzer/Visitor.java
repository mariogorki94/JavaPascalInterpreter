package com.mario.gorki.semanthic_analyzer;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Loop;
import com.mario.gorki.abstract_tree.data.Variable;
import com.mario.gorki.abstract_tree.data.VariableType;
import com.mario.gorki.abstract_tree.operations.*;
import com.mario.gorki.abstract_tree.types.BoolType;
import com.mario.gorki.abstract_tree.types.IntegerType;
import com.mario.gorki.abstract_tree.types.RealType;
import com.mario.gorki.abstract_tree.types.StringType;
import com.mario.gorki.exceptions.AnalyzerException;

public interface Visitor {
    void visit(AST node)throws AnalyzerException;
    void visit(BoolType bool) throws AnalyzerException;
    void visit(StringType type) throws AnalyzerException;
    void visit(IntegerType number)throws AnalyzerException;
    void visit(RealType number)throws AnalyzerException;
    void visit(UnaryOperation unaryOperation)throws AnalyzerException;
    void visit(BinaryOperation binaryOperation)throws AnalyzerException;
    void visit(Compound compound)throws AnalyzerException;
    void visit(Assignment assignment)throws AnalyzerException;
    void visit(Variable variable)throws AnalyzerException;
    void visit(NoOp noOp)throws AnalyzerException;
    void visit(Block block)throws AnalyzerException;
    void visit(VariableDeclaration variableDeclaration) throws AnalyzerException;
    void visit(ConstantDeclaration constantDeclaration) throws AnalyzerException;
    void visit(VariableType type)throws AnalyzerException;
    void visit(Program program)throws AnalyzerException;
    void visit(Procedure procedure)throws AnalyzerException;
    void visit(Function function)throws AnalyzerException;
    void visit(Param param)throws AnalyzerException;
    void visit(FunctionCall call)throws AnalyzerException;
    void visit(Condition condition)throws AnalyzerException;
    void visit(IfElse ifElse)throws AnalyzerException;
    void visit(Loop loop)throws AnalyzerException;
    void visit(For forLoop)throws AnalyzerException;
}
