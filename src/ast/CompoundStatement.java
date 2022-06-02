package ast;

public class CompoundStatement extends Expression {
    public String bracketLeft;
    public Expression exprMiddle;
    public String bracketRight;

    public CompoundStatement(String bracketLeft, Expression exprMiddle, String bracketRight){
        this.bracketLeft = bracketLeft;
        this.exprMiddle = exprMiddle;
        this.bracketRight = bracketRight;
    }
}
