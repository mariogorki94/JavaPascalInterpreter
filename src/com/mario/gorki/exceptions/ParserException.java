package com.mario.gorki.exceptions;

public class ParserException extends Exception {
    private int lineNumber;

    public ParserException(String string, int lineNumber) {
        super(string);
        this.lineNumber = lineNumber;
    }

    @Override
    public String getMessage() {
        return String.format("%s %d : %s", "Parse Error at line ", lineNumber, super.getMessage());
    }
}
