package ast;

import java.util.ArrayList;
import java.util.List;

public class CompilationUnit {
    public List<Expression> expressions;

    public CompilationUnit() {
        this.expressions = new ArrayList<>();
    }

    public void addExpression(Expression e){
        expressions.add(e);
    }
}
