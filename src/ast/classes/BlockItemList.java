package ast.classes;

import java.util.ArrayList;

public class BlockItemList extends Expression {

    public ArrayList<Expression> expressions;

    public BlockItemList(ArrayList<Expression> expressions){
        this.expressions = expressions;
    }
}
