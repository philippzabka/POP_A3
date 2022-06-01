package ast;

import antlr.CBaseVisitor;
import antlr.CParser;

import java.util.ArrayList;
import java.util.Arrays;

public class AntlrToExpression extends CBaseVisitor<Expression> {

    public AntlrToExpression(){}

    public Expression visitFunctionDefinition(CParser.FunctionDefinitionContext ctx){
        String type = ctx.getChild(0).getText();
        String methodName = ctx.getChild(1).getText();
        Expression exprRight = visit(ctx.getChild(2));

        return new FunctionDefinition(type, methodName, exprRight);
    }

    public Expression visitCompoundStatement(CParser.CompoundStatementContext ctx){
        String bracketLeft = ctx.getChild(0).getText();
        Expression exprMiddle = visit(ctx.getChild(1));
        String bracketRight = ctx.getChild(2).getText();

        return new CompoundStatement(bracketLeft, exprMiddle, bracketRight);
    }

    public Expression visitDeclaration(CParser.DeclarationContext ctx){
        String type = ctx.getChild(0).getText();
        String variablesStr = ctx.getChild(1).getText();
        String[] arrOfStr = variablesStr.split(",");
        ArrayList<String> variables = new ArrayList<>(Arrays.asList(arrOfStr));
        for(String a: variables)
            System.out.println(a);

        return new Declaration(type, variables);
    }

    public Expression visitDirectDeclarator(CParser.DirectDeclaratorContext ctx){
        String name = ctx.getChild(0).getText();
        String bracketLeft = ctx.getChild(1).getText();
        String bracketRight = ctx.getChild(2).getText();

        return new DirectDeclarator(name, bracketLeft, bracketRight);
    }

    public Expression visitAssignmentExpression(CParser.AssignmentExpressionContext ctx){
        String variableName = ctx.getChild(0).getText();
        String operator = ctx.getChild(1).getText();
        int value = Integer.parseInt(ctx.getChild(2).getText());

        return new ExpressionStatement(variableName, operator, value);
    }
}
