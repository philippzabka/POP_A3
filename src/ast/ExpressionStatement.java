package ast;

public class ExpressionStatement extends Expression {

    public Expression expr;

    public ExpressionStatement(Expression expr){
        this.expr = expr;
    }
}
