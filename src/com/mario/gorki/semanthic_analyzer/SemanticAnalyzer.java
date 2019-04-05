package com.mario.gorki.semanthic_analyzer;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Declaration;
import com.mario.gorki.abstract_tree.Loop;
import com.mario.gorki.abstract_tree.data.Variable;
import com.mario.gorki.abstract_tree.data.VariableType;
import com.mario.gorki.abstract_tree.enums.Type;
import com.mario.gorki.abstract_tree.operations.*;
import com.mario.gorki.abstract_tree.types.BoolType;
import com.mario.gorki.abstract_tree.types.IntegerType;
import com.mario.gorki.abstract_tree.types.RealType;
import com.mario.gorki.abstract_tree.types.StringType;
import com.mario.gorki.exceptions.AnalyzerException;
import com.mario.gorki.semanthic_analyzer.symbols.*;
import com.mario.gorki.utils.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class SemanticAnalyzer implements Visitor {

    private ScopedSymbolTable currentScope;
    private HashMap<String, ScopedSymbolTable> scopes = new HashMap<>();

    public HashMap<String, ScopedSymbolTable> analyze(AST ast) throws AnalyzerException {
        Log.print("ANALYZING TREE...");
        visit(ast);
        return scopes;
    }

    @Override
    public void visit(AST node) throws AnalyzerException {
        if (node instanceof BoolType) {
            visit((BoolType) node);
            return;
        }
        if (node instanceof StringType) {
            visit((StringType) node);
            return;
        }
        if (node instanceof IntegerType) {
            visit((IntegerType) node);
            return;
        }
        if (node instanceof RealType) {
            visit((RealType) node);
            return;
        }
        if (node instanceof UnaryOperation) {
            visit((UnaryOperation) node);
            return;
        }
        if (node instanceof BinaryOperation) {
            visit((BinaryOperation) node);
            return;
        }
        if (node instanceof Compound) {
            visit((Compound) node);
            return;
        }
        if (node instanceof Assignment) {
            visit((Assignment) node);
            return;
        }
        if (node instanceof Variable) {
            visit((Variable) node);
            return;
        }
        if (node instanceof NoOp) {
            visit((NoOp) node);
            return;
        }
        if (node instanceof Block) {
            visit((Block) node);
            return;
        }
        if (node instanceof VariableDeclaration) {
            visit((VariableDeclaration) node);
            return;
        }
        if (node instanceof ConstantDeclaration) {
            visit((ConstantDeclaration) node);
            return;
        }
        if (node instanceof VariableType) {
            visit((VariableType) node);
            return;
        }
        if (node instanceof Program) {
            visit((Program) node);
            return;
        }
        if (node instanceof Function) {
            visit((Function) node);
            return;
        }
        if (node instanceof Procedure) {
            visit((Procedure) node);
            return;
        }
        if (node instanceof Param) {
            visit((Param) node);
            return;
        }
        if (node instanceof FunctionCall) {
            visit((FunctionCall) node);
            return;
        }
        if (node instanceof Condition) {
            visit((Condition) node);
            return;
        }
        if (node instanceof IfElse) {
            visit((IfElse) node);
            return;
        }
        if (node instanceof Loop) {
            visit((Loop) node);
            return;
        }
        if (node instanceof For) {
            visit((For) node);
            return;
        }


        throw new AnalyzerException("Unsupported node type " + node.toString());
    }

    @Override
    public void visit(BoolType bool) throws AnalyzerException {

    }

    @Override
    public void visit(StringType type) throws AnalyzerException {

    }

    @Override
    public void visit(IntegerType number) {

    }

    @Override
    public void visit(RealType number) {

    }

    @Override
    public void visit(UnaryOperation unaryOperation) throws AnalyzerException {
        visit(unaryOperation.operand);
    }

    @Override
    public void visit(BinaryOperation binaryOperation) throws AnalyzerException {
        visit(binaryOperation.left);
        visit(binaryOperation.right);
    }

    @Override
    public void visit(Compound compound) throws AnalyzerException {
        for (AST node : compound.children)
            visit(node);
    }

    @Override
    public void visit(Assignment assignment) throws AnalyzerException {
        visit(assignment.left);
        visit(assignment.right);
    }

    @Override
    public void visit(Variable variable) throws AnalyzerException {
        if (currentScope == null)
            throw new AnalyzerException("Cannot access a variable outside scope");
        if (currentScope.lookup(variable.name) == null)
            throw new AnalyzerException("Identifier nor found: " + variable.name);
    }

    @Override
    public void visit(NoOp noOp) {

    }

    @Override
    public void visit(Block block) throws AnalyzerException {
        for (Declaration d : block.declarations)
            visit(d);

        visit(block.compound);
    }

    @Override
    public void visit(VariableDeclaration variableDeclaration) throws AnalyzerException {
        if (currentScope == null)
            throw new AnalyzerException("Cannot declare a variable outside scope");

        if (currentScope.lookup(variableDeclaration.variable.name, true) != null)
            throw new AnalyzerException("Identifier with name " + variableDeclaration.variable.name + " already exists in current scope");

        Symbol type = currentScope.lookup(variableDeclaration.type.type.toString());

        if (type == null)
            throw new AnalyzerException("Type not found " + variableDeclaration.type.type.toString());

        if (variableDeclaration instanceof ArrayDeclaration)
            currentScope.insert(new ArraySymbol(variableDeclaration.variable.name, type,
                    ((ArrayDeclaration) variableDeclaration).startIndex, ((ArrayDeclaration) variableDeclaration).endIndex));
        else
            currentScope.insert(new VariableSymbol(variableDeclaration.variable.name, type));
    }

    @Override
    public void visit(ConstantDeclaration constantDeclaration) throws AnalyzerException {
        if (currentScope == null)
            throw new AnalyzerException("Cannot declare constant outside scope");

        if (currentScope.lookup(constantDeclaration.name, true) != null)
            throw new AnalyzerException("Identifier with name " + constantDeclaration.name + " already exists in current scope");

        currentScope.insert(new ConstantSymbol(constantDeclaration.name));
    }

    @Override
    public void visit(VariableType type) {

    }

    @Override
    public void visit(Program program) throws AnalyzerException {
        ScopedSymbolTable globalScope = new ScopedSymbolTable("global", 1, null);
        scopes.put(globalScope.getName(), globalScope);
        currentScope = globalScope;
        visit(program.block);
        currentScope = null;
    }

    @Override
    public void visit(Procedure procedure) throws AnalyzerException {
        ScopedSymbolTable scope = new ScopedSymbolTable(procedure.name, currentScope != null ? currentScope.getLevel() + 1 : 1, currentScope);
        scopes.put(scope.getName(), scope);
        currentScope = scope;

        ArrayList<Symbol> parameters = new ArrayList<>();
        for (Param param : procedure.params) {
            Symbol type = scope.lookup(param.type.type.toString());
            if (type == null)
                throw new AnalyzerException("Type not found " + param.type.type.toString());

            VariableSymbol variable = new VariableSymbol(param.name, type);
            parameters.add(variable);
            scope.insert(variable);
        }

        if (procedure instanceof Function) {
            Symbol type = scope.lookup(((Function) procedure).returnType.type.toString());
            if (type == null)
                throw new AnalyzerException("Type not found " + ((Function) procedure).returnType.type.toString());
            FunctionSymbol fn = new FunctionSymbol(procedure.name, parameters, procedure, type);
            scope.getEnclosingScope().insert(fn);
        } else {
            ProcedureSymbol proc = new ProcedureSymbol(procedure.name, parameters, procedure);
            scope.getEnclosingScope().insert(proc);
        }

        visit(procedure.block);
        currentScope = currentScope.getEnclosingScope();
    }

    @Override
    public void visit(Function function) throws AnalyzerException {
        visit((Procedure) function);
    }

    @Override
    public void visit(Param param) {
        visit(param.type);
    }

    @Override
    public void visit(FunctionCall call) throws AnalyzerException {
        Symbol symbol = currentScope.lookup(call.name);

        if (symbol == null)
            throw new AnalyzerException("Symbol(procedure/function) not found " + call.name);

        if (symbol instanceof ProcedureSymbol) {
            checkFunction(call, (ProcedureSymbol) symbol);
            return;
        }
        if (symbol instanceof BuiltInProcedureSymbol) {
            checkBuiltInProcedure(call, (BuiltInProcedureSymbol) symbol);
            return;
        }

        throw new AnalyzerException("Symbol " + symbol.getName() + " is not procedure or function");
    }

    @Override
    public void visit(Condition condition) throws AnalyzerException {
        visit(condition.leftSide);
        visit(condition.rightSide);
    }

    @Override
    public void visit(IfElse ifElse) throws AnalyzerException {
        visit(ifElse.condition);
        visit(ifElse.trueExpression);

        if (ifElse.falseExpression != null)
            visit(ifElse.falseExpression);
    }

    @Override
    public void visit(Loop loop) throws AnalyzerException {
        visit(loop.statement);
        visit(loop.condition);
    }

    @Override
    public void visit(For forLoop) throws AnalyzerException {
        if (currentScope != null)
            currentScope.insert(new VariableSymbol(forLoop.variable.name, currentScope.lookup("integer")));

        visit(forLoop.startValue);
        visit(forLoop.endValue);
        visit(forLoop.statement);
    }

    private void checkBuiltInProcedure(FunctionCall call, BuiltInProcedureSymbol symbol) {
    }

    private void checkFunction(FunctionCall call, ProcedureSymbol procedure) throws AnalyzerException {
        if (procedure.params.size() != call.actualParamters.size()) {
            throw new AnalyzerException("Procedure called with wrong number of parameters " + call.name);
        }

        if (procedure.params.size() == 0)
            return;

        for (int i = 0; i < procedure.params.size(); i++) {
            Symbol varSymbol = procedure.params.get(i);
            if (varSymbol instanceof VariableSymbol) {
                if (!(((VariableSymbol) varSymbol).type instanceof BuiltInTypeSymbol))
                    throw new AnalyzerException("Procedure declared with wrong parameters " + call.name);
            }
        }
        //TODO MAYBE IMPLEMENT TYPE CHECK
    }
}
