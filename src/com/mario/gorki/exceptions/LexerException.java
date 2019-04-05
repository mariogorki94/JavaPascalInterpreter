package com.mario.gorki.exceptions;

public class LexerException extends Exception {
    public int lineNumber;

    public LexerException(String string, int line){
        super(string);
        this.lineNumber = line;
    }

    public String getMessage() {
        return new StringBuilder().append("lineNumber:").append(lineNumber).append(" ").append(super.getMessage()).toString();
    }
}
