package ast;

public class IterationStatement extends Expression {
    public String forToken;
    public String bracketLeft;
    public Expression forConditionExpr;
    public String bracketRight;
    public Expression statementExpr;

    public IterationStatement(String forToken, String bracketLeft, Expression forConditionExpr,
                              String bracketRight, Expression statementExpr){
        this.forToken = forToken;
        this.bracketLeft = bracketLeft;
        this.forConditionExpr = forConditionExpr;
        this.bracketRight = bracketRight;
        this.statementExpr = statementExpr;

    }
}
