package ast;

import java.util.ArrayList;
import java.util.List;

public class Declaration extends Expression {

    public Expression left;
    public Expression right;

    public Declaration(Expression left, Expression right){
        this.left = left;
        this.right = right;
    }

    public Declaration(Expression left){
        this.left = left;
    }
}
