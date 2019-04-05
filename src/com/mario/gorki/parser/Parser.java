package com.mario.gorki.parser;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Declaration;
import com.mario.gorki.abstract_tree.data.ArrayVariable;
import com.mario.gorki.abstract_tree.data.Variable;
import com.mario.gorki.abstract_tree.data.VariableType;
import com.mario.gorki.abstract_tree.enums.BinaryOperationType;
import com.mario.gorki.abstract_tree.enums.ConditionType;
import com.mario.gorki.abstract_tree.enums.Type;
import com.mario.gorki.abstract_tree.enums.UnaryOperationType;
import com.mario.gorki.abstract_tree.operations.*;
import com.mario.gorki.abstract_tree.types.BoolType;
import com.mario.gorki.abstract_tree.types.IntegerType;
import com.mario.gorki.abstract_tree.types.RealType;
import com.mario.gorki.abstract_tree.types.StringType;
import com.mario.gorki.exceptions.LexerException;
import com.mario.gorki.exceptions.ParserException;
import com.mario.gorki.lexer.Lexer;
import com.mario.gorki.tokens.Token;
import com.mario.gorki.tokens.TokenType;
import com.mario.gorki.utils.Log;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;

import static com.mario.gorki.tokens.TokenType.*;

public class Parser {

    private int tokenIndex = 0;
    private ArrayList<Token> tokens;

    public AST parse(ArrayList<Token> tokens) throws ParserException {
        Log.print("PARSING...");
        this.tokens = tokens;
        AST node = program();
        if (currentToken().getType() != EOF)
            throw new ParserException("EOF expected", currentToken().getLineNumber());

        return node;
    }

    private Token currentToken() {
        return tokens.get(tokenIndex);
    }

    private Token nextToken() {
        return tokens.get(tokenIndex + 1);
    }

    private void eat(TokenType t) throws ParserException {
        if (currentToken().getType() == t)
            tokenIndex += 1;
        else
            throw new ParserException("Parser error, expected " + t.toString() + " ,got " + currentToken().getType().toString(), currentToken().getLineNumber());
    }

    /*
     program : PROGRAM variable SEMI block DOT
     */
    private Program program() throws ParserException {
        eat(PROGRAM);
        if (currentToken().getType() != ID || currentToken().getValue() == null)
            throw new ParserException("Program must have a name", currentToken().getLineNumber());

        String name = (String) currentToken().getValue();
        eat(ID);
        eat(SEMI);
        Block blockNode = block();
        Program programNode = new Program(name, blockNode);
        eat(DOT);
        return programNode;
    }

    /*
     block : declarations compound_statement
     */
    private Block block() throws ParserException {
        ArrayList<Declaration> decs = declarations();
        Compound statement = compoundStatement();
        return new Block(decs, statement);
    }

    /*
     declarations :
       CONST (const_declaration SEMI)+
     | VAR (variable_declaration SEMI)+
     | (PROCEDURE ID (LPAREN formal_parameter_list RPAREN)? SEMI block SEMI)*
     | (FUNCTION ID (LPAREN formal_parameter_list RPAREN)? COLON type_spec SEMI block SEMI)*
     | empty
     */
    private ArrayList<Declaration> declarations() throws ParserException {
        ArrayList<Declaration> declarations = new ArrayList<>();

        if (currentToken().getType() == CONST_DEF) {
            eat(CONST_DEF);
            ArrayList<ConstantDeclaration> decs = new ArrayList<>();
            while (currentToken().getType() == ID) {
                String name = (String) currentToken().getValue();
                eat(ID);
                eat(EQUALS);
                AST expr = expr();
                decs.add(new ConstantDeclaration(name, expr));
                eat(SEMI);
            }

            declarations.addAll(decs);
        }

        if (currentToken().getType() == VAR_DEF) {
            eat(VAR_DEF);
            while (currentToken().getType() == ID) {
                ArrayList<VariableDeclaration> decs = variableDeclaration();
                declarations.addAll(decs);
                eat(SEMI);
            }
        }

        while (currentToken().getType() == PROCEDURE) {
            eat(PROCEDURE);
            if (currentToken().getType() != ID || currentToken().getValue() == null)
                throw new ParserException("Procedure name expected", currentToken().getLineNumber());

            String name = (String) currentToken().getValue();

            eat(ID);

            ArrayList<Param> params = new ArrayList<>();

            if (currentToken().getType() == PAREN_L) {
                eat(PAREN_L);
                params = formalParameterList();
                eat(PAREN_R);
            }
            eat(SEMI);

            Block body = block();
            Procedure procedure = new Procedure(name, params, body);
            declarations.add(procedure);
            eat(SEMI);
        }

        while (currentToken().getType() == FUNCTION) {
            eat(FUNCTION);
            if (currentToken().getType() != ID || currentToken().getValue() == null)
                throw new ParserException("Function name expected", currentToken().getLineNumber());
            String name = (String) currentToken().getValue();

            eat(ID);

            ArrayList<Param> params = new ArrayList<>();

            if (currentToken().getType() == PAREN_L) {
                eat(PAREN_L);
                params = formalParameterList();
                eat(PAREN_R);
            }

            eat(COLON);
            VariableType type = typeSpec();

            eat(SEMI);
            Block body = block();
            Function function = new Function(name, params, body, type);
            declarations.add(function);
            eat(SEMI);
        }

        return declarations;
    }

