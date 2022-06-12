package main;

import antlr.CLexer;
import ast.build.AntlrToCompUnit;
import antlr.CParser;
import ast.classes.CompilationUnit;
import ast.process.ExpressionProcessor;
import ast.process.ExpressionProcessorToRiscV;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.IOException;

public class Parser {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: file name \n");
        } else {
            String fileName = args[0];
            CParser parser = getParser(fileName);

            ParseTree ast = parser.compilationUnit();
            AntlrToCompUnit visitor = new AntlrToCompUnit();
            CompilationUnit startingRule = visitor.visit(ast);
//            ExpressionProcessor ep = new ExpressionProcessor(startingRule.expressions);
//            ep.initEvaluation();
//            ep.symbolTable.forEach((k, v) -> System.out.println((k + " = " + v)));

            ExpressionProcessorToRiscV ep = new ExpressionProcessorToRiscV(startingRule.expressions);
            ep.initEvaluation();
            ep.symbolTable.forEach((k, v) -> System.out.println((k + " = " + v)));
        }
    }

    private static CParser getParser(String fileName) {
        CParser parser = null;
        try {
            CharStream input = CharStreams.fromFileName(fileName);
            CLexer lexer = new CLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            parser = new CParser(tokens);
        } catch(IOException e){
            e.printStackTrace();
        }
        return parser;
    }
}