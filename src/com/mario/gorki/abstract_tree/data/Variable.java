package com.mario.gorki.abstract_tree.data;

import com.mario.gorki.abstract_tree.AST;

public class Variable implements AST {

    public String name;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public AST[] getChildren() {
        return new AST[0];
    }

}
