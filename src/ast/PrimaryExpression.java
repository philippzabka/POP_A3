package ast;

public class PrimaryExpression extends Expression {
    public String bracketLeft;
    public String value;
    public Expression expr;
    public String bracketRight;

    public PrimaryExpression(String bracketLeft, Expression expr, String bracketRight){
        this.bracketLeft = bracketLeft;
        this.expr = expr;
        this.bracketRight = bracketRight;
    }
    public PrimaryExpression(String value){
        this.value = value;
    }
}
