package com.mario.gorki.interpreter;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Value;
import com.mario.gorki.abstract_tree.data.ArrayVariable;
import com.mario.gorki.abstract_tree.data.Variable;
import com.mario.gorki.abstract_tree.operations.*;
import com.mario.gorki.abstract_tree.types.*;
import com.mario.gorki.exceptions.InterpreterException;
import com.mario.gorki.semanthic_analyzer.ScopedSymbolTable;
import com.mario.gorki.semanthic_analyzer.Symbol;
import com.mario.gorki.semanthic_analyzer.symbols.*;
import com.mario.gorki.utils.Log;

import java.util.*;

public class Interpreter implements Evaluator {

    private Stack<Frame> callStack = new Stack<>();
    private AST tree;
    private HashMap<String, ScopedSymbolTable> scopes;

    public void interpret(AST tree, HashMap<String, ScopedSymbolTable> scopes) throws InterpreterException {
        Log.print("PROGRAM START...");
        this.tree = tree;
        this.scopes = scopes;
        eval(tree);
    }

    @Override
    public Value eval(AST node) throws InterpreterException {
        if (node instanceof BoolType)
            return eval((BoolType) node);
        if (node instanceof StringType)
            return eval((StringType) node);
        if (node instanceof IntegerType)
            return eval((IntegerType) node);
        if (node instanceof RealType)
            return eval((RealType) node);
        if (node instanceof ConstantDeclaration)
            return eval((ConstantDeclaration) node);
        if (node instanceof UnaryOperation)
            return eval((UnaryOperation) node);
        if (node instanceof BinaryOperation)
            return eval((BinaryOperation) node);
        if (node instanceof Compound)
            return eval((Compound) node);
        if (node instanceof Assignment)
            return eval((Assignment) node);
        if (node instanceof Variable)
            return eval((Variable) node);
        if (node instanceof Block)
            return eval((Block) node);
        if (node instanceof Program)
            return eval((Program) node);
        if (node instanceof FunctionCall)
            return eval((FunctionCall) node);
        if (node instanceof Condition)
            return eval((Condition) node);
        if (node instanceof IfElse)
            return eval((IfElse) node);
        if (node instanceof RepeatUntil)
            return eval((RepeatUntil) node);
        if (node instanceof While)
            return eval((While) node);
        if (node instanceof For)
            return eval((For) node);

        return null;
    }

    @Override
    public Value eval(BoolType bool) throws InterpreterException {
        return bool;
    }

    @Override
    public Value eval(StringType type) throws InterpreterException {
        return type;
    }

    @Override
    public Value eval(IntegerType number) throws InterpreterException {
        return number;
    }

    @Override
    public Value eval(RealType number) throws InterpreterException {
        return number;
    }

    @Override
    public Value eval(ConstantDeclaration constDec) throws InterpreterException {
        Frame currentFrame = callStack.peek();
        if (currentFrame == null)
            throw new InterpreterException("No call stack frame");

        currentFrame.setConstant(constDec.name, eval(constDec.value));

        return null;
    }

    @Override
    public Value eval(UnaryOperation unaryOperation) throws InterpreterException {
        Value v = eval(unaryOperation.operand);
        if (v instanceof IntegerType) {
            switch (unaryOperation.operation) {
                case PLUS:
                    return new IntegerType(+((IntegerType) v).getNumber());
                case MINUS:
                    return new IntegerType(-((IntegerType) v).getNumber());
            }
        }
        if (v instanceof RealType) {
            switch (unaryOperation.operation) {
                case PLUS:
                    return new RealType(+((RealType) v).getNumber());
                case MINUS:
                    return new RealType(-((RealType) v).getNumber());
            }
        }

        throw new InterpreterException("Unary operation can be applied only on numbers");
    }

    @Override
    public Value eval(BinaryOperation binaryOperation) throws InterpreterException {
        Value left = eval(binaryOperation.left);
        Value right = eval(binaryOperation.right);
        switch (binaryOperation.operation) {
            case PLUS:
                return left.plus(right);
            case MINUS:
                return left.minus(right);
            case MULT:
                return left.multiplication(right);
            case FLOAT_DIV:
                return left.floatDivision(right);
            case INTEGER_DIV:
                return left.integerDivision(right);
        }

        throw new InterpreterException("Interpreter does not support binary operation " + binaryOperation.operation.toString());
    }