    /*
    formal_parameter_list : formal_parameters
     | formal_parameters SEMI formal_parameter_list
     */
    private ArrayList<Param> formalParameterList() throws ParserException {
        if (currentToken().getType() != ID)
            return new ArrayList<>(); //without params

        ArrayList<Param> parameters = formalParameters();
        while (currentToken().getType() == SEMI) {
            eat(SEMI);
            parameters.addAll(formalParameterList());
        }
        return parameters;
    }

    /*
       formal_parameters : ID (COMMA ID)* COLON type_spec
     */
    private ArrayList<Param> formalParameters() throws ParserException {
        if (currentToken().getType() != ID || currentToken().getValue() == null)
            throw new ParserException("Parameter name expected", currentToken().getLineNumber());

        ArrayList<String> parameters = new ArrayList<>();
        parameters.add((String) currentToken().getValue());
        eat(ID);

        while (currentToken().getType() == COMA) {
            eat(COMA);

            if (currentToken().getType() != ID || currentToken().getValue() == null)
                throw new ParserException("Parameter name expected", currentToken().getLineNumber());

            parameters.add((String) currentToken().getValue());
            eat(ID);
        }

        eat(COLON);
        VariableType type = typeSpec();
        ArrayList<Param> params = new ArrayList<>();
        for (String s : parameters) {
            params.add(new Param(s, type));
        }
        return params;
    }

    /*
      variable_declaration : ID (COMMA ID)* COLON type_spec
     | ID (COMMA ID)* COLON ARRAY LBRACKET startIndex DOT DOT endIdex RBRACKE OF type_spec
     */
    private <T extends VariableDeclaration> ArrayList<T> variableDeclaration() throws ParserException {
        if (currentToken().getType() != ID || currentToken().getValue() == null)
            throw new ParserException("Variable name expected", currentToken().getLineNumber());

        ArrayList<String> variableNames = new ArrayList<>();
        variableNames.add((String) currentToken().getValue());
        eat(ID);

        while (currentToken().getType() == COMA) {
            eat(COMA);
            if (currentToken().getType() != ID || currentToken().getValue() == null)
                throw new ParserException("Variable name expected", currentToken().getLineNumber());

            variableNames.add((String) currentToken().getValue());
            eat(ID);
        }

        eat(COLON);

        if (currentToken().getType() == ARRAY) {
            eat(ARRAY);
            eat(BRACKET_L);
            Integer startIndex = 0;
            Integer endIndex = 0;

            if (currentToken().getType() != CONST_INT)
                throw new ParserException("Start index of array expected", currentToken().getLineNumber());

            startIndex = (Integer) currentToken().getValue();
            eat(CONST_INT);

            eat(DOT);
            eat(DOT);

            if (currentToken().getType() != CONST_INT)
                throw new ParserException("End index of array expected", currentToken().getLineNumber());

            endIndex = (Integer) currentToken().getValue();
            eat(CONST_INT);

            eat(BRACKET_R);
            eat(OF);
            VariableType type = typeSpec();

            ArrayList<ArrayDeclaration> dec = new ArrayList<>();
            for (String s : variableNames) {
                dec.add(new ArrayDeclaration(new Variable(s), type, startIndex, endIndex));
            }

            return (ArrayList<T>) dec;
        } else {
            VariableType type = typeSpec();
            ArrayList<VariableDeclaration> dec = new ArrayList<>();
            for (String s : variableNames) {
                dec.add(new VariableDeclaration(new Variable(s), type));
            }
            return (ArrayList<T>) dec;
        }
    }

