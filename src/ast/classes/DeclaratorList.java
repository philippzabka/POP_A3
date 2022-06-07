package ast.classes;

import java.util.ArrayList;

public class DeclaratorList extends Expression {

    public ArrayList<Expression> expressions;

    public DeclaratorList(ArrayList<Expression> expressions){
        this.expressions = expressions;
    }
}
