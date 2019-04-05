package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.data.Variable;

public class For implements AST {

    public AST statement;
    public Variable variable;
    public AST startValue;
    public AST endValue;

    public For(AST statement, Variable variable, AST startValue, AST endValue) {
        this.statement = statement;
        this.variable = variable;
        this.startValue = startValue;
        this.endValue = endValue;
    }

    @Override
    public String getValue() {
        return "FOR";
    }

    @Override
    public AST[] getChildren() {
        return new AST[]{statement};
    }

}
