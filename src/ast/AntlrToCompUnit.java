package ast;

import antlr.CBaseVisitor;
import antlr.CParser;

public class AntlrToCompUnit extends CBaseVisitor<CompilationUnit> {

    public CompilationUnit visitCompilationUnit (CParser.CompilationUnitContext ctx){
        AntlrToExpression exprVisitor = new AntlrToExpression();
        CompilationUnit compilationUnit = new CompilationUnit();
        for(int i = 0; i < ctx.getChildCount(); i++){
            if(i != ctx.getChildCount() - 1) {
                // Skip EOF
//                System.out.println(ctx.getChild(i).getText());
                compilationUnit.addExpression(exprVisitor.visit(ctx.getChild(i)));
            }
        }
        return compilationUnit;
    }

}
