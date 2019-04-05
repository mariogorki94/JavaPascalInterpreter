package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.enums.UnaryOperationType;

import java.util.ArrayList;

public class UnaryOperation implements AST {
    public UnaryOperationType operation;
    public AST operand;

    public UnaryOperation(UnaryOperationType operation, AST operand) {
        this.operation = operation;
        this.operand = operand;
    }

    @Override
    public String getValue() {
        return "u " + operation.toString();
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{operand};
    }

}
