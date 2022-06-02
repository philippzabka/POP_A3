package ast;

public class FunctionDefinition extends Expression {
    public Expression type;
    public Expression methodName;
    public Expression exprRight;

    public FunctionDefinition(Expression type, Expression methodName, Expression exprRight){
        this.type = type;
        this.methodName = methodName;
        this.exprRight = exprRight;
    }

}
