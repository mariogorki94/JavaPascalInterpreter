package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Declaration;
import com.mario.gorki.abstract_tree.data.Variable;
import com.mario.gorki.abstract_tree.data.VariableType;

public class VariableDeclaration implements Declaration {
    public Variable variable;
    public VariableType type;

    public VariableDeclaration(Variable variable, VariableType type) {
        this.variable = variable;
        this.type = type;
    }

    @Override
    public String getValue() {
        return "var";
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{variable, type};
    }
}
