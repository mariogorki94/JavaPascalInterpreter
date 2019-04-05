package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;

public class IfElse implements AST {

    public Condition condition;
    public AST trueExpression;
    public AST falseExpression;

    public IfElse(Condition condition, AST trueExpression, AST falseExpression) {
        this.condition = condition;
        this.trueExpression = trueExpression;
        this.falseExpression = falseExpression;
    }

    @Override
    public String getValue() {
        return "IF";
    }

    @Override
    public AST[] getChildren() {
        AST[] data = new AST[falseExpression == null ? 1 : 2];
        data[0] = trueExpression;
        if (falseExpression != null)
            data[1] = falseExpression;
        return data;
    }

}
