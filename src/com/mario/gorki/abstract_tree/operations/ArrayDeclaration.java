package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.data.Variable;
import com.mario.gorki.abstract_tree.data.VariableType;

public class ArrayDeclaration extends VariableDeclaration {
    public int startIndex;
    public int endIndex;

    public ArrayDeclaration(Variable variable, VariableType type, int startIndex, int endIndex) {
        super(variable, type);
        this.startIndex = startIndex;
        this.endIndex = endIndex;
    }
}