    @Override
    public Value eval(Compound compound) throws InterpreterException {
        for (AST child : compound.children)
            eval(child);

        return null;
    }

    @Override
    public Value eval(Assignment assignment) throws InterpreterException {
        Frame currentFrame = callStack.peek();
        if (currentFrame == null)
            throw new InterpreterException("No call stack frame");

        if (assignment.left instanceof ArrayVariable) {
            Value v = eval(((ArrayVariable) assignment.left).index);
            if (!(v instanceof IntegerType))
                throw new InterpreterException("Cannot use non-Integer index with array " + assignment.left.name);

            currentFrame.set(assignment.left.name, eval(assignment.right), ((IntegerType) v).getNumber());
        }

        currentFrame.set(assignment.left.name, eval(assignment.right));

        return null;
    }

    @Override
    public Value eval(Variable variable) throws InterpreterException {
        Frame currentFrame = callStack.peek();
        if (currentFrame == null)
            throw new InterpreterException("No call stack frame");

        if (variable instanceof ArrayVariable) {
            Value v = eval(((ArrayVariable) variable).index);
            if (!(v instanceof IntegerType))
                throw new InterpreterException("Cannot use non-Integer index with array " + variable.name);

            return currentFrame.getValue(variable.name, ((IntegerType) v).getNumber());
        }
        return currentFrame.getValue(variable.name);
    }

    @Override
    public Value eval(Block block) throws InterpreterException {
        for (AST declaration : block.declarations)
            eval(declaration);

        return eval(block.compound);
    }

    @Override
    public Value eval(Program program) throws InterpreterException {
        Frame frame = new Frame(scopes.get("global"), null);
        callStack.push(frame);
        return eval(program.block);
    }

    @Override
    public Value eval(FunctionCall call) throws InterpreterException {
        Frame current = callStack.peek();

        Symbol s = current.getScope().lookup(call.name);
        if (s != null && s instanceof ProcedureSymbol)
            return callFunction(call.name, call.actualParamters, current);
        else
            return callBuiltInProcedure(call.name, call.actualParamters, current);
    }

    @Override
    public Value eval(Condition condition) throws InterpreterException {
        Value left = eval(condition.leftSide);
        Value right = eval(condition.rightSide);

        switch (condition.conditionType) {
            case EQUALS:
                return new BoolType(left.equals(right));
            case GREATER_THAN:
                return new BoolType(left.greaterThan(right));
            case LESS_THAN:
                return new BoolType(left.lessThan(right));
        }

        throw new InterpreterException("Interpreter does not support " + condition.conditionType.toString());
    }

    @Override
    public Value eval(IfElse ifElse) throws InterpreterException {
        Value value = eval(ifElse.condition);

        if (!(value instanceof BoolType))
            throw new InterpreterException("Condition not boolean");

        if (((BoolType) value).getBool())
            return eval(ifElse.trueExpression);

        if (ifElse.falseExpression != null)
            return eval(ifElse.falseExpression);

        return null;
    }

    @Override
    public Value eval(RepeatUntil repeatUntil) throws InterpreterException {
        eval(repeatUntil.statement);

        Value value = eval(repeatUntil.condition);

        while (value instanceof BoolType && !((BoolType) value).getBool()) {
            eval(repeatUntil.statement);
            value = eval(repeatUntil.condition);
        }

        return null;
    }

    @Override
    public Value eval(While whileLoop) throws InterpreterException {
        Value value = eval(whileLoop.condition);

        while (value instanceof BoolType && ((BoolType) value).getBool() == true)
            eval(whileLoop.statement);

        return null;
    }

    @Override
    public Value eval(For forLoop) throws InterpreterException {
        Frame currentFrame = callStack.peek();
        if (currentFrame == null)
            throw new InterpreterException("No call stack frame");

        Value start = eval(forLoop.startValue);
        Value end = eval(forLoop.endValue);

        if (!(start instanceof IntegerType) || !(end instanceof IntegerType))
            throw new InterpreterException("Cannot do a for loop on non integer values");

        for (int i = ((IntegerType) start).getNumber(); i <= ((IntegerType) end).getNumber(); i++) {
            currentFrame.set(forLoop.variable.name, new IntegerType(i));
            eval(forLoop.statement);
        }

        currentFrame.remove(forLoop.variable.name);

        return null;
    }