    /*
     type_spec : INTEGER
     | REAL
     | STRING
     | BOOLEAN
     */
    private VariableType typeSpec() throws ParserException {
        switch (currentToken().getType()) {
            case TYPE_INT:
                eat(TYPE_INT);
                return new VariableType(Type.INT);
            case TYPE_REAL:
                eat(TYPE_REAL);
                return new VariableType(Type.REAL);
            case TYPE_BOOL:
                eat(TYPE_BOOL);
                return new VariableType(Type.BOOL);
            case TYPE_STRING:
                eat(TYPE_STRING);
                return new VariableType(Type.STRING);
            default:
                throw new ParserException("Expected type token ", currentToken().getLineNumber());
        }
    }

    /*
       compound_statement: BEGIN statement_list END
     */
    private Compound compoundStatement() throws ParserException {
        eat(BEGIN);
        ArrayList<AST> nodes = statementList();
        eat(END);
        return new Compound(nodes);
    }

    /*
     statement_list : statement
     | statement SEMI statement_list
     */
    private ArrayList<AST> statementList() throws ParserException {
        ArrayList<AST> statements = new ArrayList<>();
        statements.add(statement());

        while (currentToken().getType() == SEMI) {
            eat(SEMI);
            statements.add(statement());
        }

        return statements;
    }

    /*
    statement : compound_statement
     | procedure_call
     | if_else_statement
     | assignment_statement
     | repeat_until
     | for_loop
     | while_loop
     | empty
     */
    private AST statement() throws ParserException {
        switch (currentToken().getType()) {
            case BEGIN:
                return compoundStatement();
            case IF:
                return ifElseStatement();
            case REPEAT:
                return repeatUntilLoop();
            case WHILE:
                return whileLoop();
            case FOR:
                return forLoop();
            case NO_OP:
                return empty();
            case ID:
                if (nextToken().getType() == PAREN_L)
                    return functionCall();
                else
                    return assignmentStatement();
            default:
                return empty();

        }
    }

    /*
    repeat_until : REPEAT statement UNTIL condition
     */
    private RepeatUntil repeatUntilLoop() throws ParserException {
        eat(REPEAT);

        ArrayList<AST> statements = new ArrayList<>();

        while (currentToken().getType() != UNTIL) {
            statements.add(statement());
            if (currentToken().getType() == SEMI) {
                eat(SEMI);
            }
        }
        eat(UNTIL);
        Condition c = condition();
        return new RepeatUntil(statements.size() == 1 ? statements.get(0) : new Compound(statements), c);
    }

    /*
     for_loop : WHILE condition DO statement
     */
    private While whileLoop() throws ParserException {
        eat(WHILE);
        Condition c = condition();
        eat(DO);
        AST s = statement();
        return new While(s, c);
    }

    /*
     for_loop : FOR variable ASSIGN expression TO expression DO statement
     */
    private For forLoop() throws ParserException {
        eat(FOR);
        Variable v = variable();
        eat(ASSIGN);
        AST start = expr();
        eat(TO);
        AST end = expr();
        eat(DO);
        AST s = statement();
        return new For(s, v, start, end);
    }

    /*
     function_call : id LPAREN (factor (factor COLON)* )* RPAREN
     */
    private FunctionCall functionCall() throws ParserException {
        if (currentToken().getType() != ID || currentToken().getValue() == null)
            throw new ParserException("Function/Procedure name expected", currentToken().getLineNumber());

        String name = (String) currentToken().getValue();
        ArrayList<AST> parameters = new ArrayList<>();

        eat(ID);
        eat(PAREN_L);
        if (currentToken().getType() == PAREN_R) { // no parameters
            eat(PAREN_R);
        } else {
            parameters.add(expr());
            while (currentToken().getType() == COMA) {
                eat(COMA);
                parameters.add(factor());
            }
            eat(PAREN_R);
        }

        return new FunctionCall(name, parameters);
    }

    /*
     if_else_statement : IF condition statement
         | IF condition THEN statement ELSE statement
     */
    private IfElse ifElseStatement() throws ParserException {
        eat(IF);
        Condition cond = condition();
        eat(THEN);
        AST trueStatement = statement();
        AST falseStatement = null;
        if (currentToken().getType() == ELSE) {
            eat(ELSE);
            falseStatement = statement();
        }
        return new IfElse(cond, trueStatement, falseStatement);
    }

