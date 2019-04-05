package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;

public class NoOp implements AST {
    @Override
    public String getValue() {
        return "noOp";
    }

    @Override
    public AST[] getChildren() {
        return new AST[0];
    }

}
