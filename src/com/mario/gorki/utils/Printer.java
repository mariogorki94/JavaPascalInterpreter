package com.mario.gorki.utils;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.semanthic_analyzer.ScopedSymbolTable;
import java.util.Collection;

public class Printer {
    public static void printTree(AST ast){
        System.out.println("----------ABSTRACT SYNTAX TREE----------");
       printNode(ast, 0);

    }

    public static void printTables(Collection<ScopedSymbolTable> data){
        System.out.println("----------SCOPED SYMBOL TABLES----------");
        for (ScopedSymbolTable t : data){
            System.out.println(t.toString());
        }
    }

    private static void printNode(AST node, int offset){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i <= offset; i++){
            builder.append("  ");
        }
        builder.append(node.getValue());

        System.out.println(builder.toString());
        AST[] children = node.getChildren();

        if (children.length > 0)
            offset ++;

        for (AST c : children){
            printNode(c, offset);
        }
    }
}
