package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.enums.BinaryOperationType;

import java.util.ArrayList;

public class BinaryOperation implements AST {

    public AST left;
    public BinaryOperationType operation;
    public AST right;

    public BinaryOperation(AST left, BinaryOperationType operation, AST right) {
        this.left = left;
        this.operation = operation;
        this.right = right;
    }

    @Override
    public String getValue() {
        return operation.toString();
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{left, right};
    }


}
