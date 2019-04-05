package com.mario.gorki.abstract_tree;

import com.mario.gorki.abstract_tree.operations.Condition;

public abstract class Loop implements AST {
    public AST statement;
    public Condition condition;

    public Loop(AST statement, Condition condition) {
        this.statement = statement;
        this.condition = condition;
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{statement, condition};
    }
}
