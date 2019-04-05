package com.mario.gorki.abstract_tree.enums;

public enum ConditionType {
    EQUALS("="),
    GREATER_THAN(">"),
    LESS_THAN("<");

    private final String name;

    ConditionType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
