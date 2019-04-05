package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Loop;

import java.sql.Statement;

public class RepeatUntil extends Loop {


    public RepeatUntil(AST statement, Condition condition) {
        super(statement, condition);
    }

    @Override
    public String getValue() {
        return "REPEAT";
    }

}
