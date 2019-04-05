package com.mario.gorki.abstract_tree.operations;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.abstract_tree.Declaration;

import java.util.ArrayList;

public class Procedure implements Declaration {
    public String name;
    public ArrayList<Param> params;
    public Block block;

    public Procedure(String name, ArrayList<Param> params, Block block) {
        this.name = name;
        this.params = params;
        this.block = block;
    }

    @Override
    public String getValue() {
        return name;
    }

    @Override
    public AST[] getChildren() {
        AST[] data = new AST[params.size() + 1];

        for (int i = 0; i < params.size(); i ++){
            data[i] = params.get(i);
        }
        data[params.size()] = block;
       return data;
    }

}
