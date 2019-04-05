package com.mario.gorki.lexer;

import com.mario.gorki.exceptions.LexerException;
import com.mario.gorki.tokens.Token;
import com.mario.gorki.tokens.TokenType;
import com.mario.gorki.utils.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.mario.gorki.tokens.TokenType.EOF;

public class Lexer {
    private HashMap<String, Token> keywords = new HashMap<>();
    private Character currentChar;
    private LineNumberReader reader;
    private boolean isStringStart;
    private boolean wasStringLast;

    public Lexer(Reader reader) {
        this.reader = new LineNumberReader(reader);
        init();
    }

    public Lexer(InputStream stream) {

        reader = new LineNumberReader(new InputStreamReader(stream));
        init();
    }

    public ArrayList<Token> lex() throws LexerException {
        System.out.println("LEXING...");
        ArrayList<Token> tokens = new ArrayList<>();
        Token token = getNextToken();
        tokens.add(token);
        while (token.getType() != EOF) {
            token = getNextToken();
            tokens.add(token);
        }

        return tokens;
    }

    private void init() {
        advance();

        keywords.put("program", new Token(TokenType.PROGRAM));
        keywords.put("var", new Token(TokenType.VAR_DEF));
        keywords.put("const", new Token(TokenType.CONST_DEF));
        keywords.put("div", new Token(TokenType.INTEGER_DIV));
        keywords.put("integer", new Token(TokenType.TYPE_INT));
        keywords.put("real", new Token(TokenType.TYPE_REAL));
        keywords.put("boolean", new Token(TokenType.TYPE_BOOL));
        keywords.put("string", new Token(TokenType.TYPE_STRING));
        keywords.put("begin", new Token(TokenType.BEGIN));
        keywords.put("end", new Token(TokenType.END));
        keywords.put("procedure", new Token(TokenType.PROCEDURE));
        keywords.put("true", new Token(TokenType.CONST_BOOL, true));
        keywords.put("false", new Token(TokenType.CONST_BOOL, false));
        keywords.put("if", new Token(TokenType.IF));
        keywords.put("else", new Token(TokenType.ELSE));
        keywords.put("then", new Token(TokenType.THEN));
        keywords.put("function", new Token(TokenType.FUNCTION));
        keywords.put("repeat", new Token(TokenType.REPEAT));
        keywords.put("until", new Token(TokenType.UNTIL));
        keywords.put("for", new Token(TokenType.FOR));
        keywords.put("to", new Token(TokenType.TO));
        keywords.put("do", new Token(TokenType.DO));
        keywords.put("while", new Token(TokenType.WHILE));
        keywords.put("of", new Token(TokenType.OF));
        keywords.put("array", new Token(TokenType.ARRAY));
        keywords.put("nop", new Token(TokenType.NO_OP));
    }


    private void skipWhiteSpace() {
        while (currentChar != null && Character.isWhitespace(currentChar))
            advance();
    }

    private void skipComments() {
        while (currentChar != null && currentChar != '}')
            advance();

        advance();
    }

    private void advance() {
        try {
            int data = reader.read();
            currentChar = data == -1 ? null : (char) data;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Character peek() {
        try {
            reader.mark(5);
            int data = reader.read();
            Character c = data == -1 ? null : (char) data;
            reader.reset();
            return c;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Token getNumber() {
        StringBuilder builder = new StringBuilder();
        while (currentChar != null && Character.isDigit(currentChar)) {
            builder.append(currentChar);
            advance();
        }

        if (currentChar != null && currentChar == '.' && peek() != null && peek() != '.') {
            builder.append('.');
            advance();

            while (currentChar != null && Character.isDigit(currentChar)) {
                builder.append(currentChar);
                advance();
            }

            return new Token(TokenType.CONST_REAL, Double.valueOf(builder.toString()), getLineNumber());
        }

        return new Token(TokenType.CONST_INT, Integer.valueOf(builder.toString()));
    }

    private Token getId() {
        StringBuilder builder = new StringBuilder();

        while (currentChar != null && Character.isLetterOrDigit(currentChar)) {
            builder.append(currentChar);
            advance();
        }

        String value = builder.toString();
        if (keywords.containsKey(value.toLowerCase())) {
            Token t = keywords.get(value.toLowerCase());
            return new Token(t.getType(), t.getValue(), getLineNumber());
        }

        return new Token(TokenType.ID, value, getLineNumber());
    }

    private Token getString() {
        StringBuilder builder = new StringBuilder();
        while (currentChar != null && currentChar != '\'') {
            builder.append(currentChar);
            advance();
        }

        return new Token(TokenType.CONST_STRING, builder.toString(), getLineNumber());
    }

    private int getLineNumber() {
        return reader.getLineNumber() + 1;
    }

    private Token getNextToken() throws LexerException {
        while (currentChar != null) {

            if (wasStringLast) {
                wasStringLast = false;
                if (currentChar == '\'')
                    advance();

                return new Token(TokenType.APOSTROPHE, getLineNumber());
            }

            if (isStringStart) {
                wasStringLast = true;
                isStringStart = false;
                return getString();
            }

            if (Character.isWhitespace(currentChar)) {
                skipWhiteSpace();
                continue;
            }

            if (currentChar == '{') {
                advance();
                skipComments();
                continue;
            }

            if (currentChar == '\'') {
                advance();
                isStringStart = !isStringStart;
                return new Token(TokenType.APOSTROPHE, getLineNumber());
            }

            if (Character.isDigit(currentChar))
                return getNumber();

            if (Character.isLetterOrDigit(currentChar))
                return getId();

            if (currentChar == ':' && peek() != null && peek() == '=') {
                advance();
                advance();
                return new Token(TokenType.ASSIGN, getLineNumber());
            }

            if (currentChar == '.') {
                advance();
                return new Token(TokenType.DOT, getLineNumber());
            }

            if (currentChar == ',') {
                advance();
                return new Token(TokenType.COMA, getLineNumber());
            }

            if (currentChar == ';') {
                advance();
                return new Token(TokenType.SEMI, getLineNumber());
            }

            if (currentChar == ':') {
                advance();
                return new Token(TokenType.COLON, getLineNumber());
            }

            if (currentChar == '+') {
                advance();
                return new Token(TokenType.PLUS, getLineNumber());
            }

            if (currentChar == '-') {
                advance();
                return new Token(TokenType.MINUS, getLineNumber());
            }

            if (currentChar == '*') {
                advance();
                return new Token(TokenType.MULT, getLineNumber());
            }

            if (currentChar == '/') {
                advance();
                return new Token(TokenType.FLOAT_DIV, getLineNumber());
            }

            if (currentChar == '(') {
                advance();
                return new Token(TokenType.PAREN_L, getLineNumber());
            }

            if (currentChar == ')') {
                advance();
                return new Token(TokenType.PAREN_R, getLineNumber());
            }

            if (currentChar == '[') {
                advance();
                return new Token(TokenType.BRACKET_L, getLineNumber());
            }

            if (currentChar == ']') {
                advance();
                return new Token(TokenType.BRACKET_R, getLineNumber());
            }

            if (currentChar == '=') {
                advance();
                return new Token(TokenType.EQUALS, getLineNumber());
            }

            if (currentChar == '>') {
                advance();
                return new Token(TokenType.GREATER_THAN, getLineNumber());
            }

            if (currentChar == '<') {
                advance();
                return new Token(TokenType.LESS_THAN, getLineNumber());
            }

            throw new LexerException("Unrecognized character " + currentChar, getLineNumber());
        }

        return new Token(TokenType.EOF, getLineNumber());
    }
}
