package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.data.VariableType;

import java.util.ArrayList;

public class Function extends Procedure {
    public VariableType returnType;

    public Function(String name, ArrayList<Param> params, Block block, VariableType returnType) {
        super(name, params, block);
        this.returnType = returnType;
    }

    @Override
    public String getValue() {
        return name + " : " + returnType.type;
    }
}
