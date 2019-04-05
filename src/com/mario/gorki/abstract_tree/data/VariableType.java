package com.mario.gorki.abstract_tree.data;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.enums.Type;

public class VariableType implements AST {

    public Type type;

    public VariableType(Type type) {
        this.type = type;
    }

    @Override
    public String getValue() {
        return type.toString();
    }

    @Override
    public AST[] getChildren() {
        return new AST[0];
    }
}
