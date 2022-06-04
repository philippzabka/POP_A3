package ast;

public class SelectionStatement extends Expression {

    public String ifToken;
    public String elseToken;
    public String bracketLeft;
    public String bracketRight;
    public Expression ifClauseExpr;

    public Expression ifExpr;
    public Expression elseExpr;

    public SelectionStatement(String ifToken, String bracketLeft, Expression ifClauseExpr,
                              String bracketRight, Expression ifExpr, String elseToken,
                              Expression elseExpr){

        this.ifToken = ifToken;
        this.bracketLeft = bracketLeft;
        this.ifClauseExpr = ifClauseExpr;
        this.bracketRight = bracketRight;
        this.ifExpr = ifExpr;
        this.elseToken = elseToken;
        this.elseExpr = elseExpr;
    }
}
