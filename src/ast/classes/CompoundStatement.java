package ast.classes;

public class CompoundStatement extends Expression {
    public Expression exprMiddle;

    public CompoundStatement(Expression exprMiddle){
        this.exprMiddle = exprMiddle;
    }
}
