package com.mario.gorki;

import com.mario.gorki.abstract_tree.AST;
import com.mario.gorki.interpreter.Interpreter;
import com.mario.gorki.lexer.Lexer;
import com.mario.gorki.parser.Parser;
import com.mario.gorki.semanthic_analyzer.ScopedSymbolTable;
import com.mario.gorki.semanthic_analyzer.SemanticAnalyzer;
import com.mario.gorki.tokens.Token;
import com.mario.gorki.utils.Printer;

import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        FileReader reader = checkForFile(scanner);

        try {
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser();
            SemanticAnalyzer analyzer = new SemanticAnalyzer();
            Interpreter interpreter = new Interpreter();

            ArrayList<Token> tokens = lexer.lex();
            AST ast = parser.parse(tokens);
            HashMap<String, ScopedSymbolTable> scopes = analyzer.analyze(ast);

            Printer.printTree(ast);
            System.out.println("\n\n");
            Printer.printTables(scopes.values());

            System.out.println("\n\n TO INTERPRET TYPE \"GO\" AND PRESS ENTER");
            String command;
            do {
                command = scanner.next();
            } while (!command.toLowerCase().equals("go"));

            interpreter.interpret(ast, scopes);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static FileReader checkForFile(Scanner scanner) {
        try {
            System.out.println("TYPE FILE NAME AND PRESS ENTER (THE FILE MUST BE IN THIS PROGRAM DIRECTORY)");
            String filename = scanner.next();
            return new FileReader(filename);
        } catch (Exception e) {
            System.out.println("File not found");
            return checkForFile(scanner);
        }
    }
}
