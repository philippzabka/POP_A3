package ast;

import java.util.ArrayList;

public class AdditiveExpression extends Expression{
    public ArrayList<Expression> expressions;
    public ArrayList<String> operators;

    public AdditiveExpression(ArrayList<Expression> expressions, ArrayList<String> operators){
        this.expressions = expressions;
        this.operators = operators;
    }
}
