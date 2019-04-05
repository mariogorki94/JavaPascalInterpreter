package com.mario.gorki.abstract_tree.enums;

public enum BinaryOperationType {
    PLUS("+"),
    MINUS("-"),
    MULT("*"),
    FLOAT_DIV("//"),
    INTEGER_DIV("/");

    private final String name;

    BinaryOperationType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
