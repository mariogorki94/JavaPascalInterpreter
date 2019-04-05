package com.mario.gorki.abstract_tree.enums;

public enum  UnaryOperationType {
    PLUS("+"),
    MINUS("-");

    private final String name;

    UnaryOperationType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
