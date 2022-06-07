package ast.classes;

public class IterationStatement extends Expression {
    public String forToken;
    public Expression forConditionExpr;
    public Expression statementExpr;

    public IterationStatement(String forToken, Expression forConditionExpr, Expression statementExpr){
        this.forToken = forToken;
        this.forConditionExpr = forConditionExpr;
        this.statementExpr = statementExpr;

    }
}
