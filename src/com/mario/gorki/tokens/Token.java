package com.mario.gorki.tokens;

public class Token {
    private int lineNumber;
    private TokenType type;
    private Object value;

    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
        this.lineNumber = -1;
    }

    public Token(TokenType type) {
        this.type = type;
        this.lineNumber = -1;
    }

    public Token(TokenType type, int lineNumber) {
        this.type = type;
        this.lineNumber = lineNumber;
    }

    public Token(TokenType type, Object value, int lineNumber) {
        this.type = type;
        this.value = value;
        this.lineNumber = lineNumber;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof Token))
            return false;

        return this.type == ((Token) obj).type && this.value.equals(((Token) obj).value);
    }

    public TokenType getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
