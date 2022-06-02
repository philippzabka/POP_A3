package ast;

public class AssignmentExpression extends Expression{

    public Expression left;
    public String operator;
    public Expression right;

    public AssignmentExpression(Expression left, String operator, Expression right){
        this.left = left;
        this.operator = operator;
        this.right = right;
    }

    public AssignmentExpression(Expression right){
        this.right = right;
    }
}
