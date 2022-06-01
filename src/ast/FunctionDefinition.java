package ast;

public class FunctionDefinition extends Expression{

    public String type;
    public String methodName;
    public Expression exprRight;

    public FunctionDefinition(String type, String methodName, Expression exprRight){
        this.type = type;
        this.methodName = methodName;
        this.exprRight = exprRight;
    }

}
