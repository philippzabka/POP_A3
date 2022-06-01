import antlr.CLexer;
import ast.AntlrToCompUnit;
import antlr.CParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        if (args.length != 1) {
            System.out.println("Usage: file name \n");
        } else {
            String fileName = args[0];
            CParser parser = getParser(fileName);

            AntlrToCompUnit anltrCompUnit = new AntlrToCompUnit();
            anltrCompUnit.visitCompilationUnit(parser.compilationUnit());

//            AntlrToExpression expr = new AntlrToExpression();
//            expr.visitDeclaration(parser.declaration());
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