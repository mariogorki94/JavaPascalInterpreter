package com.mario.gorki.abstract_tree.enums;

public enum Type {
    INT("INTEGER"),
    REAL("REAL"),
    BOOL("BOOL"),
    STRING("STRING");

    private final String name;

    Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
