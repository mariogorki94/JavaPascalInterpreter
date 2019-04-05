package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Declaration;

public class ConstantDeclaration implements Declaration {
    public String name;
    public AST value;

    public ConstantDeclaration(String name, AST value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public String getValue() {
        return "const " + name;
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{value};
    }
}
