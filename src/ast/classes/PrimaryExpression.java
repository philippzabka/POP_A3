package ast.classes;

public class PrimaryExpression extends Expression {
    public String value;
    public Expression expr;

    public PrimaryExpression(Expression expr){
        this.expr = expr;
    }
    public PrimaryExpression(String value){
        this.value = value;
    }
}
