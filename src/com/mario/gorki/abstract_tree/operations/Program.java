package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;

public class Program implements AST{
    public String name;
    public Block block;

    public Program(String name, Block block) {
        this.name = name;
        this.block = block;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{block};
    }

}
