package com.mario.gorki.tokens;


public enum TokenType {
    /**
     * OPERATIONS
     */
    PLUS("PLUS"),
    MINUS("MINUS"),
    MULT("MULT"),
    INTEGER_DIV("DIV"),
    FLOAT_DIV("FDIV"),
    /**
     * PARENTHESIS
     */
    PAREN_L("L_PAREN"),
    PAREN_R("R_PAREN"),
    /**
     * BRACKETS
     */
    BRACKET_L("L_BRACKET"),
    BRACKET_R("R_BRACKET"),
    /**
     * CONSTANTS
     */
    CONST_INT("INTEGER_CONST"),
    CONST_REAL("REAL_CONST"),
    CONST_BOOL("BOOL_CONST"),
    CONST_STRING("STRING_CONST"),
    /**
     * TYPES
     */
    TYPE_INT("INTEGER"),
    TYPE_REAL("REAL"),
    TYPE_BOOL("BOOL"),
    TYPE_STRING("STRING"),

    /**
     * COMMON
     */
    EOF("EOF"),
    BEGIN("BEGIN"),
    END("END"),
    ID("ID"),
    DOT("DOT"),
    ASSIGN("ASSIGN"),
    SEMI("SEMI"),
    PROGRAM("PROGRAM"),
    VAR_DEF("VAR"),
    CONST_DEF("CONST"),
    COLON(":"),
    COMA(","),
    PROCEDURE("PROCEDURE"),
    IF("IF"),
    ELSE("ELSE"),
    THEN("THEN"),
    EQUALS("EQ"),
    LESS_THAN("LT"),
    GREATER_THAN("GT"),
    FUNCTION("FUNC"),
    APOSTROPHE("APOSTROPHE"),
    REPEAT("REPEAT"),
    UNTIL("UNTIL"),
    FOR("FOR"),
    TO("TO"),
    DO("DO"),
    WHILE("WHILE"),
    ARRAY("ARRAY"),
    OF("OF"),
    NO_OP("NO OP");

    private final String name;

    TokenType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
