package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.enums.ConditionType;

public class Condition implements AST {

    public ConditionType conditionType;
    public AST leftSide;
    public AST rightSide;

    public Condition(ConditionType conditionType, AST leftSide, AST rightSide) {
        this.conditionType = conditionType;
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    @Override
    public String getValue() {
        return conditionType.toString();
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{leftSide, rightSide};
    }

}
