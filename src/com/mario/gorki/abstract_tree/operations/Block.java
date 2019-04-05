package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Declaration;

import java.util.ArrayList;

public class Block implements AST {
    public ArrayList<Declaration> declarations;
    public Compound compound;

    public Block(ArrayList<Declaration> declarations, Compound compound) {
        this.declarations = declarations;
        this.compound = compound;
    }


    @Override
    public String getValue() {
        return "block";
    }

    @Override
    public AST[] getChildren() {
        AST[] data = new AST[declarations.size() + 1];
        for (int i = 0; i < declarations.size() ; i ++){
            data[i] = declarations.get(i);
        }
        data[declarations.size()] = compound;
        return data;
    }

}
