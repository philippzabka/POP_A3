package ast.classes;

public class ForCondition extends Expression {
    public Expression left;
    public Expression middle;
    public Expression right;

    public ForCondition(Expression left, Expression middle, Expression right){
        this.left = left;
        this.middle = middle;
        this.right = right;
    }
}