    /*
     condition: expr (= | < | >) expr
     | LPAREN expr (= | < | >) expr RPAREN
     */
    private Condition condition() throws ParserException {
        if (currentToken().getType() == PAREN_L)
            eat(PAREN_L);

        AST left = expr();
        ConditionType type;
        switch (currentToken().getType()) {
            case EQUALS:
                eat(EQUALS);
                type = ConditionType.EQUALS;
                break;
            case LESS_THAN:
                eat(LESS_THAN);
                type = ConditionType.LESS_THAN;
                break;
            case GREATER_THAN:
                eat(GREATER_THAN);
                type = ConditionType.GREATER_THAN;
                break;
            default:
                throw new ParserException("Invalid condition type", currentToken().getLineNumber());
        }
        AST right = expr();
        if (currentToken().getType() == PAREN_R)
            eat(PAREN_R);

        return new Condition(type, left, right);
    }

    /*
     assignment_statement : variable ASSIGN expr
     */
    private Assignment assignmentStatement() throws ParserException {
        Variable left = variable();
        eat(ASSIGN);
        AST right = expr();
        return new Assignment(left, right);
    }

    /*
    An empty production
     */
    private NoOp empty() throws ParserException {
        if (currentToken().getType() == TokenType.NO_OP){
            eat(NO_OP);
            eat(NO_OP);
        }


        return new NoOp();
    }

    /*
     expr: term ((PLUS | MINUS) term)*
     */
    private AST expr() throws ParserException {
        AST node = term();

        while (currentToken().getType() == PLUS || currentToken().getType() == MINUS) {
            TokenType t = currentToken().getType();
            eat(t);
            BinaryOperationType bt = t == PLUS ? BinaryOperationType.PLUS : BinaryOperationType.MINUS;
            node = new BinaryOperation(node, bt, term());
        }

        return node;
    }

    /*
    term : factor ((MUL | INTEGER_DIV | FLOAT_DIV) factor)*
     */
    private AST term() throws ParserException {
        AST node = factor();

        while (currentToken().getType() == MULT || currentToken().getType() == INTEGER_DIV || currentToken().getType() == FLOAT_DIV) {
            TokenType t = currentToken().getType();
            eat(t);
            BinaryOperationType bt = null;
            switch (t) {
                case MULT:
                    bt = BinaryOperationType.MULT;
                    break;
                case INTEGER_DIV:
                    bt = BinaryOperationType.INTEGER_DIV;
                    break;
                case FLOAT_DIV:
                    bt = BinaryOperationType.FLOAT_DIV;
                    break;
            }
            node = new BinaryOperation(node, bt, factor());
        }

        return node;
    }

    /*
     variable : ID
     */
    private Variable variable() throws ParserException {
        if (currentToken().getType() != ID || currentToken().getValue() == null)
            throw new ParserException("Syntax error, expected variable", currentToken().getLineNumber());

        String name = (String) currentToken().getValue();
        eat(ID);
        if (currentToken().getType() == BRACKET_L) {
            eat(BRACKET_L);
            AST index = expr();
            eat(BRACKET_R);
            return new ArrayVariable(name, index);
        }

        return new Variable(name);
    }

    /*
     factor : PLUS factor
     | MINUS factor
     | INTEGER_CONST
     | REAL_CONST
     | STRING_CONST
     | LPAREN expr RPAREN
     | variable
     | function_call
     */
    private AST factor() throws ParserException {
        switch (currentToken().getType()) {
            case PLUS:
                eat(PLUS);
                return new UnaryOperation(UnaryOperationType.PLUS, factor());
            case MINUS:
                eat(MINUS);
                return new UnaryOperation(UnaryOperationType.MINUS, factor());
            case CONST_INT:
                Integer value = (Integer) currentToken().getValue();
                eat(CONST_INT);
                return new IntegerType(value);
            case CONST_REAL:
                Double d = (Double) currentToken().getValue();
                eat(CONST_REAL);
                return new RealType(d);
            case PAREN_L:
                eat(PAREN_L);
                AST res = expr();
                eat(PAREN_R);
                return res;
            case APOSTROPHE:
                eat(APOSTROPHE);
                if (currentToken().getType() != CONST_STRING)
                    throw new ParserException("Expected string literal after '", currentToken().getLineNumber());
                String sd = (String) currentToken().getValue();
                eat(CONST_STRING);
                eat(APOSTROPHE);
                return new StringType(sd);
            case CONST_BOOL:
                Boolean bd = (Boolean) currentToken().getValue();
                eat(CONST_BOOL);
                return new BoolType(bd);
            default:
                if (nextToken().getType() == PAREN_L)
                    return functionCall();
                else
                    return variable();
        }
    }
}
