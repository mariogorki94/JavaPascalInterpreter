package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.data.Variable;

public class Assignment implements AST {
    public Variable left;
    public AST right;

    public Assignment(Variable left, AST right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String getValue() {
        return ":=";
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{left, right};
    }

}