    private Value callFunction(String function, ArrayList<AST> params, Frame frame) throws InterpreterException {
        Symbol s = frame.getScope().lookup(function);
        if (s == null || !(s instanceof ProcedureSymbol))
            throw new InterpreterException("Symbol procedure not found " + function);

        ScopedSymbolTable oldScope = scopes.get(function);
        ScopedSymbolTable newScope = frame.getScope().getLevel() == 1 ? oldScope : new ScopedSymbolTable(function, oldScope.getLevel() + 1, oldScope);
        Frame newFrame = new Frame(newScope, frame);

        if (((ProcedureSymbol) s).params.size() > 0)
            for (int i = 0; i < params.size(); i++) {
                Value eval = eval(params.get(i));
                newFrame.set(((ProcedureSymbol) s).params.get(i).getName(), eval);
            }

        callStack.push(newFrame);
        eval(((ProcedureSymbol) s).body.block);
        callStack.pop();

        return newFrame.getReturnValue();
    }

    /**
     * BUILT IN PROCEDURES
     */

    private Value callBuiltInProcedure(String procedure, ArrayList<AST> params, Frame frame) throws InterpreterException {
        switch (procedure.toLowerCase()) {
            case "write":
                write(params, false);
                return null;
            case "writeln":
                write(params, true);
                return null;
            case "read":
                read(params, frame);
                return null;
            case "readln":
                read(params, frame);
                return null;
            case "random":
                return random(params);
            case "length":
                return length(params, frame);
        }

        throw new InterpreterException("Built in procedure not found " + procedure);
    }

    private Value length(ArrayList<AST> params, Frame frame) throws InterpreterException {
        if (params.size() != 1)
            throw new InterpreterException("Length called with invalid parameters");

        AST first = params.get(0);

        if (!(first instanceof Variable))
            throw new InterpreterException("Length called with invalid parameters");

        Symbol symb = frame.getScope().lookup(((Variable) first).name);

        if (symb == null || !(symb instanceof ArraySymbol))
            throw new InterpreterException("Length called with invalid parameters");

        return new IntegerType(((ArraySymbol) symb).endIndex - ((ArraySymbol) symb).startIndex + 1);
    }

    private Value random(ArrayList<AST> params) throws InterpreterException {
        if (params.size() != 1)
            throw new InterpreterException("Random called with invalid parameters");

        AST first = params.get(0);
        Value v = eval(first);
        if (!(v instanceof IntegerType))
            throw new InterpreterException("Random called with invalid parameters");

        Random r = new Random(System.currentTimeMillis());
        int i = r.nextInt(((IntegerType) v).getNumber());
        return new IntegerType(i);
    }

    private void write(ArrayList<AST> params, boolean newLine) throws InterpreterException {
        StringBuilder builder = new StringBuilder();
        for (AST param : params) {
            Value v = eval(param);

            if (v == null)
                throw new InterpreterException("Cannot use write with expression without value");

            if (v instanceof AST)
                builder.append((((AST) v).getValue()));
        }

        if (newLine)
            builder.append("\n");

        System.out.print(builder.toString());
    }

    private void read(ArrayList<AST> params, Frame frame) throws InterpreterException {
        Scanner scan = new Scanner(System.in);
        String line = scan.next();

        if (line == null)
            throw new InterpreterException("Empty input");

        String[] parts = line.split(" ");

        for (int i = 0; i < params.size(); i++) {
            AST param = params.get(i);
            if (!(param instanceof Variable))
                throw new InterpreterException("READ parameter must be a variable");

            Symbol symbol = frame.getScope().lookup(((Variable) param).name);

            if (symbol == null || !(symbol instanceof VariableSymbol) || !(((VariableSymbol) symbol).type instanceof BuiltInTypeSymbol))
                throw new InterpreterException("Symbol variable not found " + ((Variable) param).name);

            switch (((BuiltInTypeSymbol) ((VariableSymbol) symbol).type).type) {
                case INT:
                    frame.set(((Variable) param).name, new IntegerType(Integer.valueOf(parts[i])));
                    break;
                case BOOL:
                    frame.set(((Variable) param).name, new BoolType(Boolean.valueOf(parts[i])));
                    break;
                case REAL:
                    frame.set(((Variable) param).name, new RealType(Double.valueOf(parts[i])));
                    break;
                case STRING:
                    frame.set(((Variable) param).name, new StringType(parts[i]));
                    break;
            }
        }
    }


}
