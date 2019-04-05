package com.mario.gorki.abstract_tree.data;

import com.mario.gorki.abstract_tree.AST;

public class ArrayVariable extends Variable {

    public AST index;

    public ArrayVariable(String name, AST index) {
        super(name);
        this.index = index;

    }
}
