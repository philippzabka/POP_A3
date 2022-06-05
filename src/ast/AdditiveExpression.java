package ast;

import java.util.ArrayList;

public class AdditiveExpression extends Expression{

//    public Expression left;
//    public Expression right;
//    public Expression straight;
//
//    public AdditiveExpression(Expression left, Expression right){
//        this.left = left;
//        this.right = right;
//    }
//
//    public AdditiveExpression(Expression straight){
//        this.straight = straight;
//    }

    public ArrayList<Expression> expressions;
    public ArrayList<String> operators;

    public AdditiveExpression(ArrayList<Expression> expressions, ArrayList<String> operators){
        this.expressions = expressions;
        this.operators = operators;
    }
}
