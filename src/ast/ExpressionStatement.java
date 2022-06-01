package ast;

public class ExpressionStatement extends Expression{

    public String variableName;
    public String operator;
    public int value;

    public ExpressionStatement(String variableName, String operator, int value){
        this.variableName = variableName;
        this.operator = operator;
        this.value = value;
    }
}
