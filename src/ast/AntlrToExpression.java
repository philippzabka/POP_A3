package ast;

import antlr.CBaseVisitor;
import antlr.CParser;

import java.security.spec.ECField;
import java.util.ArrayList;

public class AntlrToExpression extends CBaseVisitor<Expression> {

    public AntlrToExpression(){}

    public Expression visitFunctionDefinition(CParser.FunctionDefinitionContext ctx){
        Expression type = visit(ctx.getChild(0));
        Expression methodName = visit(ctx.getChild(1));
        Expression exprBody = visit(ctx.getChild(2));

        return new FunctionDefinition(type, methodName, exprBody);
    }

    public Expression visitCompoundStatement(CParser.CompoundStatementContext ctx){
        String bracketLeft = ctx.getChild(0).getText();
//        System.out.println(bracketLeft);
        Expression exprMiddle = visit(ctx.getChild(1));
        String bracketRight = ctx.getChild(2).getText();
//        System.out.println(bracketRight);

        return new CompoundStatement(bracketLeft, exprMiddle, bracketRight);
    }

    public Expression visitBlockItemList(CParser.BlockItemListContext ctx){
//        System.out.println(ctx.getChildCount());
        ArrayList<Expression> expressions = new ArrayList<>();
        for(int i = 0; i < ctx.getChildCount(); i++){
            expressions.add(visit(ctx.getChild(i)));
        }
        return new BlockItemList(expressions);
    }

    public Expression visitInitDeclaratorList(CParser.InitDeclaratorListContext ctx){
        ArrayList<Expression> expressions = new ArrayList<>();
        for(int i = 0; i < ctx.getChildCount(); i++){
            expressions.add(visit(ctx.getChild(i)));
        }
        return new DeclaratorList(expressions);
    }

    public Expression visitExpressionStatement(CParser.ExpressionStatementContext ctx){
        Expression expr = visit(ctx.getChild(0));
        return new ExpressionStatement(expr);
    }

    public Expression visitDeclaration(CParser.DeclarationContext ctx){

        if(ctx.getChildCount() > 1){
            Expression left = visit(ctx.getChild(0));
            Expression right = visit(ctx.getChild(1));
            return new Declaration(left, right);
        }
        else {
            Expression left = visit(ctx.getChild(0));
            return new Declaration(left);
        }
    }

    public Expression visitDeclarationSpecifiers(CParser.DeclarationSpecifiersContext ctx){

        ArrayList<String> specifiers = new ArrayList<>();
        for(int i = 0; i < ctx.getChildCount(); i++){
            specifiers.add(ctx.getChild(i).getText());
//            System.out.println(ctx.getChild(i).getText());
        }
        return new DeclarationSpecifiers(specifiers);
    }

    public Expression visitDirectDeclarator(CParser.DirectDeclaratorContext ctx){
        if(ctx.getChildCount() > 1){
            String name = ctx.getChild(0).getText();
            String bracketLeft = ctx.getChild(1).getText();
            String bracketRight = ctx.getChild(2).getText();
//            System.out.println(name + bracketLeft + bracketRight);
            return new DirectDeclarator(name, bracketLeft, bracketRight);
        }
        else {
            String name = ctx.getChild(0).getText();
//            System.out.println(name);
            return new DirectDeclarator(name);
        }
    }

    public Expression visitAssignmentExpression(CParser.AssignmentExpressionContext ctx){
        if(ctx.getChildCount() > 1) {
            Expression leftSide = visit(ctx.getChild(0));
            String operator = ctx.getChild(1).getText();
//            System.out.println(operator);
            Expression rightSide = visit(ctx.getChild(2));
            return new AssignmentExpression(leftSide, operator, rightSide);
        }
        else {
            Expression rightSide = visit(ctx.getChild(0));
            return new AssignmentExpression(rightSide);
        }
    }

    public Expression visitAdditiveExpression(CParser.AdditiveExpressionContext ctx){
        ArrayList<Expression> exprs = new ArrayList<>();
        ArrayList<String> operators = new ArrayList<>();
        for(int i = 0; i < ctx.getChildCount(); i++){
            if(ctx.getChild(i) instanceof CParser.MultiplicativeExpressionContext){
                exprs.add(visit(ctx.getChild(i)));
            }
            else {
                operators.add(ctx.getChild(i).getText());
            }
        }
        return new AdditiveExpression(exprs, operators);
    }

    public Expression visitRelationalExpression(CParser.RelationalExpressionContext ctx){
        if(ctx.getChildCount() > 1) {
            System.out.println("CHILD" + ctx.getChildCount());
            Expression leftSide = visit(ctx.getChild(0));
            String operator = ctx.getChild(1).getText();
            System.out.println(operator);
            Expression rightSide = visit(ctx.getChild(2));
            return new RelationalExpression(leftSide, operator, rightSide);
        }
        else {
            Expression rightSide = visit(ctx.getChild(0));
            return new RelationalExpression(rightSide);
        }
    }

    public Expression visitSelectionStatement(CParser.SelectionStatementContext ctx){
        String ifToken = ctx.getChild(0).getText();
        String bracketLeft = ctx.getChild(1).getText();
//        System.out.println(ifToken + bracketLeft);

        Expression ifClauseExpr = visit(ctx.getChild(2));
        String bracketRight = ctx.getChild(3).getText();
//        System.out.println(bracketRight);

        Expression ifExpr = visit(ctx.getChild(4));
        String elseToken = ctx.getChild(5).getText();
//        System.out.println(elseToken);
        Expression elseExpr = visit(ctx.getChild(6));

        return new SelectionStatement(ifToken, bracketLeft, ifClauseExpr, bracketRight, ifExpr, elseToken, elseExpr);
    }

    public Expression visitPrimaryExpression(CParser.PrimaryExpressionContext ctx){
        if (ctx.getChildCount() > 1){
            String bracketLeft = ctx.getChild(0).getText();
//            System.out.println(bracketLeft);
            Expression expr = visit(ctx.getChild(1));
            String bracketsRight = ctx.getChild(2).getText();
//            System.out.println(bracketsRight);
            return new PrimaryExpression(bracketLeft, expr, bracketsRight);
        }
        else {
            String value = ctx.getChild(0).getText();
//            System.out.println(value);
            return new PrimaryExpression(value);
        }
    }

    public Expression visitIterationStatement(CParser.IterationStatementContext ctx){
        String forToken = ctx.getChild(0).getText();
        String bracketLeft = ctx.getChild(1).getText();
//        System.out.println(forToken + bracketLeft);
        Expression forConditionExpr = visit(ctx.getChild(2));
        String bracketRight = ctx.getChild(3).getText();
//        System.out.println(bracketRight);
        Expression statementExpr = visit(ctx.getChild(4));

        return new IterationStatement(forToken, bracketLeft, forConditionExpr, bracketRight, statementExpr);
    }

    public Expression visitForCondition(CParser.ForConditionContext ctx){
        Expression left = visit(ctx.getChild(0));
        Expression middle = visit(ctx.getChild(2));
        Expression right = visit(ctx.getChild(4));
        return new ForCondition(left, middle, right);
    }
}
