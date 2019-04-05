package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Loop;

public class While extends Loop {
    public While(AST statement, Condition condition) {
        super(statement, condition);
    }

    @Override
    public String getValue() {
        return "WHILE";
    }

}
