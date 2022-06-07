package ast.classes;

public class SelectionStatement extends Expression {

    public String ifToken;
    public String elseToken;
    public Expression ifClauseExpr;
    public Expression ifExpr;
    public Expression elseExpr;

    public SelectionStatement(String ifToken, Expression ifClauseExpr, Expression ifExpr, String elseToken,
                              Expression elseExpr){

        this.ifToken = ifToken;
        this.ifClauseExpr = ifClauseExpr;
        this.ifExpr = ifExpr;
        this.elseToken = elseToken;
        this.elseExpr = elseExpr;
    }
}
