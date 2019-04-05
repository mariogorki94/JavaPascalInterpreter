package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.data.VariableType;

public class Param implements AST {
    public String name;
    public VariableType type;

    public Param(String name, VariableType type) {
        this.name = name;
        this.type = type;
    }

    @Override
    public String getValue() {
        return "param " + name;
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{type};
    }

}
