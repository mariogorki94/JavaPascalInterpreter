package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;

import java.util.ArrayList;

public class Compound implements AST {
    public ArrayList<AST> children;

    public Compound(ArrayList<AST> children) {
        this.children = children;
    }

    @Override
    public String getValue() {
        return "compound";
    }

    @Override
    public AST[] getChildren() {
        return children.toArray(new AST[0]);
    }


}
