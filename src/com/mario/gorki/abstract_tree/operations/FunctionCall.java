package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;

import java.util.ArrayList;

public class FunctionCall implements AST {
    public String name;
    public ArrayList<AST> actualParamters;

    public FunctionCall(String name, ArrayList<AST> actualParamters) {
        this.name = name;
        this.actualParamters = actualParamters;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public AST[] getChildren() {
        return actualParamters.toArray(new AST[0]);
    }

}
