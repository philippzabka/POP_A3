package ast;

public class RelationalExpression extends Expression{

    public Expression leftSide;
    public String operator;
    public Expression rightSide;

    public RelationalExpression(Expression leftSide, String operator, Expression rightSide){
        this.leftSide = leftSide;
        this.operator = operator;
        this.rightSide = rightSide;
    }

    public RelationalExpression(Expression rightSide){
        this.rightSide = rightSide;
    }
}
