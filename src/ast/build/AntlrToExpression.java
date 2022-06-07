package ast.build;

import antlr.CBaseVisitor;
import antlr.CParser;
import ast.classes.*;

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
        Expression exprMiddle = visit(ctx.getChild(1));
        return new CompoundStatement(exprMiddle);
    }

    public Expression visitBlockItemList(CParser.BlockItemListContext ctx){
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
        }
        return new DeclarationSpecifiers(specifiers);
    }

    public Expression visitDirectDeclarator(CParser.DirectDeclaratorContext ctx){
        String name = ctx.getChild(0).getText();
        return new DirectDeclarator(name);
    }

    public Expression visitAssignmentExpression(CParser.AssignmentExpressionContext ctx){
        if(ctx.getChildCount() > 1) {
            Expression leftSide = visit(ctx.getChild(0));
            String operator = ctx.getChild(1).getText();
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
            Expression leftSide = visit(ctx.getChild(0));
            String operator = ctx.getChild(1).getText();
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
        Expression ifClauseExpr = visit(ctx.getChild(2));
        Expression ifExpr = visit(ctx.getChild(4));
        String elseToken = ctx.getChild(5).getText();
        Expression elseExpr = visit(ctx.getChild(6));
        return new SelectionStatement(ifToken, ifClauseExpr, ifExpr, elseToken, elseExpr);
    }

    public Expression visitPrimaryExpression(CParser.PrimaryExpressionContext ctx){
        if (ctx.getChildCount() > 1){
            Expression expr = visit(ctx.getChild(1));
            return new PrimaryExpression(expr);
        }
        else {
            String value = ctx.getChild(0).getText();
            return new PrimaryExpression(value);
        }
    }

    public Expression visitIterationStatement(CParser.IterationStatementContext ctx){
        String forToken = ctx.getChild(0).getText();
        Expression forConditionExpr = visit(ctx.getChild(2));
        Expression statementExpr = visit(ctx.getChild(4));
        return new IterationStatement(forToken, forConditionExpr, statementExpr);
    }

    public Expression visitForCondition(CParser.ForConditionContext ctx){
        Expression left = visit(ctx.getChild(0));
        Expression middle = visit(ctx.getChild(2));
        Expression right = visit(ctx.getChild(4));
        return new ForCondition(left, middle, right);
    }
}
